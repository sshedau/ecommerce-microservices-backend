package org.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderDto {
    private Long id;

    private Long userId;

    private Long productId;

    private String productName;

    private BigDecimal priceAtPurchase;
}
