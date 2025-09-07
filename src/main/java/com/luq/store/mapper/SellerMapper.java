package com.luq.store.mapper;

import com.luq.store.domain.Seller;
import com.luq.store.dto.request.seller.SellerRegisterDTO;
import com.luq.store.dto.request.seller.SellerUpdateDTO;
import com.luq.store.dto.response.seller.SellerResponseDTO;
import com.luq.store.exceptions.InvalidMailException;
import com.luq.store.exceptions.InvalidPhoneException;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SellerMapper {
    public Seller toEntity(SellerRegisterDTO data) {
        Seller seller = new Seller();
        Mail mail = new Mail(data.mail());
        Phone phone = new Phone(data.phone());

        seller.setName(data.name());
        seller.setMail(mail);
        seller.setPhone(phone);
        seller.setDepartment(data.department());

        return seller;
    }

    public Seller toEntity(SellerUpdateDTO data) {
        Seller seller = new Seller();

        try{seller.setMail(new Mail(data.mail()));}
        catch(InvalidMailException e){seller.setMail(null);}

        try{seller.setPhone(new Phone(data.phone()));}
        catch(InvalidPhoneException e){seller.setPhone(null);}

        seller.setName(data.name());
        seller.setDepartment(data.department());

        return seller;
    }

    public Seller toEntity(SellerResponseDTO data) {
        Mail mail = new Mail(data.mail());
        Phone phone = new Phone(data.phone());

        return new Seller(
            data.id(),
            data.name(),
            mail,
            phone,
            data.department(),
            data.createdBy(),
            data.created(),
            data.modifiedBy(),
            data.modified()
        );
    }

    public SellerResponseDTO toDTO(Seller seller) {
        return new SellerResponseDTO(
            seller.getId(),
            seller.getName(),
            seller.getMail().toString(),
            seller.getPhone().toString(),
            seller.getDepartment(),
            seller.getCreatedBy(),
            seller.getCreated(),
            seller.getModifiedBy(),
            seller.getModified()
        );
    }

    public List<SellerResponseDTO> toDTOList(List<Seller> oList) {
        return oList
            .stream()
            .map(this::toDTO)
            .toList();
    }
}
