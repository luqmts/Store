package com.luq.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductRegisteredException extends IllegalArgumentException{
    public ProductRegisteredException(String message) {
        super(message);
    }
}
