package com.luq.storevs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "supply", uniqueConstraints=@UniqueConstraint(columnNames={"product"}))
@AllArgsConstructor
@NoArgsConstructor
public class Supply implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "Product")
    private Product product;

    private String createdBy;
    private LocalDateTime created;
    private String modifiedBy;
    private LocalDateTime modified;

    public Supply(Integer quantity, Product product){
        this.quantity = quantity;
        this.product = product;
    }
}
