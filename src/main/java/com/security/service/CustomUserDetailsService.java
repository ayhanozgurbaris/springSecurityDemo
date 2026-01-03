package com.security.service;

import com.security.entity.User;
import com.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(STR."User not found: \{username}");
        }

        User dbUser = userOptional.get();

        return org.springframework.security.core.userdetails.User.builder()
                .username(dbUser.getUsername())
                .password(dbUser.getPassword())
                .roles(dbUser.getRole())
                .build();
    }
}
