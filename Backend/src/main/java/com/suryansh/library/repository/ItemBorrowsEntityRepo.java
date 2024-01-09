package com.suryansh.library.repository;

import com.suryansh.library.dto.ChartQueryDto;
import com.suryansh.library.entity.ItemBorrowsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemBorrowsEntityRepo extends JpaRepository<ItemBorrowsEntity, Long> {

    @Query(value = "select * from item_borrows where return_status=false order by expected_return_date", nativeQuery = true)
    Page<ItemBorrowsEntity> getAllPendingItemAndSort(Pageable pageable);

    @Query(value = "select ib from ItemBorrowsEntity ib where ib.issuer.id=:id and ib.returnStatus=false")
    List<ItemBorrowsEntity> allPendingOfIssuer(long id);

    @Query(value = "select ib from ItemBorrowsEntity ib where ib.issuer.id = :id order by ib.borrowDate desc ")
    Page<ItemBorrowsEntity> getAllItemsOfIssuer(long id, Pageable pageable);

    @Query(value = "select ib from ItemBorrowsEntity ib where ib.libraryItem.id = :id order by ib.expectedReturnDate desc")
    Page<ItemBorrowsEntity> findAllForItem(long id, Pageable pageable);

    @Query(value = "select ib from ItemBorrowsEntity ib where ib.returnStatus=false and ib.issuer.uniqueId =:issuerId and ib.libraryItem.uniqueId =:itemId")
    Page<ItemBorrowsEntity> getByIssuerAndItemId(String issuerId, String itemId, Pageable pageable);

    // Visualise Query
    @Query("select new com.suryansh.library.dto.ChartQueryDto(count (*),cast(returnStatus as java.lang.String) ) from ItemBorrowsEntity group by returnStatus")
    List<ChartQueryDto> getTotalItemByReturnStatus();
    @Query("select new com.suryansh.library.dto.ChartQueryDto(count (*),cast(date (borrowDate) as java.lang.String) )" +
            " from ItemBorrowsEntity group by date(borrowDate)")
    List<ChartQueryDto> getTotalItemBorrowedOnDate();
    @Query("select new com.suryansh.library.dto.ChartQueryDto(count (*),cast(returnedOnTime as java.lang.String)) from ItemBorrowsEntity group by returnedOnTime")
    List<ChartQueryDto> getTotalItemReturnedOnTime();
    @Query("select count (*) from ItemBorrowsEntity ")
    long totalItemBorrowed();
}