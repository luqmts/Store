package com.luq.storevs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public Supply(Integer quantity, Product product){
        this.quantity = quantity;
        this.product = product;
    }
}
