package com.luq.store.dto.request.seller;

import com.luq.store.domain.Department;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;

public record SellerRegisterDTO(
    String name,
    String mail,
    String phone,
    Department department
){}