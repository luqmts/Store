package com.luq.storevs.model;

import com.luq.storevs.valueobjects.Mail;
import com.luq.storevs.valueobjects.Phone;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name="sellers")
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Seller {
    public enum Department{
        TECHNOLOGY,
        FOOD,
        CLOTHES
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Integer id;

    @ToString.Include
    private String name;
    private Mail mail;
    private Phone phone;
    private Department department;

    public Seller(String name, Mail mail, Phone phone, Department department){
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.department = department;
    }
}
