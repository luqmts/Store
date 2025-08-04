package com.luq.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidMailException extends IllegalArgumentException{
    public InvalidMailException(String message) {
        super(message);
    }
}
