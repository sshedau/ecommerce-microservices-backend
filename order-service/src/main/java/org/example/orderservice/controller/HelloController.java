package org.example.orderservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders/hello")
public class HelloController {
    @GetMapping
    public String greet() {
        return "Hello from ORDER-SERVICE !" ;
    }
}
