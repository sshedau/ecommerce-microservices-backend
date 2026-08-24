package org.example.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}

//----------------------------------------------------
//        -- ORDERS
//----------------------------------------------------
//
//TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
//
//INSERT INTO orders
//        (user_id, product_id, product_name, price_at_purchase)
//VALUES
//        (4, 1, 'Laptop', 65000.00),
//    (4, 2, 'Mouse', 1200.00),
//            (4, 4, 'Monitor', 18000.00),
//
//            (5, 3, 'Keyboard', 2500.00),
//            (5, 5, 'Headphones', 3500.00),
//
//            (6, 1, 'Laptop', 65000.00),
//
//            (7, 6, 'Webcam', 4500.00),
//            (7, 7, 'USB-C Hub', 2200.00),
//
//            (8, 10, 'Smartphone', 45000.00),
//
//            (9, 11, 'Tablet', 30000.00),
//            (9, 13, 'Desk Lamp', 1800.00),
//
//            (10, 12, 'Gaming Chair', 15000.00),
//
//            (11, 8, 'Mechanical Keyboard', 5500.00),
//            (11, 9, 'External SSD', 7500.00),
//
//            (12, 14, 'Bluetooth Speaker', 4000.00),
//
//            (13, 15, 'Power Bank', 2500.00),
//            (13, 16, 'Wireless Charger', 1800.00),
//
//            (14, 17, 'Gaming Mouse', 3200.00),
//
//            (15, 18, 'Graphics Tablet', 8500.00),
//            (15, 19, 'Printer', 12000.00);
