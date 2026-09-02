package org.example.userservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class InstanceController {

    @Value("${eureka.instance.instance-id:${spring.application.name}:${server.port}}")
    private String instanceId;

    @GetMapping("/instance")
    public String getInstance() {
        return "Request handled by: " + instanceId;
    }
}