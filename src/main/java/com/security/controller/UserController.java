package com.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<String> getMyProfile(@AuthenticationPrincipal UserDetails currentUser) {

        String username = currentUser.getUsername();
        String roles = currentUser.getAuthorities().toString();

        String message = "Hoş geldin " + username + "! Senin rollerin şunlar: " + roles;

        return ResponseEntity.ok(message);
    }
}