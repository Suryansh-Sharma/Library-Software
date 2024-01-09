package com.suryansh.library.service;

import com.suryansh.library.dto.IssuersDto;
import com.suryansh.library.dto.PaginationPage;
import com.suryansh.library.dto.TotalFine;
import com.suryansh.library.entity.FinePdfEntity;
import com.suryansh.library.entity.IssuerEntity;
import com.suryansh.library.entity.ItemBorrowsEntity;
import com.suryansh.library.entity.LibraryItemsEntity;
import com.suryansh.library.exception.SpringLibraryException;
import com.suryansh.library.mapper.MappingService;
import com.suryansh.library.model.IssuerModel;
import com.suryansh.library.repository.FinePdfEntityRepo;
import com.suryansh.library.repository.IssuerEntityRepo;
import com.suryansh.library.repository.ItemBorrowsEntityRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IssuerServiceImpl implements IssuerService {
    private final static Logger logger = LoggerFactory.getLogger(LibraryServiceImpl.class);
    private final MappingService mappingService;
    private final IssuerEntityRepo issuerEntityRepo;
    private final ItemBorrowsEntityRepo borrowsEntityRepo;
    private final FinePdfEntityRepo finePdfEntityRepo;
    private final FileService fileService;

    @Value("${per-day-amount}")
    private int PER_DAY_AMOUNT;

    public IssuerServiceImpl(MappingService mappingService,
                             IssuerEntityRepo issuerEntityRepo,
                             ItemBorrowsEntityRepo borrowsEntityRepo,
                             FinePdfEntityRepo userFineEntityRepo, FileService fileService) {
        this.mappingService = mappingService;
        this.issuerEntityRepo = issuerEntityRepo;
        this.borrowsEntityRepo = borrowsEntityRepo;
        this.finePdfEntityRepo = userFineEntityRepo;
        this.fileService = fileService;
    }

    @Override
    @Transactional
    public String addNewIssuerInDb(IssuerModel model) {
        Optional<IssuerEntity> check = issuerEntityRepo.checkWeatherIssuerIsPresent(model.getContactNo()
                , model.getFirstname().toUpperCase());
        if (check.isPresent()) {
            logger.error("User {} is already present ", model);
            throw new SpringLibraryException("Issuer of same name and contact is already present ", "DuplicateIssuer",
                    HttpStatus.CONFLICT);
        }
        IssuerEntity issuer = mappingService.mapIssuerModelToEntity(model);
        String unique_id = generateUUID();
        issuer.setUniqueId(unique_id);
        try {
            issuerEntityRepo.save(issuer);
            logger.info("Added new issuer {} of unique id : {} ", model, unique_id);
            return "New Issuer is successfully added of unique id : - " + unique_id;
        } catch (Exception e) {
            logger.error("Unable to save new issuer " + e);
            throw new SpringLibraryException(e.getMessage(), "UnableToSave", HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public PaginationPage findIssuerFromDb(String target, String value, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<IssuerEntity> page = issuerEntityRepo.searchIssuerQuery(target, value, pageable);

        return new PaginationPage(
                pageable.getPageNumber(),
                page.getTotalPages(),
                page
                        .get()
                        .map(mappingService::mapIssuerEntityToDto)
                        .toList(),
                page.getTotalElements(),
                pageable.getPageSize()
        );
    }

    @Override
    public IssuersDto getIssuerFromUniqueId(String uniqueId) {
        IssuerEntity issuer = issuerEntityRepo.findByUniqueId(uniqueId)
                .orElseThrow(() -> new SpringLibraryException("Unable to find issuer of unique_id " + uniqueId,
                        "IssuerNotFount", HttpStatus.NOT_FOUND));

        return mappingService.mapIssuerEntityToFullDto(issuer);
    }

    @Override
    public IssuersDto getIssuerByUniqueId(String uniqueId) {
        IssuerEntity issuer = issuerEntityRepo.findByUniqueId(uniqueId)
                .orElseThrow(() -> new SpringLibraryException("Unable to find issuer of unique_id " + uniqueId,
                        "IssuerNotFount", HttpStatus.NOT_FOUND));
        return mappingService.mapIssuerEntityToDto(issuer);
    }

    @Override
    @Transactional
    public TotalFine getTotalFine(String uniqueId) {
        IssuerEntity issuer = issuerEntityRepo.findByUniqueId(uniqueId)
                .orElseThrow(() -> new SpringLibraryException("Unable to find issuer of unique_id " + uniqueId,
                        "IssuerNotFount", HttpStatus.NOT_FOUND));
        List<ItemBorrowsEntity> borrowsList = borrowsEntityRepo.allPendingOfIssuer(issuer.getId());
        if (borrowsList.isEmpty()) {
            throw new SpringLibraryException("No pending are found of issuer",
                    "NoPendingFound",
                    HttpStatus.NOT_FOUND);
        }
        LocalDateTime todayDateTime = LocalDateTime.now();
        List<TotalFine.FineItem> totalFineItems = new ArrayList<>();
        double totalAmount = 0;
        for (ItemBorrowsEntity itemBorrows : borrowsList) {
            if (itemBorrows.getExpectedReturnDate().isBefore(todayDateTime.toLocalDate())) {
                LibraryItemsEntity item = itemBorrows.getLibraryItem();
                long pendingDays = ChronoUnit.DAYS.between(itemBorrows.getExpectedReturnDate(), todayDateTime.toLocalDate());
                TotalFine.FineItem fineItem = new TotalFine.FineItem(
                        item.getUniqueId(),
                        item.getTitle(),
                        issuer.getUniqueId(),
                        issuer.getFirstname() + ' ' + issuer.getLastname(),
                        itemBorrows.getId(),
                        itemBorrows.getBorrowDate(),
                        itemBorrows.getExpectedReturnDate(),
                        pendingDays,
                        PER_DAY_AMOUNT,
                        PER_DAY_AMOUNT * itemBorrows.getQuantity() * pendingDays
                );
                totalAmount += fineItem.total_amount();
                totalFineItems.add(fineItem);
            }

        }
        if (totalFineItems.isEmpty()) {
            throw new SpringLibraryException("No pending are found of issuer",
                    "NoPendingFound",
                    HttpStatus.NOT_FOUND);
        }
        TotalFine.IssuerInfo issuerInfo = new TotalFine.IssuerInfo(
                issuer.getFirstname() + ' ' + issuer.getLastname(),
                issuer.getIssuerType(),
                issuer.getIssuerClass(),
                issuer.getContactNo()
        );
        return new TotalFine(
                totalFineItems.size(),
                totalAmount,
                PER_DAY_AMOUNT,
                todayDateTime,
                totalFineItems,
                issuerInfo
        );
    }

    @Override
    public String storePdfAndPayFine(String uniqueId, MultipartFile file) {
        String fileName = file.getOriginalFilename();

        if (fileName != null && !fileName.toLowerCase().endsWith(".pdf")) {
            throw new SpringLibraryException("Sorry, Only Pdf is allowed",
                    "WrongFileType",
                    HttpStatus.BAD_REQUEST);
        }

        IssuerEntity issuer = issuerEntityRepo.findByUniqueId(uniqueId)
                .orElseThrow(() -> new SpringLibraryException("Unable to find issuer of unique_id " + uniqueId,
                        "IssuerNotFount", HttpStatus.NOT_FOUND));

        LocalDateTime todayDateTime = LocalDateTime.now();
        List<ItemBorrowsEntity> borrowsList = borrowsEntityRepo.allPendingOfIssuer(issuer.getId());
        List<ItemBorrowsEntity> finedBorrows = new ArrayList<>();
        double totalAmount = 0;

        FinePdfEntity fineEntity = FinePdfEntity.builder()
                .fileName(fileName)
                .dateTime(todayDateTime)
                .issuer(issuer)
                .build();
        for (ItemBorrowsEntity e : borrowsList) {
            if (e.getExpectedReturnDate().isBefore(todayDateTime.toLocalDate())) {
                long pendingDays = ChronoUnit.DAYS.between(e.getExpectedReturnDate(), todayDateTime.toLocalDate());
                totalAmount += PER_DAY_AMOUNT * e.getQuantity() * pendingDays;
                e.setReturnStatus(true);
                e.setActualReturnDate(todayDateTime);
                e.setItemCondition("EXCELLENT");
                e.setReturnedOnTime(false);
                e.setFinePdf(fineEntity);
                finedBorrows.add(e);
            }
        }
        if (finedBorrows.isEmpty()) {
            throw new SpringLibraryException(
                    "No pending are found of issuer",
                    "NoPendingFound",
                    HttpStatus.NOT_FOUND);
        }
        fineEntity.setItemBorrows(finedBorrows);
        fineEntity.setAmount(totalAmount);
        fineEntity.setTotalItems(finedBorrows.size());

        try {
            finePdfEntityRepo.save(fineEntity);
            fileService.savePdfIntoFile(file, fileName);
            logger.info("User {} Fine payed and pdf saved successfully ", issuer.getUniqueId());
            return "Fine payed and slip saved successfully";
        } catch (Exception e) {
            logger.error("Error paying and saving PDF for user " + issuer.getUniqueId(), e);
            throw new SpringLibraryException("SomethingWentWrong", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void updateIssuerInDb(IssuerModel model, String uniqueId) {
        IssuerEntity issuer = issuerEntityRepo.findByUniqueId(uniqueId)
                .orElseThrow(() -> new SpringLibraryException("Unable to find issuer of unique_id " + uniqueId,
                        "IssuerNotFount", HttpStatus.NOT_FOUND));
        issuer.setFirstname(model.getFirstname().toUpperCase());
        issuer.setLastname(model.getLastname().toUpperCase());
        issuer.setRollNo(model.getRollNo());
        issuer.setIssuerType(model.getIssuerType().name());
        issuer.setContactNo(model.getContactNo());
        issuer.setEmail(model.getEmail());
        issuer.setIssuerClass(model.getIssuerClass());
        issuer.setIssuerBranch(model.getIssuerBranch().name());
        try {
            issuerEntityRepo.save(issuer);
            logger.info("Successfully updated issuer {} ", uniqueId);
        } catch (Exception e) {
            logger.error("Unable to update issuer " + e);
            throw new SpringLibraryException("Unable to update issuer of unique_id " + uniqueId,
                    "IssuerNotFount", HttpStatus.NOT_FOUND);
        }
    }

    private String generateUUID() {
        long lastId = issuerEntityRepo.getNextGeneratedId() + 1;
        String formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        return formattedDate.replace("-", "") + lastId;
    }

}
