package org.example.productservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products/hello")
public class HelloController {
    @GetMapping
    public String greet() {
        return "Hello from PRODUCT-SERVICE !" ;
    }
}
