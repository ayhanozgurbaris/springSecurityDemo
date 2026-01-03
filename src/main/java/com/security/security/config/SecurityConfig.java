package com.security.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. PARÇA: Şifreleme Cihazı
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. PARÇA: Kullanıcı Listesi (Davetli Listesi)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        // Birinci Kullanıcıyı Oluşturuyoruz
        UserDetails user1 = User.withUsername("john_doe")
                .password(encoder.encode("user123")) // Şifreyi şifreliyoruz
                .roles("USER")
                .build();

        // İkinci Kullanıcıyı Oluşturuyoruz
        UserDetails user2 = User.withUsername("admin_jane")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        // Bu listeyi Spring'e teslim ediyoruz
        return new InMemoryUserDetailsManager(user1, user2);
    }

    // 3. PARÇA: Güvenlik Duvarı ve Kurallar (Kapıdaki Koruma)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Test kolaylığı için kapattık
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/public/**").permitAll() // Herkese açık
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // Sadece Admin
                        .anyRequest().authenticated() // Geri kalan her yer için giriş şart
                )
                .httpBasic(Customizer.withDefaults()) // Postman vb. için
                .formLogin(Customizer.withDefaults()); // Tarayıcı formu için

        return http.build();
    }
}