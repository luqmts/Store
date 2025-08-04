package com.luq.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidCnpjException extends IllegalArgumentException{
    public InvalidCnpjException(String message) {
        super(message);
    }
}
