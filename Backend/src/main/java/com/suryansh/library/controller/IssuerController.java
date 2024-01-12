package com.suryansh.library.controller;

import com.suryansh.library.dto.IssuersDto;
import com.suryansh.library.dto.PaginationPage;
import com.suryansh.library.dto.TotalFine;
import com.suryansh.library.model.IssuerModel;
import com.suryansh.library.service.IssuerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/issuer")
@CrossOrigin("*")
public class IssuerController {
    private final IssuerService issuerService;

    public IssuerController(IssuerService issuerService) {
        this.issuerService = issuerService;
    }

    @GetMapping("generate-total-fine/{issuer_unique_id}")
    public TotalFine generateTotalFine(@PathVariable String issuer_unique_id) {
        return issuerService.getTotalFine(issuer_unique_id);
    }

    @GetMapping("/search")
    public PaginationPage searchIssuer(@RequestParam String value,
                                       @RequestParam(required = false, defaultValue = "firstname") String target,
                                       @RequestParam(required = false, defaultValue = "6") int page_size,
                                       @RequestParam(required = false, defaultValue = "0") int page_no) {
        return issuerService.findIssuerFromDb(target, value, page_no, page_size);
    }

    @GetMapping("/full-detail-by/unique-id/{unique_id}")
    public IssuersDto getIssuerFullDetailByUniqueId(@PathVariable String unique_id) {
        return issuerService.getIssuerFromUniqueId(unique_id);
    }

    @GetMapping("/{unique_id}")
    public IssuersDto getIssuerDetail(@PathVariable String unique_id) {
        return issuerService.getIssuerByUniqueId(unique_id);
    }

    @Operation(summary = "Endpoint To Update Issuer Profile", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update-profile/{unique_id}")
    public void updateIssuerProfile(@Valid @RequestBody IssuerModel model, @PathVariable String unique_id) {
        issuerService.updateIssuerInDb(model, unique_id);
    }

    @Operation(summary = "Endpoint To Pay Fine of issuer and store pdf slip in database", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/pay-fine-upload-pdf/{unique_id}")
    public String PayFineStoreSlip(@PathVariable String unique_id, @RequestParam("pdf") MultipartFile file) throws IOException {
        return issuerService.storePdfAndPayFine(unique_id, file);
    }
}
