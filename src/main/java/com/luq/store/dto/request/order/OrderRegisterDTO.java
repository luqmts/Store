package com.luq.store.dto.request.order;

import java.time.LocalDate;

public record OrderRegisterDTO(
    Integer quantity,
    LocalDate orderDate,
    Integer productId,
    Integer sellerId,
    Integer customerId
){}