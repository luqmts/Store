package com.luq.store.mapper;

import com.luq.store.domain.Supplier;
import com.luq.store.dto.request.supplier.SupplierRegisterDTO;
import com.luq.store.dto.request.supplier.SupplierUpdateDTO;
import com.luq.store.dto.response.supplier.SupplierResponseDTO;
import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SupplierMapper {
    public Supplier toEntity(SupplierRegisterDTO data) {
        Supplier supplier = new Supplier();
        Cnpj cnpj = new Cnpj(data.cnpj());
        Mail mail = new Mail(data.mail());
        Phone phone = new Phone(data.phone());

        supplier.setName(data.name());
        supplier.setCnpj(cnpj);
        supplier.setMail(mail);
        supplier.setPhone(phone);

        return supplier;
    }

    public Supplier toEntity(SupplierUpdateDTO data) {
        Supplier supplier = new Supplier();
        Cnpj cnpj = new Cnpj(data.cnpj());
        Mail mail = new Mail(data.mail());
        Phone phone = new Phone(data.phone());

        supplier.setName(data.name());
        supplier.setCnpj(cnpj);
        supplier.setMail(mail);
        supplier.setPhone(phone);

        return supplier;
    }

    public Supplier toEntity(SupplierResponseDTO data) {
        return new Supplier(
            data.id(),
            data.name(),
            new Cnpj(data.cnpj()),
            new Mail(data.mail()),
            new Phone(data.phone()),
            data.createdBy(),
            data.created(),
            data.modifiedBy(),
            data.modified()
        );
    }

    public SupplierResponseDTO toDTO(Supplier supplier) {
        return new SupplierResponseDTO(
            supplier.getId(),
            supplier.getName(),
            supplier.getCnpj().toString(),
            supplier.getMail().toString(),
            supplier.getPhone().toString(),
            supplier.getCreatedBy(),
            supplier.getCreated(),
            supplier.getModifiedBy(),
            supplier.getModified()
        );
    }

    public List<SupplierResponseDTO> toDTOList(List<Supplier> oList) {
        return oList
            .stream()
            .map(this::toDTO)
            .toList();
    }
}
