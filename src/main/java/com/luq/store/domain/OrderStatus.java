package com.luq.store.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING_PAYMENT ("Pending_payment"),
    EXPIRED_PAYMENT ("Expired_payment"),
    PAID ("Paid");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public static OrderStatus getOrderStatus(String description) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getDescription().toUpperCase().equals(description)) {
                return status;
            }
        }
        return null;
    }
}