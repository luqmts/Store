package com.luq.storevs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Identifiable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="TotalPrice")
    private Float totalPrice;
    @Column(name="Quantity")
    private Integer quantity;
    @Column(name="OrderDate")
    private LocalDate orderDate;
    
    @ManyToOne
    @JoinColumn(name="Product")
    private Product product;
    @ManyToOne
    @JoinColumn(name="Seller")
    private Seller seller;
    @ManyToOne
    @JoinColumn(name="Customer")
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
}
