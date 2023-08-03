package com.example.miniproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/no-auth")
    public String noAuth() {
        return "no auth success!";
    }

    @GetMapping("/require-auth")
    public String reAuth() {
        return "auth success!";
    }
}
