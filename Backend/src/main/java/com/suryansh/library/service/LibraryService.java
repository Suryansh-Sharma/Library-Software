package com.suryansh.library.service;

import com.suryansh.library.dto.BorrowItemDto;
import com.suryansh.library.dto.ItemFullDetailDto;
import com.suryansh.library.dto.LibraryItemPagingDto;
import com.suryansh.library.dto.PaginationPage;
import com.suryansh.library.model.BorrowItemModel;
import com.suryansh.library.model.LibraryItemModel;
import com.suryansh.library.model.ReturnItemModel;

import java.util.List;
import java.util.Set;

public interface LibraryService {
    LibraryItemPagingDto getAll(int page_size, int page_no);

    String saveNewItem(LibraryItemModel model);

    String borrowNewItem(BorrowItemModel model);

    String returnItemOnTime(ReturnItemModel model);

    PaginationPage specificItemsByUser(String issuerUid, String itemUid, int page_size, int page_no);

    PaginationPage getAllBorrowsOfIssuer(String userUniqueId, int pageNo, int pageSize);


    PaginationPage getAllPendingItemsFromDb(int pageSize, int pageNo);

    LibraryItemPagingDto ItemFullTextSearch(String value, int pageNo, int pageSize, Boolean is_book);

    List<BorrowItemDto> findAllPendingBorrowsOfUser(String userUniqueId);

    ItemFullDetailDto getItemFullDetail(String uniqueId);

    PaginationPage getAllBorrowsOfItem(String uniqueId, int page_no, int page_size);

    String updateItem(LibraryItemModel model, String uniqueId);

    PaginationPage getAllPendingIssuerOfItemFromDb(String uniqueId, int pageNo, int pageSize);


    String increaseReturnDate(long borrowId, String newDate);

    Set<String> searchPublisher(String name);

    PaginationPage getAllPaidFines(int pageNo, int pageSize);
}
