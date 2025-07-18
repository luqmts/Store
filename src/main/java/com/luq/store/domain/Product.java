package com.luq.store.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name="products")
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Product implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Integer id;
    @ToString.Include
    @Column(name = "Name", length = 50)
    private String name;
    @Column(name = "Sku", length = 25)
    private String sku;
    @Column(name = "Description", length = 1000)
    private String description;
    
    @Min(1)
    private Float price;
    
    @ManyToOne
    @JoinColumn(name="Supplier")
    private Supplier supplier;

    private String createdBy;
    private LocalDateTime created;
    private String modifiedBy;
    private LocalDateTime modified;

    public Product(String sku, String name, String description, Supplier supplier, Float price) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.supplier = supplier;
        this.price = price;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}