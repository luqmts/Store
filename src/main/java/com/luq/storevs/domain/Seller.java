package com.luq.storevs.model;

import com.luq.storevs.valueobjects.Mail;
import com.luq.storevs.valueobjects.Phone;
import com.luq.storevs.valueobjects.converters.MailConverter;
import com.luq.storevs.valueobjects.converters.PhoneConverter;

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

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="sellers")
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Seller implements Identifiable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Integer id;

    @ToString.Include
    @Column(name="Name", length = 50)
    private String name;
    @Convert(converter = MailConverter.class)    
    @Column(name="Mail", length = 50)
    private Mail mail;
    @Convert(converter = PhoneConverter.class)
    @Column(name="Phone", length = 11)
    private Phone phone;
    @Column(name="Department", length = 25)
    private Department department;

    private String createdBy;
    private LocalDateTime created;
    private String modifiedBy;
    private LocalDateTime modified;

    public Seller(String name, Mail mail, Phone phone, Department department){
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.department = department;
    }
}
