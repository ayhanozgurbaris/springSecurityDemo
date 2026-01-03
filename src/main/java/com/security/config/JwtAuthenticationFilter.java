package com.security.config;

import com.security.service.CustomUserDetailsService;
import com.security.service.JwtService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Header'dan "Authorization" kısmını al
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. Token yoksa veya "Bearer " ile başlamıyorsa işlem yapma, diğer filtreye geç
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. "Bearer " kısmını kırp (7 karakter), sadece token kalsın
        jwt = authHeader.substring(7);

        // 4. Token içinden kullanıcı adını çek (JwtService bu işi yapıyor)
        username = jwtService.extractUsername(jwt);

        // 5. Kullanıcı adı varsa ve sistemde henüz doğrulanmamışsa
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Veritabanından kullanıcıyı bul
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // (Burada normalde token valid mi diye kontrol edilir, şimdilik basit tutuyoruz)

            // 6. Kimlik kartını oluştur (UsernamePasswordAuthenticationToken)
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 7. Sisteme "Bu kişi artık giriş yapmıştır" de (Context'e kaydet)
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 8. Zincirdeki diğer filtrelere devam et
        filterChain.doFilter(request, response);
    }
}