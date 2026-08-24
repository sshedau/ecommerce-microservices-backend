package org.example.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}


//INSERT INTO product (name, price, active) VALUES
//                                              ('Laptop', 65000.00, true),
//                                                      ('Mouse', 1200.00, true),
//                                                      ('Keyboard', 2500.00, true),
//                                                      ('Monitor', 18000.00, true),
//                                                      ('Headphones', 3500.00, true),
//                                                      ('Webcam', 4500.00, true),
//                                                      ('USB-C Hub', 2200.00, true),
//                                                      ('Mechanical Keyboard', 5500.00, true),
//                                                      ('External SSD', 7500.00, true),
//                                                      ('Smartphone', 45000.00, true),
//                                                      ('Tablet', 30000.00, true),
//                                                      ('Gaming Chair', 15000.00, true),
//                                                      ('Desk Lamp', 1800.00, true),
//                                                      ('Bluetooth Speaker', 4000.00, true),
//                                                      ('Power Bank', 2500.00, true),
//                                                      ('Wireless Charger', 1800.00, true),
//                                                      ('Gaming Mouse', 3200.00, true),
//                                                      ('Graphics Tablet', 8500.00, true),
//                                                      ('Printer', 12000.00, true),
//                                                      ('External Hard Drive', 6000.00, true);
