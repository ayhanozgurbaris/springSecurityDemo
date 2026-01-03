package com.security.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class WelcomeController {

    // 1. Herkesin (Giriş yapmış herkesin) görebileceği sayfa
    @GetMapping("/public")
    public String publicPage() {
        return "Public Area: Herkes burayı görebilir (User & Admin).";
    }

    // 2. Sadece ADMIN yetkisi olanın görebileceği sayfa
    @GetMapping("/admin")
    public String adminPage() {
        return "Admin Area: Sadece Yöneticiler burayı görebilir!";
    }
}
