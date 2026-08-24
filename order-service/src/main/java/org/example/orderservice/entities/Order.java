package org.example.orderservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Long userId ;
    private Long productId ;

    private String productName ;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal priceAtPurchase ;

}
