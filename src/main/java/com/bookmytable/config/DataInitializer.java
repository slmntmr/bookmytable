package com.bookmytable.config;

import com.bookmytable.entity.business.Role;
import com.bookmytable.entity.business.User;
import com.bookmytable.repository.RoleRepository;
import com.bookmytable.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Rollerin var olup olmadığını kontrol ediyoruz, yoksa ekliyoruz
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ADMIN")));
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role("USER")));

        // Admin kullanıcısının var olup olmadığını kontrol ediyoruz, yoksa ekliyoruz
        Optional<User> adminUser = userRepository.findByEmail("admin@example.com");
        if (adminUser.isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123")); // Şifreyi hashliyoruz
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);
            userRepository.save(admin);
            System.out.println("Admin kullanıcı oluşturuldu: admin@example.com / Şifre: admin123");
        }
    }
}
