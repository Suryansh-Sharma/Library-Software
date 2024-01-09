package com.suryansh.library.controller;

import com.suryansh.library.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/download-pay-slip/{filename}")
    public ResponseEntity<?> downloadFinePdfSlip(@PathVariable String filename) {
        return fileService.getFileFineSlip(filename);
    }

    @PostMapping("/upload-pdf")
    public void uploadPdf(@RequestParam("pdf") MultipartFile file) {
        fileService.savePdfIntoFile(file, file.getOriginalFilename());
    }
}
