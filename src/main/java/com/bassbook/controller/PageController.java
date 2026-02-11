package com.bassbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/form.html")
    public String form() {
        return "forward:/form.html";
    }

    @GetMapping("/login")
    public String login() {
        return "forward:/index.html";
    }
}