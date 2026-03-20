package com.security.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blacklisted_tokens")
public class BlacklistedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 500)
    private String token;

    // Sadece loglama veya sonradan veritabanını temizlemek için ekliyoruz
    private LocalDateTime blacklistedAt;

    // Boş Constructor (JPA için zorunlu)
    public BlacklistedToken() {
    }

    public BlacklistedToken(String token) {
        this.token = token;
        this.blacklistedAt = LocalDateTime.now();
    }

    public String getToken() {
        return token;
    }
}