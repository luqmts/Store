package com.luq.store.dto.response.payment;

import com.luq.store.domain.OrderStatus;

public record PaymentStatusDTO(
    Integer orderId,
    OrderStatus status
) {}
