package com.suryansh.library.service;

import com.suryansh.library.exception.SpringLibraryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    @Value("${fine-pdf-folder}")
    private String FINE_FOLDER_PATH;

    public void savePdfIntoFile(MultipartFile file, String fileName) {
        String newFilePath = FINE_FOLDER_PATH + fileName;
        File newFile = new File(newFilePath);
        try {
            file.transferTo(newFile);
            logger.info("File {} is saved successfully to folder {} ", fileName, FINE_FOLDER_PATH);
        } catch (Exception e) {
            logger.error("Unable to save file in file store " + e.getMessage());
            throw new SpringLibraryException("WrongFileType", "Sorry, Only Pdf is allowed",
                    HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getFileFineSlip(String filename) {
        try {
            Path filePath = Paths.get(FINE_FOLDER_PATH).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                logger.error("File not found: {}", filename);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("File not found: " + filename);
            }
        } catch (Exception e) {
            logger.error("Error downloading file: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error downloading file: " + filename);
        }
    }

}
