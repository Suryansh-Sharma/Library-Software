package com.suryansh.library.advice;

import com.suryansh.library.exception.SpringLibraryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class LibraryRestAdvice {

    @ExceptionHandler(SpringLibraryException.class)
    public ResponseEntity<ErrorResponse> handleSpringLibEx(SpringLibraryException ex) {
        var body = new ErrorResponse("Ops !!, Some thing went wrong ", new ErrorResponse.ErrorResponseMessage(
                ex.getMessage(),
                ex.getType(),
                ex.getHttpStatus()
        ));
        return new ResponseEntity<>(body, ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseMap> handleValidationEx(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(f ->
                errorMap.put(f.getField(), f.getDefaultMessage()));
        var body = new ErrorResponseMap(
                "Validation exception occurred",
                errorMap,
                "JavaValidationException",
                HttpStatus.BAD_REQUEST

        );
        return new ResponseEntity<>(body, e.getStatusCode());
    }

    public record ErrorResponse(String title, ErrorResponseMessage error) {
        public record ErrorResponseMessage(String message, String type, HttpStatus httpStatus) {
        }
    }

    public record ErrorResponseMap(String title, Map<String, String> error, String type, HttpStatus httpStatus) {
    }
}
