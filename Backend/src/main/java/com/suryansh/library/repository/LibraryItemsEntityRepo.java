package com.suryansh.library.repository;

import com.suryansh.library.dto.ChartQueryDto;
import com.suryansh.library.entity.LibraryItemsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LibraryItemsEntityRepo extends JpaRepository<LibraryItemsEntity, Long> {
    Optional<LibraryItemsEntity> findByTitle(String title);

    @Query(value = "SELECT max(id) from library_items", nativeQuery = true)
    long getNextGeneratedId();

    Optional<LibraryItemsEntity> findByUniqueId(String itemUniqueId);

    @Query("select it from LibraryItemsEntity it where " +
            "it.uniqueId = :value or " +
            "it.title ilike concat('%',:value,'%') or " +
            "it.description ilike concat('%',:value,'%') or " +
            "it.subject ilike concat('%',:value,'%') or " +
            "it.publisher ilike concat('%',:value,'%') or " +
            "it.itemLanguage ilike concat('%',:value,'%')")
    Page<LibraryItemsEntity> searchItemFullText(Pageable pageable, String value);

    @Query("select it from LibraryItemsEntity it where " +
            "it.title ilike concat('%',:value,'%') or " +
            "it.subject ilike concat('%',:value,'%') or " +
            "it.books.bookClass ilike concat('%',:value,'%') or " +
            "it.books.author ilike concat('%',:value,'%') or " +
            "it.publisher ilike concat('%',:value,'%')"
    )
    Page<LibraryItemsEntity> searchBookItemFullText(Pageable pageable, String value);

    // Visualization Query
    @Query("SELECT new com.suryansh.library.dto.ChartQueryDto(COUNT(*), subject) FROM LibraryItemsEntity GROUP BY subject")
    List<ChartQueryDto> getTotalItemBySubject();

    @Query("SELECT new com.suryansh.library.dto.ChartQueryDto(COUNT(*), itemType) FROM LibraryItemsEntity GROUP BY itemType")
    List<ChartQueryDto> getTotalItemByItemType();

    @Query(value = "select count(*) as total_record from library_items",nativeQuery = true)
    long countTotalItem();
    @Query("select new com.suryansh.library.dto.ChartQueryDto(count (*), publisher) from LibraryItemsEntity GROUP BY publisher")
    List<ChartQueryDto> getTotalItemByPublication();
    @Query(value = "SELECT it.publisher FROM library_items it WHERE it.publisher ILIKE CONCAT('%', ?1, '%') LIMIT 5", nativeQuery = true)
    Set<String> searchPublisher(String name);

}