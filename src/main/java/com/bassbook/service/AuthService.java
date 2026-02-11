package com.bassbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service("authService")
public class AuthService {

    @Autowired
    private Environment environment;

    public boolean isOAuth2Configured() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles != null) {
            for (String profile : activeProfiles) {
                if ("oauth2".equals(profile)) {
                    return true;
                }
            }
        }
        return false;
    }
}