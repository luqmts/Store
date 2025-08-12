package com.luq.store.dto.request.product;

import java.math.BigDecimal;

public record ProductUpdateDTO(
    String name,
    String sku,
    String description,
    BigDecimal price,
    Integer supplier_id
){}
