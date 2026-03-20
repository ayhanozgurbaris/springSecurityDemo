package com.security.service;

import com.security.dto.AuthResponseDTO;
import com.security.dto.LoginRequestDTO;
import com.security.dto.RegisterRequestDTO;
import com.security.entity.User;
import com.security.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    public AuthResponseDTO refreshToken(String authHeader) {
        // 1. Gelen header boşsa veya Bearer ile başlamıyorsa hata ver
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Refresh token eksik veya hatalı!");
        }

        // 2. "Bearer " kısmını atıp token'ı al
        String refreshToken = authHeader.substring(7);

        // 3. Token içinden kullanıcı adını çıkar
        String username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            // Kullanıcıyı veritabanından bul
            User user = userRepository.findByUsername(username).orElseThrow();

            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();

            // SADECE YENİ BİR ACCESS TOKEN ÜRET (Refresh token'ı aynen geri dönüyoruz)
            String newAccessToken = jwtService.generateToken(userDetails);

            return new AuthResponseDTO(newAccessToken, refreshToken);
        }

        throw new RuntimeException("Geçersiz Refresh Token!");
    }
}
