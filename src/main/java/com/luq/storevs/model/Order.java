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
    
    @ManyToOne
    private Product product;
    @ManyToOne
    private Seller seller;

    public Order(Float totalPrice, Integer quantity, Product product){
        this.totalPrice = totalPrice;
        this.quantity = quantity; 
        this.product = product;
    }
}
