package com.security.service;

import com.security.dto.RegisterRequestDTO;
import com.security.entity.User;
import com.security.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void register(RegisterRequestDTO registerRequestDTO) {
        User newUser = new User();
        newUser.setUsername(registerRequestDTO.username());
        newUser.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
        newUser.setRole("USER");
        userRepository.save(newUser);
    }
}
