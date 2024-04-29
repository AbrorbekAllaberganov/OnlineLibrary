package com.example.LibraryService.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorized extends RuntimeException{

    public UnAuthorized(String message) {
        super(message);
    }

    public UnAuthorized(String message, Throwable cause) {
        super(message, cause);
    }

}
