package com.bassbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthPageController {

    @GetMapping
    public String authStatusPage() {
        return "forward:/auth-status.html";
    }
}