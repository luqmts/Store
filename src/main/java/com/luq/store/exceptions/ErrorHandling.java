package com.luq.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandling {
    Map<String, String> errors = new HashMap<>();

    @ExceptionHandler(InvalidCnpjException.class)
    public ResponseEntity<Map<String, String>> exceptionFound(InvalidCnpjException e) {
        errors.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(InvalidMailException.class)
    public ResponseEntity<Map<String, String>> exceptionFound(InvalidMailException e) {
        errors.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(InvalidPhoneException.class)
    public ResponseEntity<Map<String, String>> exceptionFound(InvalidPhoneException e) {
        errors.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(InvalidProductPriceException.class)
    public ResponseEntity<Map<String, String>> exceptionFound(InvalidProductPriceException e) {
        errors.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<Map<String, String>> exceptionFound(InvalidQuantityException e) {
        errors.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> exceptionFound(NotFoundException e) {
        errors.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(ProductRegisteredException.class)
    public ResponseEntity<Map<String, String>> exceptionFound(ProductRegisteredException e) {
        errors.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
