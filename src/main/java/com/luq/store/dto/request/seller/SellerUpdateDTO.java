package com.luq.store.dto.request.seller;

import com.luq.store.domain.Department;

public record SellerUpdateDTO(
    String name,
    String mail,
    String phone,
    Department department
){}
