package com.bassbook.controller;

import com.bassbook.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping
    public String authStatusPage() {
        return "forward:/auth-status.html";
    }

    @GetMapping("/config")
    @ResponseBody
    public Map<String, Object> getAuthConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("oauth2Configured", authService.isOAuth2Configured());
        return config;
    }

    @GetMapping("/status")
    @ResponseBody
    public Map<String, Object> getAuthStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("oauth2Configured", authService.isOAuth2Configured());
        status.put("message", authService.isOAuth2Configured() ? 
            "OAuth2 login is available" : 
            "OAuth2 is not configured - please set credentials to enable login");
        return status;
    }
}