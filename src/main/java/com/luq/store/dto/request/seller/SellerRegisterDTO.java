package com.luq.store.dto.request.seller;

import com.luq.store.domain.Supplier;

import java.math.BigDecimal;

public record ProductRegisterDTO(
    String name,
    String sku,
    String description,
    BigDecimal price,
    Supplier supplier
){}