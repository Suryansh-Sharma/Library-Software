package com.suryansh.library.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SpringLibraryException extends RuntimeException {
    private final String type;
    private final HttpStatus httpStatus;

    public SpringLibraryException(String exMessage, String type, HttpStatus status) {
        super(exMessage);
        this.type = type;
        this.httpStatus = status;
    }

}
