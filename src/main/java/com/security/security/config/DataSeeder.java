//package com.security.security.config;
//
//import com.security.entity.User;
//import com.security.repository.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataSeeder implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        if (userRepository.count() == 0) {
//
//            User admin = new User();
//            admin.setUsername("admin_db");
//            admin.setPassword(passwordEncoder.encode("admin123")); // Şifreyi Hashliyoruz!
//            admin.setRole("ADMIN");
//            userRepository.save(admin);
//
//            User user = new User();
//            user.setUsername("user_db");
//            user.setPassword(passwordEncoder.encode("user123"));
//            user.setRole("USER");
//            userRepository.save(user);
//
//            System.out.println("--- Veritabanına örnek kullanıcılar eklendi ---");
//        }
//    }
//}
