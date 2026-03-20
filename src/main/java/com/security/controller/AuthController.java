package com.security.controller;

import com.security.dto.AuthResponseDTO;
import com.security.dto.LoginRequestDTO;
import com.security.dto.RegisterRequestDTO;
import com.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        authenticationService.register(registerRequestDTO);
        return ResponseEntity.ok("user registered succesfully");
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authenticationService.login(request);
    }

    @PostMapping("/refresh-token")
    public AuthResponseDTO refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authenticationService.refreshToken(authHeader);
    }


}
