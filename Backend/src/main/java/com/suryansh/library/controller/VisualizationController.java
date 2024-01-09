package com.suryansh.library.controller;

import com.suryansh.library.dto.ChartsDataDto;
import com.suryansh.library.service.VisualiseService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/visualise/")
@CrossOrigin("*")
public class VisualizationController {
    private final VisualiseService visualiseService;

    public VisualizationController(VisualiseService visualiseService) {
        this.visualiseService = visualiseService;
    }

    @GetMapping("total-item-per-subject")
    public ChartsDataDto totalItemPerSubject(){
        return visualiseService.getTotalItemPerSubject();
    }

    @GetMapping("total-item-per-type")
    public ChartsDataDto totalItemPerType(){
        return visualiseService.getTotalItemPerType();
    }

    @GetMapping("total-item-per-class")
    public ChartsDataDto totalItemPerClass(){
        return visualiseService.getTotalItemPerClass();
    }

    @GetMapping("total-item-per-pub-year")
    public ChartsDataDto totalItemPerPubYear(){
        return visualiseService.getTotalItemPerPubYear();
    }

    @GetMapping("total-issuer-per-type")
    public ChartsDataDto totalIssuerPerType(){
        return visualiseService.getTotalIssuerPerType();
    }

    @GetMapping("total-issuer-per-class")
    public ChartsDataDto totalIssuerPerClass(){
        return visualiseService.getTotalIssuerPerClass();
    }

    @GetMapping("total-fine-per-date")
    public ChartsDataDto totalFinePerDate(){
        return visualiseService.getTotalFinePerDate();
    }

    @GetMapping("total-items-by-borrow-status")
    public ChartsDataDto totalItemByBorrowStatus(){
        return visualiseService.getTotalItemPerBorrowStatus();
    }

    @GetMapping("total-items-per-date")
    public ChartsDataDto totalItemBorrowPerDate(){
        return visualiseService.getTotalItemBorrowPerDate();
    }

    @GetMapping("general-info")
    public ChartsDataDto GeneralInfo(){
        return visualiseService.getGeneralInfo();
    }

    @GetMapping("total-item-returned-on-time")
    public ChartsDataDto TotalItemReturnedOnTime(){
        return visualiseService.getTotalItemReturnedOnTime();
    }

    @GetMapping("total-item-per-publication")
    public ChartsDataDto TotalItemPerPublication(){
        return visualiseService.getTotalItemPerPublication();
    }
}
