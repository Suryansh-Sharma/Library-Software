package com.suryansh.library.service;

import com.suryansh.library.dto.IssuersDto;
import com.suryansh.library.dto.PaginationPage;
import com.suryansh.library.dto.TotalFine;
import com.suryansh.library.model.IssuerModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IssuerService {
    String addNewIssuerInDb(IssuerModel model);

    PaginationPage findIssuerFromDb(String target, String value, int pageNo, int pageSize);

    IssuersDto getIssuerFromUniqueId(String uniqueId);

    void updateIssuerInDb(IssuerModel model, String uniqueId);

    IssuersDto getIssuerByUniqueId(String uniqueId);

    TotalFine getTotalFine(String issuerUniqueId);

    String storePdfAndPayFine(String uniqueId, MultipartFile file) throws IOException;
}
