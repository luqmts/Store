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
@Table(name="products")
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String sku;
    private String name;
    private String description;
    
    @ManyToOne()
    private Supplier supplier;
    private Double price;

    public Product(String sku, String name, String description, Supplier supplier, double price) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.supplier = supplier;
        this.price = price;
    }
}
