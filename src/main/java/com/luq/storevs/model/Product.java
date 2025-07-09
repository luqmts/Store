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
import lombok.ToString;

import jakarta.validation.constraints.Min;

@Data
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
    private String name;
    private String sku;
    private String description;
    
    @Min(1)
    private Float price;
    
    @ManyToOne
    private Supplier supplier;

    public Product(String sku, String name, String description, Supplier supplier, Float price) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.supplier = supplier;
        this.price = price;
    }
}