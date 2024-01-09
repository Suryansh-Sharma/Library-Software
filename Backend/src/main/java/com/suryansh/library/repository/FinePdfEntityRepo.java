package com.suryansh.library.repository;

import com.suryansh.library.dto.ChartQueryDto;
import com.suryansh.library.entity.FinePdfEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinePdfEntityRepo extends JpaRepository<FinePdfEntity, Long> {
//    Visualise Query
    @Query("select new com.suryansh.library.dto.ChartQueryDto(count (*),cast(date (dateTime) as java.lang.String))" +
            " from FinePdfEntity  group by date(dateTime)")
    List<ChartQueryDto> getTotalFineByDate();

    @Query("select sum (amount) from FinePdfEntity")
    double totalFineCollected();

}