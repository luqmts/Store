package com.luq.store.exceptions;

import java.util.List;

public class MultipleValidationException extends RuntimeException {
    private final List<? extends IllegalArgumentException> exceptions;

    public MultipleValidationException(List<? extends IllegalArgumentException> exceptions) {
        super(buildMessage(exceptions));
        this.exceptions = exceptions;
    }

    public List<? extends IllegalArgumentException> getExceptions() {
        return exceptions;
    }

    private static String buildMessage(List<? extends IllegalArgumentException> exceptions) {
        StringBuilder sb = new StringBuilder("Validation failed with following errors:");
        for (RuntimeException ex : exceptions) {
            sb.append("\n - ").append(ex.getMessage());
        }
        return sb.toString();
    }
}
