package com.luq.storevs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.luq.storevs.valueobjects.Cnpj;
import com.luq.storevs.valueobjects.Mail;
import com.luq.storevs.valueobjects.Phone;
import com.luq.storevs.valueobjects.converters.CnpjConverter;
import com.luq.storevs.valueobjects.converters.MailConverter;
import com.luq.storevs.valueobjects.converters.PhoneConverter;

@Data
@Entity
@Table(name="suppliers")
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Supplier implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Integer id;
    
    @ToString.Include
    @Column(name="Name", length = 50)
    private String name;

    @Convert(converter = CnpjConverter.class)
    @Column(name="Cnpj", length = 18)
    private Cnpj cnpj;
    @Convert(converter = MailConverter.class)
    @Column(name="Mail", length = 50)
    private Mail mail;
    @Convert(converter = PhoneConverter.class)
    @Column(name="Phone", length = 11)
    private Phone phone;

    public Supplier(String name, Cnpj cnpj, Mail mail, Phone phone) {
        this.name = name;
        this.cnpj = cnpj;
        this.mail = mail;
        this.phone = phone;
    }
}
