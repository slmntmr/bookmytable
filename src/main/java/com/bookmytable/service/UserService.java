package com.bookmytable.service;

import com.bookmytable.entity.business.User;
import com.bookmytable.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service // Bu sınıfın bir servis sınıfı olduğunu belirtir
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Tüm kullanıcıları listeleme işlevi
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Belirli bir kullanıcıyı ID'ye göre bulma işlevi
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Yeni bir kullanıcı kayıt işlemi
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Şifreyi hashliyoruz
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    if (!user.getEmail().equals(updatedUser.getEmail()) &&
                            userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                        throw new RuntimeException("Email already in use");
                    }

                    user.setEmail(updatedUser.getEmail());

                    // Şifre değiştiriliyorsa, yeni şifreyi hashleyip güncelliyoruz
                    if (updatedUser.getPassword() != null && !passwordEncoder.matches(updatedUser.getPassword(), user.getPassword())) {
                        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    }
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    // Kullanıcıyı ID'ye göre silme işlevi
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
