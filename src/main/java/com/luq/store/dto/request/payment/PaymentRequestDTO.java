package com.luq.store.dto.request.payment;

import java.math.BigDecimal;

public record PaymentRequestDTO (
    BigDecimal unitPrice,
    Integer quantity,
    String product_name
){}
