package com.luq.storevs.model;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.luq.storevs.valueobjects.CNPJ;
import com.luq.storevs.valueobjects.Mail;
import com.luq.storevs.valueobjects.Phone;
import com.luq.storevs.valueobjects.converters.CNPJConverter;
import com.luq.storevs.valueobjects.converters.MailConverter;
import com.luq.storevs.valueobjects.converters.PhoneConverter;

@Data
@Entity
@Table(name="suppliers")
@AllArgsConstructor
@NoArgsConstructor
public class Supplier implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;
    @Convert(converter = CNPJConverter.class)
    private CNPJ CNPJ;
    @Convert(converter = MailConverter.class)
    private Mail mail;
    @Convert(converter = PhoneConverter.class)
    private Phone phone;

     public Supplier(String name, CNPJ cnpj, Mail mail, Phone phone) {
        this.name = name;
        this.CNPJ = cnpj;
        this.mail = mail;
        this.phone = phone;
    }
}
