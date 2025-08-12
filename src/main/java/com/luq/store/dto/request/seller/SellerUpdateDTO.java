package com.luq.store.dto.request.seller;

import com.luq.store.domain.Department;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;

public record SellerUpdateDTO(
    String name,
    Mail mail,
    Phone phone,
    Department department
){}
