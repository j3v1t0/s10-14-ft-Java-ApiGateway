package com.example.apigateway.fallback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {
    @GetMapping("/securityServiceFallback")
    public String securityFallback(){
        return "Security service is currently unavailable. Please try again later.";
    }

    @GetMapping("/userServiceFallback")
    public String userFallback(){
        return "User service is currently unavailable. Please try again later.";
    }

    @GetMapping("/eurekaServiceFallback")
    public String eurekaFallback(){
        return "Eureka service is currently unavailable. Please try again later.";
    }
}
