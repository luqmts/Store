package com.luq.storevs.domain;

import com.luq.storevs.valueobjects.Mail;
import com.luq.storevs.valueobjects.Phone;
import com.luq.storevs.valueobjects.converters.MailConverter;
import com.luq.storevs.valueobjects.converters.PhoneConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Seller seller = (Seller) o;
        return getId() != null && Objects.equals(getId(), seller.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
