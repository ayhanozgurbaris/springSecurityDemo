package com.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "8a4f5c2e1b3d9a7f6e4c2d8b5a1f9e3c7d2b6a4f5c8e1b3d9a7f6e4c2d8b5a1f";

    // 1. Token Üretme Metodu
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Token kime ait?
                .setIssuedAt(new Date(System.currentTimeMillis())) // Ne zaman üretildi?
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // Ne zaman ölecek? (24 dakika)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // İmzala
                .compact();
    }

    // 2. Token'dan Kullanıcı Adını Okuma
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
