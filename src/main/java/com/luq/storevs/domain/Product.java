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
import lombok.ToString;

import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

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
}