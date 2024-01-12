package com.suryansh.library.controller;

import com.suryansh.library.dto.BorrowItemDto;
import com.suryansh.library.dto.ItemFullDetailDto;
import com.suryansh.library.dto.LibraryItemPagingDto;
import com.suryansh.library.dto.PaginationPage;
import com.suryansh.library.model.BorrowItemModel;
import com.suryansh.library.model.LibraryItemModel;
import com.suryansh.library.model.ReturnItemModel;
import com.suryansh.library.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/library")
@CrossOrigin("*")
public class LibraryController {
    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/all")
    public LibraryItemPagingDto getAll(@RequestParam(required = false, defaultValue = "6") int page_size
            , @RequestParam(required = false, defaultValue = "0") int page_no) {
        return libraryService.getAll(page_size, page_no);
    }

    @Operation(summary = "Endpoint To Add New Item In Library", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add-new-item")
    public String addNewItem(@RequestBody @Valid LibraryItemModel model) {
        return libraryService.saveNewItem(model);
    }

    @Operation(summary = "Endpoint Borrow New Item", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/borrow-item")
    public String issueItemToUser(@RequestBody @Valid BorrowItemModel model) {
        return libraryService.borrowNewItem(model);
    }

    @GetMapping("/all-borrows-of-user/{user_unique_id}")
    public PaginationPage getAllBorrowsOfUser(@PathVariable String user_unique_id,
                                              @RequestParam(required = false, defaultValue = "6") int page_size,
                                              @RequestParam(required = false, defaultValue = "0") int page_no) {
        return libraryService.getAllBorrowsOfIssuer(user_unique_id, page_no, page_size);
    }

    @GetMapping("/all-pending-borrows-of-user/{user_unique_id}")
    public List<BorrowItemDto> getAllPendingBorrowsOfIssuer(@PathVariable String user_unique_id) {
        return libraryService.findAllPendingBorrowsOfUser(user_unique_id);
    }

    @Operation(summary = "Endpoint To Extend Item Date Of Return", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("extend-return-date/{borrow_id}/{new_date}")
    public String extendsDateOfReturn(@PathVariable long borrow_id, @PathVariable String new_date) {
        return libraryService.increaseReturnDate(borrow_id, new_date);
    }

    @Operation(summary = "Endpoint To Return Item on time", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/return-item")
    public String returnItemOnTime(@RequestBody @Valid ReturnItemModel model) {
        return libraryService.returnItemOnTime(model);
    }

    @GetMapping("/specific-items-of-user/{issuer_uid}/{item_uid}")
    public PaginationPage specificItemsOfUser(@PathVariable String issuer_uid, @PathVariable String item_uid,
                                              @RequestParam(required = false, defaultValue = "6") int page_size,
                                              @RequestParam(required = false, defaultValue = "0") int page_no) {
        return libraryService.specificItemsByUser(issuer_uid, item_uid, page_size, page_no);
    }

    @GetMapping("/all-pending-return-item")
    public PaginationPage getAllPendingItems(@RequestParam(required = false, defaultValue = "6") int page_size
            , @RequestParam(required = false, defaultValue = "0") int page_no) {
        return libraryService.getAllPendingItemsFromDb(page_size, page_no);
    }

    @GetMapping("/item-full-text-search/{value}")
    public LibraryItemPagingDto fullTextSearchItem(@PathVariable String value,
                                                   @RequestParam(required = false, defaultValue = "false") Boolean is_book,
                                                   @RequestParam(required = false, defaultValue = "6") int page_size,
                                                   @RequestParam(required = false, defaultValue = "0") int page_no) {
        return libraryService.ItemFullTextSearch(value, page_no, page_size, is_book);
    }

    @GetMapping("/item-full-details/{unique_id}")
    public ItemFullDetailDto getItemFullDetails(@PathVariable String unique_id) {
        return libraryService.getItemFullDetail(unique_id);
    }

    @GetMapping("/all-borrows-of-item/{unique_id}")
    public PaginationPage getAllBorrowsOfItem(@PathVariable String unique_id,
                                              @RequestParam(required = false, defaultValue = "6") int page_size,
                                              @RequestParam(required = false, defaultValue = "0") int page_no) {
        return libraryService.getAllBorrowsOfItem(unique_id, page_no, page_size);
    }

    @GetMapping("/all-pending-issuer-of-item/{unique_id}")
    public PaginationPage getAllPendingIssuerOfItem(@PathVariable String unique_id,
                                                    @RequestParam(required = false, defaultValue = "6") int page_size,
                                                    @RequestParam(required = false, defaultValue = "0") int page_no) {
        return libraryService.getAllPendingIssuerOfItemFromDb(unique_id, page_no, page_size);
    }

    @Operation(summary = "Endpoint To Update Item Info And Stock", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update-item-detail-and-stock/{unique_id}")
    public String updateItem(@Valid @RequestBody LibraryItemModel model, @PathVariable String unique_id) {
        return libraryService.updateItem(model, unique_id);
    }
    @GetMapping("/search-publisher/{name}")
    public Set<String> searchPublisher(@PathVariable String name){
        return libraryService.searchPublisher(name);
    }

    @GetMapping("/all-paid-fines")
    public PaginationPage getAllPaidFines(@RequestParam(defaultValue = "0")int page_no,
                                          @RequestParam(defaultValue = "6")int page_size){
        return libraryService.getAllPaidFines(page_no,page_size);
    }
}
