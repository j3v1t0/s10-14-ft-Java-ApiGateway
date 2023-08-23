package com.example.apigateway.fallback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {
    @GetMapping("/securityServiceFallback")
    public String securityFallback(){
        return "Security service is down!";
    }

    @GetMapping("/userServiceFallback")
    public String userFallback(){
        return "User service is down!";
    }

}
