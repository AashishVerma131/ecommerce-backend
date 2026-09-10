package com.aashish.ecommerce_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.aashish.ecommerce_backend.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @Column(length = 50, nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @PrePersist
    public void generateId() {
        if (id == null) {
            id = IdGenerator.generate("item");
        }
    }
}
