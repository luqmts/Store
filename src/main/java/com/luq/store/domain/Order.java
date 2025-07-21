package com.luq.store.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name="orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Identifiable{
    @Id
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="TotalPrice")
    @ToString.Include
    private Float totalPrice;
    @Column(name="Quantity")
    @ToString.Include
    private Integer quantity;
    @Column(name="OrderDate")
    private LocalDate orderDate;
    
    @ManyToOne
    @JoinColumn(name="Product")
    @ToString.Include
    private Product product;
    @ManyToOne
    @JoinColumn(name="Seller")
    @ToString.Include
    private Seller seller;
    @ManyToOne
    @JoinColumn(name="Customer")
    @ToString.Include
    private Customer customer;

    private String createdBy;
    private LocalDateTime created;
    private String modifiedBy;
    private LocalDateTime modified;

    public Order(Float totalPrice, Integer quantity, Product product, Customer customer, LocalDate orderDate){
        this.totalPrice = totalPrice;
        this.quantity = quantity; 
        this.product = product;
        this.customer = customer;
        this.orderDate = orderDate;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Order order = (Order) o;
        return getId() != null && Objects.equals(getId(), order.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
