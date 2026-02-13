package com.bassbook.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OidcUser user) {
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", user.getFullName());
        userInfo.put("email", user.getEmail());
        userInfo.put("sub", user.getSubject());
        userInfo.put("picture", user.getPicture());
        
        return userInfo;
    }
}