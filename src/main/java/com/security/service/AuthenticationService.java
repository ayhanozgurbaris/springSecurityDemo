package com.security.service;

import com.security.dto.AuthResponseDTO;
import com.security.dto.LoginRequestDTO;
import com.security.dto.RegisterRequestDTO;
import com.security.entity.User;
import com.security.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequestDTO registerRequestDTO) {
        User newUser = new User();
        newUser.setUsername(registerRequestDTO.username());
        newUser.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
        newUser.setRole("USER");
        userRepository.save(newUser);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        // 1. Spring Security bizim yerimize kullanıcı adı ve şifreyi kontrol etsin
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // 2. Eğer üstteki satır hata vermediyse giriş başarılıdır.
        // Kullanıcıyı bul ve token üret.
        User user = userRepository.findByUsername(request.username()).orElseThrow();

        // Not: User sınıfımızın UserDetails implemente etmesi gerekiyor (Bir sonraki adımda düzelteceğiz)
        // Şimdilik JwtService.generateToken metodunun UserDetails istediğini unutma.
        // Hata almamak için User sınıfında küçük bir ayar yapacağız.

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build()
        );

        return new AuthResponseDTO(token);
    }
}
