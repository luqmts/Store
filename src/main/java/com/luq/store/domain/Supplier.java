package com.luq.store.domain;

import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import com.luq.store.valueobjects.converters.CnpjConverter;
import com.luq.store.valueobjects.converters.MailConverter;
import com.luq.store.valueobjects.converters.PhoneConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
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

    private String createdBy;
    private LocalDateTime created;
    private String modifiedBy;
    private LocalDateTime modified;

    public Supplier(String name, Cnpj cnpj, Mail mail, Phone phone) {
        this.name = name;
        this.cnpj = cnpj;
        this.mail = mail;
        this.phone = phone;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Supplier supplier = (Supplier) o;
        return getId() != null && Objects.equals(getId(), supplier.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
