package com.suryansh.library.service;

import com.suryansh.library.dto.*;
import com.suryansh.library.entity.*;
import com.suryansh.library.exception.SpringLibraryException;
import com.suryansh.library.mapper.MappingService;
import com.suryansh.library.model.BorrowItemModel;
import com.suryansh.library.model.LibraryItemModel;
import com.suryansh.library.model.ReturnItemModel;
import com.suryansh.library.repository.FinePdfEntityRepo;
import com.suryansh.library.repository.IssuerEntityRepo;
import com.suryansh.library.repository.ItemBorrowsEntityRepo;
import com.suryansh.library.repository.LibraryItemsEntityRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LibraryServiceImpl implements LibraryService {
    private final static Logger logger = LoggerFactory.getLogger(LibraryServiceImpl.class);
    private final LibraryItemsEntityRepo libraryItemsEntityRepo;
    private final IssuerEntityRepo issuerEntityRepo;
    private final ItemBorrowsEntityRepo borrowsEntityRepo;
    private final MappingService mappingService;
    private final FinePdfEntityRepo finePdfEntityRepo;


    public LibraryServiceImpl(LibraryItemsEntityRepo libraryItemsEntityRepo, IssuerEntityRepo issuerEntityRepo, ItemBorrowsEntityRepo borrowsEntityRepo, MappingService mappingService, FinePdfEntityRepo finePdfEntityRepo) {
        this.libraryItemsEntityRepo = libraryItemsEntityRepo;
        this.issuerEntityRepo = issuerEntityRepo;
        this.borrowsEntityRepo = borrowsEntityRepo;
        this.mappingService = mappingService;
        this.finePdfEntityRepo = finePdfEntityRepo;
    }

    @Override
    public LibraryItemPagingDto getAll(int page_size, int page_no) {
        Pageable pageable = PageRequest.of(page_no, page_size);
        Page<LibraryItemsEntity> page = libraryItemsEntityRepo.findAll(pageable);
        return new LibraryItemPagingDto(
                pageable.getPageNumber(),
                page.getTotalPages(),
                page.get()
                        .map(mappingService::libraryEntityToDto)
                        .toList(),
                page.getTotalElements(),
                page_size
        );
    }

    @Override
    @Transactional
    public String saveNewItem(LibraryItemModel model) {
        var checkItem = libraryItemsEntityRepo.findByTitle(model.getTitle().toUpperCase());
        LibraryItemsEntity entity;
        String unique_id;
        if (checkItem.isPresent() && compareItems(checkItem.get(), model)) {
            // Update stock
            entity = checkItem.get();
            updateStockOfItem(model, entity);
            unique_id = entity.getUniqueId();
            logger.info("Item stock of unique id {} is going to update ", unique_id);
        } else if (checkItem.isEmpty()) {
            entity = mappingService.libraryItemModelToEntity(model);
            entity.setUniqueId(generateUUID());
        } else {
            throw new SpringLibraryException("Book is present but comparing is not working "
                    , "SomethingWentWrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            libraryItemsEntityRepo.save(entity);
            logger.info("New item {} is added in database with Unique Id {} ", model, entity.getUniqueId());
            return "Item saved successfully with unique id " + entity.getUniqueId();
        } catch (Exception e) {
            logger.error("Unable to save item " + e);
            throw new SpringLibraryException(e.getMessage(), e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public String borrowNewItem(BorrowItemModel model) {
        // Check issuer is present in db or not.
        var optionalIssuer = issuerEntityRepo.findByUniqueId(model.getIssuerUniqueId())
                .orElseThrow(() -> new SpringLibraryException("Issuer of this id is not present",
                        "IssuerNotPresent", HttpStatus.BAD_REQUEST));

        var todayLocalDateTime = LocalDateTime.now();
        // Check Issue Date if before Return Date.
        if (todayLocalDateTime.toLocalDate().isAfter(model.getExpectedReturnDate())) {
            throw new SpringLibraryException("Expected Return date can't be before issued Date",
                    "Wrong Expected Return Date", HttpStatus.BAD_REQUEST);
        }
        // Check weather issuer has fined by pending issue or not.
        optionalIssuer.getItemBorrows().forEach(ib -> {
            if (!ib.isReturnStatus()) {
                boolean isPeriodOver = todayLocalDateTime.toLocalDate().isAfter(ib.getExpectedReturnDate());
                if (isPeriodOver) {
                    throw new SpringLibraryException("Sorry User is fined because item " + ib.getLibraryItem().getTitle() + " " +
                            " is not returned on " + ib.getExpectedReturnDate()
                            , "UserIsFined", HttpStatus.BAD_REQUEST);
                }
            }
        });
        // Check item is present in a library.
        Optional<LibraryItemsEntity> optionalLibraryItem = libraryItemsEntityRepo
                .findByUniqueId(model.getItemUniqueId());
        if (optionalLibraryItem.isEmpty()) {
            throw new SpringLibraryException("Item of this id is not present in library",
                    "ItemNotPresent", HttpStatus.BAD_REQUEST);
        }
        // check weather we have desired stock or not.
        var itemStock = optionalLibraryItem.get().getItemStock();
        if (itemStock.getAvailableQuantity() < model.getQuantity()) {
            throw new SpringLibraryException("Sorry ,we have only stock of number " + itemStock.getAvailableQuantity(),
                    "ItemNotPresent", HttpStatus.BAD_REQUEST);
        }
        // Update Stock.
        if (itemStock.getAvailableQuantity() > 0) {
            itemStock.setAvailableQuantity(itemStock.getAvailableQuantity() - model.getQuantity());
        } else {
            itemStock.setAvailableQuantity(0);
        }
        itemStock.setLastCheckedOut(todayLocalDateTime);
        optionalLibraryItem.get().setItemStock(itemStock);
        var itemBorrows = ItemBorrowsEntity.builder()
                .libraryItem(optionalLibraryItem.get())
                .quantity(model.getQuantity())
                .borrowDate(todayLocalDateTime)
                .expectedReturnDate(model.getExpectedReturnDate())
                .issuer(optionalIssuer)
                .finePdf(null)
                .build();
        try {
            borrowsEntityRepo.save(itemBorrows);
            logger.info("Issuer of id: {} issued new item of id: {} ", model.getIssuerUniqueId(), model.getItemUniqueId());
            return "Successfully issued item to user";
        } catch (Exception e) {
            logger.error("Unable to issue item " + e);
            throw new SpringLibraryException("Something went wrong , check console",
                    "InternalServerError", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public String returnItemOnTime(ReturnItemModel model) {
        Optional<ItemBorrowsEntity> optionalItemBorrow = borrowsEntityRepo.findById(model.getBorrowId());
        // Check item is issued by issuer or not.
        if (optionalItemBorrow.isEmpty()) {
            throw new SpringLibraryException("Item of this id is not present in user issued repo ,Check borrow ID",
                    "ItemNotPresent", HttpStatus.BAD_REQUEST);
        }
        ItemBorrowsEntity itemBorrowed = optionalItemBorrow.get();
        // Check item is already returned or not.
        if (itemBorrowed.isReturnStatus()) {
            throw new SpringLibraryException("This item is already returned",
                    "ItemAlreadyReturned", HttpStatus.BAD_REQUEST);
        }
        // Check weather item issued by user or not.
        IssuerEntity issuer = itemBorrowed.getIssuer();
        if (!issuer.getUniqueId().equals(model.getIssuerUniqueId())) {
            throw new SpringLibraryException("Issuer not issued this item",
                    "ItemNotPresent", HttpStatus.BAD_REQUEST);
        }
        // check item return date is over or not.
        LocalDateTime todayDateTime = LocalDateTime.now();
        boolean isPeriodOver = todayDateTime.toLocalDate().isAfter(itemBorrowed.getExpectedReturnDate());
        if (isPeriodOver) {
            throw new SpringLibraryException("Sorry you have to return that item on " + itemBorrowed.getExpectedReturnDate() +
                    " ,Now pay fine please ",
                    "ReturnPeriodOver", HttpStatus.BAD_REQUEST);
        }
        // Check item is present in the library or not.
        LibraryItemsEntity libraryItem = itemBorrowed.getLibraryItem();
        if (!libraryItem.getUniqueId().equals(model.getItemUniqueId())) {
            throw new SpringLibraryException("Item is not present in library",
                    "ItemNotPresent", HttpStatus.BAD_REQUEST);
        }
        // Update Stock.
        ItemStockEntity stock = libraryItem.getItemStock();
        stock.setLastCheckedIn(todayDateTime);
        stock.setAvailableQuantity(stock.getAvailableQuantity() + itemBorrowed.getQuantity());
        libraryItem.setItemStock(stock);

        itemBorrowed.setActualReturnDate(todayDateTime);
        itemBorrowed.setItemCondition(model.getItemCondition().name());
        itemBorrowed.setReturnStatus(true);
        itemBorrowed.setLibraryItem(libraryItem);

        try {
            borrowsEntityRepo.save(itemBorrowed);
            logger.info("Item {} is successfully returned is issuer {} ", libraryItem.getUniqueId(), issuer.getUniqueId());
            return "Item is successfully returned .Place item at : -" + libraryItem.getItemLocation();
        } catch (Exception e) {
            logger.error("Unable to return item " + e);
            throw new SpringLibraryException("Unable to return item",
                    "SomeThingWentWrong", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public PaginationPage specificItemsByUser(String issuerUid, String itemUid, int page_size, int page_no) {
        Pageable pageable = PageRequest.of(page_no, page_size);
        var page = borrowsEntityRepo.getByIssuerAndItemId(issuerUid, itemUid, pageable);
        if (page.isEmpty()) {
            throw new SpringLibraryException("Check the item and issuer id again !! or Item is not issued or already returned", "UniqueIdWrong", HttpStatus.NOT_FOUND);
        }
        return new PaginationPage(
                page_no,
                page.getTotalPages(),
                page
                        .get()
                        .map(mappingService::borrowEntityToDto)
                        .toList(),
                page.getTotalElements(),
                page_size
        );
    }

    @Override
    public PaginationPage getAllBorrowsOfIssuer(String userUniqueId, int pageNo, int pageSize) {
        IssuerEntity issuer = issuerEntityRepo.findByUniqueId(userUniqueId)
                .orElseThrow(() -> new SpringLibraryException("Issuer of this id is not present",
                        "IssuerNotPresent", HttpStatus.BAD_REQUEST));
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ItemBorrowsEntity> page = borrowsEntityRepo.getAllItemsOfIssuer(issuer.getId(), pageable);
        return new PaginationPage(
                pageable.getPageNumber(),
                page.getTotalPages(),
                page
                        .get()
                        .map(mappingService::borrowEntityToDto)
                        .toList(),
                page.getTotalElements(),
                pageable.getPageSize()
        );
    }


    @Override
    public PaginationPage getAllPendingItemsFromDb(int pageSize, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ItemBorrowsEntity> page = borrowsEntityRepo.getAllPendingItemAndSort(pageable);
        return new PaginationPage(
                pageNo,
                page.getTotalPages(),
                page
                        .get()
                        .map(mappingService::borrowEntityToDto)
                        .toList(),
                page.getTotalElements(),
                pageSize
        );
    }

    @Override
    public LibraryItemPagingDto ItemFullTextSearch(String value, int pageNo, int pageSize, Boolean is_book) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<LibraryItemsEntity> page;
        if (is_book) {
            page = libraryItemsEntityRepo.searchBookItemFullText(pageable, value);
        } else {
            page = libraryItemsEntityRepo.searchItemFullText(pageable, value);
        }
        return new LibraryItemPagingDto(
                pageable.getPageNumber(),
                page.getTotalPages(),
                page.get()
                        .map(mappingService::libraryEntityToDto)
                        .toList(),
                page.getTotalElements(),
                pageable.getPageSize()
        );

    }

    @Override
    public List<BorrowItemDto> findAllPendingBorrowsOfUser(String userUniqueId) {
        IssuerEntity issuer = issuerEntityRepo.findByUniqueId(userUniqueId)
                .orElseThrow(() -> new SpringLibraryException("Issuer of this id is not present",
                        "IssuerNotPresent", HttpStatus.BAD_REQUEST));

        List<ItemBorrowsEntity> borrowsItem = borrowsEntityRepo.allPendingOfIssuer(issuer.getId());
        return borrowsItem.stream()
                .map(mappingService::borrowEntityToDto)
                .toList();
    }

    @Override
    public ItemFullDetailDto getItemFullDetail(String uniqueId) {
        LibraryItemsEntity item = libraryItemsEntityRepo.findByUniqueId(uniqueId)
                .orElseThrow(() -> new SpringLibraryException("Unable to find item of unique_id " + uniqueId,
                        "ItemNotFound", HttpStatus.NOT_FOUND));

        return mappingService.mapItemEntityToFullDto(item);
    }

    @Override
    public PaginationPage getAllBorrowsOfItem(String uniqueId, int page_no, int page_size) {
        LibraryItemsEntity item = libraryItemsEntityRepo.findByUniqueId(uniqueId)
                .orElseThrow(() -> new SpringLibraryException("Item not found of unique_id " + uniqueId,
                        "ItemNotFound", HttpStatus.BAD_REQUEST));
        Pageable pageable = PageRequest.of(page_no, page_size);
        Page<ItemBorrowsEntity> page = borrowsEntityRepo.findAllForItem(item.getId(), pageable);
        return new PaginationPage(
                page_no,
                page.getTotalPages(),
                page
                        .get()
                        .map(mappingService::borrowEntityToDto)
                        .toList(),
                page.getTotalElements(),
                page_size
        );
    }

    @Override
    public String updateItem(LibraryItemModel model, String uniqueId) {
        LibraryItemsEntity item = libraryItemsEntityRepo.findByUniqueId(uniqueId)
                .orElseThrow(() -> new SpringLibraryException("Sorry no item found of unique id " + uniqueId,
                        "ItemNotFound", HttpStatus.NOT_FOUND));
        if (model.getBookModel() != null || model.getMagazineModel() != null || !item.getItemType().equals(model.getItemType().name())) {
            throw new SpringLibraryException("Sorry this endpoint is only for updating item detail not for changing Item_Type"
                    , "WrongEndpointTask", HttpStatus.BAD_REQUEST);
        }
        if (model.getItemStock().getQuantity() != item.getItemStock().getQuantity()) {
            updateStockOfItem(model, item);
        }
        item.setTitle(model.getTitle());
        item.setSubject(model.getSubject());
        item.setDescription(model.getDescription());
        item.setItemLocation(model.getItemLocation());
        item.setItemLanguage(model.getItemLanguage());

        try {
            libraryItemsEntityRepo.save(item);
            logger.info("Item of unique id {} is going to update ", item.getUniqueId());
            return "Item is updated successfully ";
        } catch (Exception e) {
            logger.error("Unable to update item {} ", item.getUniqueId() + e);
            throw new SpringLibraryException("Unable to update item", "SomethingWentWrong",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public PaginationPage getAllPendingIssuerOfItemFromDb(String uniqueId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<IssuerEntity> page = issuerEntityRepo.findAllPendingIssuerOfItemDb(uniqueId, pageable);
        if (page.isEmpty()) {
            throw new SpringLibraryException("Item not found, or this item is not in pending status ",
                    "DataNotFound", HttpStatus.NOT_FOUND);
        }
        return new PaginationPage(
                pageNo,
                page.getTotalPages(),
                page
                        .get()
                        .map(mappingService::mapIssuerEntityToDto)
                        .toList(),
                page.getTotalElements(),
                pageSize
        );
    }

    @Override
    @Transactional
    public String increaseReturnDate(long borrowId, String newDate) {
        ItemBorrowsEntity borrow = borrowsEntityRepo.findById(borrowId)
                .orElseThrow(() -> new SpringLibraryException("BorrowItemNotFound"
                        , "Borrow item of this id not present "
                        , HttpStatus.BAD_REQUEST));

        if (borrow.isReturnStatus()) {
            throw new SpringLibraryException("ItemAlreadyReturned",
                    "Sorry item return date can not be extended because this item is already returned ",
                    HttpStatus.BAD_REQUEST);
        }

        borrow.setExpectedReturnDate(LocalDate.parse(newDate));
        try {
            borrowsEntityRepo.save(borrow);
            logger.info("Item {} return date is increased by 15 days ", borrow.getExpectedReturnDate());
            return "Expected return date is changed successfully !!";
        } catch (Exception e) {
            logger.error("Unable to change return date of item " + e);
            throw new SpringLibraryException("Unable to update item", "SomethingWentWrong",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Set<String> searchPublisher(String name) {
        return libraryItemsEntityRepo.searchPublisher(name);
    }

    @Override
    public PaginationPage getAllPaidFines(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize,Sort.by(Sort.Direction.DESC,"dateTime"));
        Page<FinePdfEntity>finePdfPage = finePdfEntityRepo.findAll(pageable);
        List<FineInfoDto>fineInfoList = finePdfPage.get()
                .map(mappingService::fineInfoEntityToDto).toList();

        return new PaginationPage(
                pageNo,
                finePdfPage.getTotalPages(),
                fineInfoList,
                finePdfPage.getTotalElements(),
                pageSize
        );
    }


    private void updateStockOfItem(LibraryItemModel model, LibraryItemsEntity entity) {
        ItemStockEntity stockEntity = entity.getItemStock();

        // If Quantity is increased
        if (model.getItemStock().getQuantity() >= stockEntity.getQuantity()) {
            int quantityIncrease = model.getItemStock().getQuantity() - stockEntity.getQuantity();
            stockEntity.setAvailableQuantity(stockEntity.getAvailableQuantity() + quantityIncrease);
            stockEntity.setQuantity(model.getItemStock().getQuantity());
            logger.info("Item Stock Increased");
        } else { // If Quantity is decreased
            int issuedItemsCount = stockEntity.getQuantity() - stockEntity.getAvailableQuantity();
            int newQuantity = model.getItemStock().getQuantity();

            if (newQuantity < issuedItemsCount) {
                // Handle the situation where new quantity is less than issued items
                logger.warn("Cannot decrease stock below the number of issued items.");
                throw new SpringLibraryException("NewQuantityIsLessThanIssued",
                        "Currently " + issuedItemsCount + "is issued, so new quantity can't be less then it", HttpStatus.BAD_REQUEST);
            }
            logger.error("hey I am inside ");
            int quantityDecrease = stockEntity.getQuantity() - newQuantity;
            stockEntity.setAvailableQuantity(stockEntity.getAvailableQuantity() - quantityDecrease);
            stockEntity.setQuantity(newQuantity);
            logger.info("Item Stock Decreased");
        }

        stockEntity.setLastStocked(LocalDateTime.now());
        entity.setItemStock(stockEntity);
    }

    public boolean compareItems(LibraryItemsEntity entity, LibraryItemModel model) {
        // Normalize strings by trimming and converting to lowercase
        String normalizedTitle = entity.getTitle().toLowerCase();
        String normalizedPublisher = entity.getPublisher().toLowerCase();
        String normalizedSubject = entity.getSubject().toLowerCase();
        String normalizedItemType = entity.getItemType();
        String normalizedLang = entity.getItemLanguage().toLowerCase();
        return normalizedTitle.equals(model.getTitle().toLowerCase()) &&
                normalizedPublisher.equals(model.getPublisher().toLowerCase()) &&
                normalizedSubject.equals(model.getSubject().toLowerCase()) &&
                normalizedItemType.equals(model.getItemType().name()) &&
                normalizedLang.equals(model.getItemLanguage().toLowerCase());
    }

    private String generateUUID() {
        long lastId = libraryItemsEntityRepo.getNextGeneratedId() + 1;
        String formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        return formattedDate.replace("-", "") + lastId;
    }
}
