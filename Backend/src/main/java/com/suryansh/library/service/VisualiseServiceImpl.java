package com.suryansh.library.service;

import com.suryansh.library.dto.ChartQueryDto;
import com.suryansh.library.dto.ChartsDataDto;
import com.suryansh.library.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisualiseServiceImpl implements VisualiseService{
    private final LibraryItemsEntityRepo libraryItemRepo;
    private final BooksEntityRepo bookRepo;
    private final FinePdfEntityRepo finePdfRepo;
    private final IssuerEntityRepo issuerRepo;
    private final ItemBorrowsEntityRepo borrowRepo;

    public VisualiseServiceImpl(LibraryItemsEntityRepo libraryItemRepo, BooksEntityRepo bookRepo, FinePdfEntityRepo finePdfRepo, IssuerEntityRepo issuerRep, ItemBorrowsEntityRepo borrowRepo) {
        this.libraryItemRepo = libraryItemRepo;
        this.bookRepo = bookRepo;
        this.finePdfRepo = finePdfRepo;
        this.issuerRepo = issuerRep;
        this.borrowRepo = borrowRepo;
    }

    @Override
    public ChartsDataDto getTotalItemPerSubject() {
        List<ChartQueryDto> chartQueryData = libraryItemRepo.getTotalItemBySubject();
        return getChartsDataDto(chartQueryData,"Subject", "Query to get total items per subject");

    }

    @Override
    public ChartsDataDto getTotalItemPerType() {
        List<ChartQueryDto> chartQueryData = libraryItemRepo.getTotalItemByItemType();
        return getChartsDataDto(chartQueryData,"Item Type", "Query to get total items per item type");
    }

    @Override
    public ChartsDataDto getTotalItemPerClass() {
        List<ChartQueryDto> chartQueryData = bookRepo.getTotalItemByClass();
        return getChartsDataDto(chartQueryData,"Class", "Query to get total items per class");
    }

    @Override
    public ChartsDataDto getTotalItemPerPubYear() {
        List<ChartQueryDto> chartQueryData = bookRepo.getTotalItemByPubYear();
        return getChartsDataDto(chartQueryData,"Published Year", "Query to get total items per published year");
    }

    @Override
    public ChartsDataDto getTotalIssuerPerType() {
        List<ChartQueryDto> chartQueryData = issuerRepo.getTotalIssuerByType();
        return getChartsDataDto(chartQueryData,"Issuer Type", "Query to get issuer by type");
    }

    @Override
    public ChartsDataDto getTotalIssuerPerClass() {
        List<ChartQueryDto> chartQueryData = issuerRepo.getTotalIssuerByClass();
        return getChartsDataDto(chartQueryData,"Issuer Class", "Query to get issuer by class");
    }

    @Override
    public ChartsDataDto getTotalFinePerDate() {
        List<ChartQueryDto> chartQueryData = finePdfRepo.getTotalFineByDate();
        return getChartsDataDto(chartQueryData,"Fine Date", "Query to get fine by date");
    }

    @Override
    public ChartsDataDto getTotalItemPerBorrowStatus() {
        List<ChartQueryDto> chartQueryData = borrowRepo.getTotalItemByReturnStatus();
        return getChartsDataDto(chartQueryData,"Borrow Status", "Query to get item by return status");
    }

    @Override
    public ChartsDataDto getTotalItemBorrowPerDate() {
        List<ChartQueryDto> chartQueryData = borrowRepo.getTotalItemBorrowedOnDate();
        return getChartsDataDto(chartQueryData,"Date", "Query to get item borrow by date");
    }

    @Override
    public ChartsDataDto getGeneralInfo() {
        List<Integer>series = new ArrayList<>();
        List<String>label = new ArrayList<>();

        Integer totalItem = Math.toIntExact(libraryItemRepo.countTotalItem());
        series.add(totalItem);
        label.add("Total Items");

        Integer totalIssuer = Math.toIntExact(issuerRepo.getTotalIssuers());
        series.add(totalIssuer);
        label.add("Total Issuers");

        Integer totalItemBorrowed = Math.toIntExact(borrowRepo.totalItemBorrowed());
        series.add(totalItemBorrowed);
        label.add("Total Item Borrowed");

        Integer totalFineCollected = (int) finePdfRepo.totalFineCollected();
        series.add(totalFineCollected);
        label.add("Total Fine Collected");



        return new ChartsDataDto(
                "General Label",
                "General Series",
                series,
                label,
                "Query to get general info"
        );
    }

    @Override
    public ChartsDataDto getTotalItemReturnedOnTime() {
        List<ChartQueryDto> chartQueryData = borrowRepo.getTotalItemReturnedOnTime();
        return getChartsDataDto(chartQueryData,"Date", "Query to get item returned on time");
    }

    @Override
    public ChartsDataDto getTotalItemPerPublication() {
        List<ChartQueryDto>chartQueryData = libraryItemRepo.getTotalItemByPublication();
        return getChartsDataDto(chartQueryData,"Publication","Query to get item by publication");
    }

    private ChartsDataDto getChartsDataDto(List<ChartQueryDto> chartQueryData, String label, String Query) {
        List<Integer> seriesData = chartQueryData.stream()
                .map(ChartQueryDto::getTotal_records) // Extract total records
                .map(Long::intValue)
                .toList();

        List<String> labels = chartQueryData.stream()
                .map(ChartQueryDto::getTarget)
                .toList();

        return  new ChartsDataDto(
                label,
                "Total Items",
                seriesData,
                labels,
                Query // Optional query string
        );
    }
}
