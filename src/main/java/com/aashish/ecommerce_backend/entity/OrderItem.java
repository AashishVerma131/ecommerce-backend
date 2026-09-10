package com.aashish.ecommerce_backend.entity;

import com.aashish.ecommerce_backend.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @Column(length = 50, nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @PrePersist
    public void generateId() {
        if (id == null) {
            id = IdGenerator.generate("orditem");
        }
    }
}
