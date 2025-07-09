package com.luq.storevs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name="orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Identifiable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Float totalPrice;
    private Integer quantity;
    private LocalDate orderDate;
    
    @ManyToOne
    private Product product;
    @ManyToOne
    private Seller seller;
    @ManyToOne
    private Customer customer;

    public Order(Float totalPrice, Integer quantity, Product product, Customer customer, LocalDate orderDate){
        this.totalPrice = totalPrice;
        this.quantity = quantity; 
        this.product = product;
        this.customer = customer;
        this.orderDate = orderDate;
    }
}
