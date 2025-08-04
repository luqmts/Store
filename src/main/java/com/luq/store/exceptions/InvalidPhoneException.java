package com.luq.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidPhoneException extends IllegalArgumentException{
    public InvalidPhoneException(String message) {
        super(message);
    }
}
