package com.suryansh.library.repository;

import com.suryansh.library.dto.ChartQueryDto;
import com.suryansh.library.entity.BooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BooksEntityRepo extends JpaRepository<BooksEntity, Integer> {
    @Query("SELECT new com.suryansh.library.dto.ChartQueryDto(COUNT(*), bookClass) FROM BooksEntity GROUP BY bookClass")
    List<ChartQueryDto> getTotalItemByClass();
    @Query("select new com.suryansh.library.dto.ChartQueryDto(count(*),cast(publicationYear as java.lang.String)) from BooksEntity GROUP BY publicationYear")
    List<ChartQueryDto> getTotalItemByPubYear();

}