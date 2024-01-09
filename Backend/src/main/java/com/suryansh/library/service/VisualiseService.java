package com.suryansh.library.service;

import com.suryansh.library.dto.ChartsDataDto;

public interface VisualiseService {
    ChartsDataDto getTotalItemPerSubject();

    ChartsDataDto getTotalItemPerType();

    ChartsDataDto getTotalItemPerClass();

    ChartsDataDto getTotalItemPerPubYear();

    ChartsDataDto getTotalIssuerPerType();

    ChartsDataDto getTotalIssuerPerClass();

    ChartsDataDto getTotalFinePerDate();

    ChartsDataDto getTotalItemPerBorrowStatus();

    ChartsDataDto getTotalItemBorrowPerDate();

    ChartsDataDto getGeneralInfo();

    ChartsDataDto getTotalItemReturnedOnTime();

    ChartsDataDto getTotalItemPerPublication();
}
