package com.luq.store.dto.request.supplier;

import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;

public record SupplierRegisterDTO(
    String name,
    String cnpj,
    String mail,
    String phone
){}