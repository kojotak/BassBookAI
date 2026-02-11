package com.bassbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service("authService")
public class AuthService {

    @Autowired
    private Environment environment;

    public boolean isOAuth2Configured() {
        return true;
    }
}