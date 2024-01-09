package com.suryansh.library.mapper;

import com.suryansh.library.dto.*;
import com.suryansh.library.entity.*;
import com.suryansh.library.model.IssuerModel;
import com.suryansh.library.model.LibraryItemModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MappingService {
    @Value("${base-url}")
    private String BASE_URL;

    public IssuersDto mapIssuerEntityToDto(IssuerEntity e) {
        return new IssuersDto(
                e.getId(),
                e.getFirstname(),
                e.getLastname(),
                e.getRollNo(),
                e.getIssuerType(),
                e.getContactNo(),
                e.getEmail(),
                e.getIssuerClass(),
                e.getUniqueId(),
                e.getIssuerBranch(),
                null
        );
    }

    public IssuersDto mapIssuerEntityToFullDto(IssuerEntity e) {
        return new IssuersDto(
                e.getId(),
                e.getFirstname(),
                e.getLastname(),
                e.getRollNo(),
                e.getIssuerType(),
                e.getContactNo(),
                e.getEmail(),
                e.getIssuerClass(),
                e.getUniqueId(),
                e.getIssuerBranch(),
                e.getUserFinesPdf()
                        .stream()
                        .map(this::fineEntityToDto)
                        .toList()
        );
    }

    public LibraryItemsEntity libraryItemModelToEntity(LibraryItemModel model) {
        LibraryItemsEntity libraryItemsEntity = LibraryItemsEntity.builder()
                .title(model.getTitle().toUpperCase())
                .publisher(model.getPublisher().toUpperCase())
                .subject(model.getSubject().toUpperCase())
                .itemType(model.getItemType().name())
                .description(model.getDescription())
                .itemLocation(model.getItemLocation().toUpperCase())
                .itemLanguage(model.getItemLanguage())
                .build();
        if (model.getItemType().name().equals("BOOK")) {
            libraryItemsEntity.setBooks(mapBookModelToEntity(model.getBookModel()));
        } else if (model.getItemType().name().equals("MAGAZINE")) {
            libraryItemsEntity.setMagazines(mapMagazineModelToEntity(model.getMagazineModel()));
        }
        libraryItemsEntity.setItemStock(mapItemStockModelToEntity(model.getItemStock()));
        return libraryItemsEntity;
    }

    public BooksEntity mapBookModelToEntity(@Valid LibraryItemModel.BookModel model) {
        return BooksEntity.builder()
                .author(model.getAuthor().toUpperCase())
                .publicationYear(model.getPublicationYear())
                .bookType(model.getBookType())
                .bookClass(model.getBookClass())
                .build();
    }

    public MagazineEntity mapMagazineModelToEntity(@Valid LibraryItemModel.MagazineModel model) {
        return MagazineEntity.builder()
                .publishedDate(model.getPublishedDate())
                .editor(model.getEditor())
                .build();
    }

    public ItemStockEntity mapItemStockModelToEntity(LibraryItemModel.ItemStock stock) {
        return ItemStockEntity.builder()
                .quantity(stock.getQuantity())
                .availableQuantity(stock.getQuantity())
                .lastStocked(LocalDateTime.now())
                .build();
    }

    public LibraryItemPagingDto.Items libraryEntityToDto(LibraryItemsEntity entity) {
        return new LibraryItemPagingDto.Items(
                entity.getId(),
                entity.getTitle(),
                entity.getPublisher(),
                entity.getItemType(),
                entity.getDescription(),
                entity.getItemLocation(),
                entity.getItemLanguage(),
                entity.getUniqueId()
        );
    }

    public IssuerEntity mapIssuerModelToEntity(IssuerModel model) {
        return IssuerEntity.builder()
                .firstname(model.getFirstname().toUpperCase())
                .lastname(model.getLastname())
                .rollNo(model.getRollNo())
                .issuerType(model.getIssuerType().name())
                .contactNo(model.getContactNo())
                .email(model.getEmail())
                .issuerClass(model.getIssuerClass())
                .issuerBranch(model.getIssuerBranch().name())
                .build();
    }

    public BorrowItemDto borrowEntityToDto(ItemBorrowsEntity e) {
        LibraryItemsEntity item = e.getLibraryItem();
        LocalDateTime todayDayTime = LocalDateTime.now();
        return new BorrowItemDto(
                e.getId(),
                e.getQuantity(),
                item.getTitle(),
                item.getUniqueId(),
                e.getIssuer().getUniqueId(),
                e.getBorrowDate(),
                e.getExpectedReturnDate(),
                e.getActualReturnDate(),
                e.isReturnStatus(),
                todayDayTime.toLocalDate().isAfter(e.getExpectedReturnDate()),
                e.getItemCondition()
        );
    }

    public IssuersDto.Fine fineEntityToDto(FinePdfEntity finePdf) {
        return new IssuersDto.Fine(
                finePdf.getId(),
                finePdf.getDateTime(),
                finePdf.getTotalItems(),
                finePdf.getFileName(),
                BASE_URL + "api/v1/file/download-pay-slip/" + finePdf.getFileName(),
                finePdf.getAmount(),
                null
        );
    }

    public ItemFullDetailDto mapItemEntityToFullDto(LibraryItemsEntity item) {
        ItemFullDetailDto res = new ItemFullDetailDto();
        ItemFullDetailDto.BookDto book = new ItemFullDetailDto.BookDto();
        ItemFullDetailDto.MagazineDto magazine = new ItemFullDetailDto.MagazineDto();
        ItemFullDetailDto.StockDto stock = new ItemFullDetailDto.StockDto();
        if (item.getItemType().equals("BOOK")) {
            BooksEntity be = item.getBooks();
            book.setId(be.getId());
            book.setAuthor(be.getAuthor());
            book.setBookType(book.getBookType());
            book.setBookClass(be.getBookClass());
            res.setBook(book);
        } else if (item.getItemType().equals("MAGAZINE")) {
            MagazineEntity me = item.getMagazines();
            magazine.setId(magazine.getId());
            magazine.setPublishedDate(me.getPublishedDate());
            magazine.setEditor(me.getEditor());
            res.setMagazine(magazine);
        }
        ItemStockEntity se = item.getItemStock();
        stock.setId(se.getId());
        stock.setQuantity(se.getQuantity());
        stock.setAvailableQuantity(se.getAvailableQuantity());
        stock.setLastStocked(se.getLastStocked());
        stock.setLastCheckedIn(se.getLastCheckedIn());
        stock.setLastCheckedOut(se.getLastCheckedOut());
        res.setStock(stock);

        res.setId(item.getId());
        res.setTitle(item.getTitle());
        res.setSubject(item.getSubject());
        res.setPublisher(item.getPublisher());
        res.setItemType(item.getItemType());
        res.setDescription(item.getDescription());
        res.setItemLocation(item.getItemLocation());
        res.setItemLanguage(item.getItemLanguage());
        res.setUniqueId(item.getUniqueId());

        return res;
    }

    public FineInfoDto fineInfoEntityToDto(FinePdfEntity fine){
        IssuerEntity issuer = fine.getIssuer();
        return new FineInfoDto(
                fine.getId(),
                fine.getDateTime(),
                fine.getAmount(),
                BASE_URL + "api/v1/file/download-pay-slip/" + fine.getFileName(),
                fine.getTotalItems(),
                new FineInfoDto.IssuerInfo(
                        issuer.getId(),
                        issuer.getFirstname(),
                        issuer.getLastname(),
                        issuer.getRollNo(),
                        issuer.getIssuerType(),
                        issuer.getContactNo(),
                        issuer.getEmail(),
                        issuer.getIssuerClass(),
                        issuer.getUniqueId(),
                        issuer.getIssuerBranch()
                )
        );
    }
}
