package com.aashish.ecommerce_backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.aashish.ecommerce_backend.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @Column(length = 50, nullable = false, updatable = false)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    @JsonManagedReference
    private List<CartItem> items = new ArrayList<>();

    @PrePersist
    public void generateId() {
        if (id == null) {
            id = IdGenerator.generate("cart");
        }
    }
}
