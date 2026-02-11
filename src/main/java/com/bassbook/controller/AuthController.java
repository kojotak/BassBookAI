package com.bassbook.controller;

import com.bassbook.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/config")
    public Map<String, Object> getAuthConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("oauth2Configured", authService.isOAuth2Configured());
        return config;
    }

    @GetMapping("/status")
    public Map<String, Object> getAuthStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("oauth2Configured", authService.isOAuth2Configured());
        status.put("message", authService.isOAuth2Configured() ? 
            "GitHub OAuth2 login is available" : 
            "GitHub OAuth2 is not configured - please set credentials to enable login");
        return status;
    }
}