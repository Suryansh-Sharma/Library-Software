package com.suryansh.library.repository;

import com.suryansh.library.dto.ChartQueryDto;
import com.suryansh.library.entity.IssuerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssuerEntityRepo extends JpaRepository<IssuerEntity, Long> {
    @Query(value = "SELECT max(id) issuers from issuers", nativeQuery = true)
    long getNextGeneratedId();

    @Query("SELECT ir FROM IssuerEntity ir WHERE ir.contactNo = :contactNo AND ir.firstname = :firstname")
    Optional<IssuerEntity> checkWeatherIssuerIsPresent(@Param("contactNo") String contactNo, @Param("firstname") String firstname);

    Optional<IssuerEntity> findByUniqueId(String issuerUniqueId);

    @Query("select ir from IssuerEntity  ir where" +
            "(:target = 'firstname' AND ir.firstname like upper(concat('%', :value, '%'))) or" +
            "(:target = 'contact_no' and ir.contactNo like :value ) or" +
            "(:target = 'email' and ir.email like concat('%',:value,'%'))or" +
            "(:target ='unique_id') and ir.uniqueId = :value")
    Page<IssuerEntity> searchIssuerQuery(String target, String value, Pageable pageable);

    @Query("select ir from IssuerEntity ir " +
            "join ir.itemBorrows ib " +
            "join ib.libraryItem li " +
            "where li.uniqueId = :uniqueId " +
            "and ib.returnStatus =" +
            "false ")
    Page<IssuerEntity> findAllPendingIssuerOfItemDb(String uniqueId, Pageable pageable);

//    Visualise Query
    @Query("select new com.suryansh.library.dto.ChartQueryDto(count (*),issuerType) from IssuerEntity group by issuerType")
    List<ChartQueryDto> getTotalIssuerByType();
    @Query("select new com.suryansh.library.dto.ChartQueryDto(count (*),issuerClass) from IssuerEntity group by issuerClass")
    List<ChartQueryDto> getTotalIssuerByClass();

    @Query("select count(*) from IssuerEntity")
    long getTotalIssuers();
}