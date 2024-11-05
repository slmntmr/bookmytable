package com.bookmytable.service;

import com.bookmytable.entity.business.User;
import com.bookmytable.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service // Bu sınıfın bir servis sınıfı olduğunu belirtir
public class UserService {

    private final UserRepository userRepository;



    // Tüm kullanıcıları listeleme işlevi
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Belirli bir kullanıcıyı ID'ye göre bulma işlevi
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Yeni bir kullanıcı ekleme işlevi
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Var olan bir kullanıcıyı güncelleme işlevi
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Kullanıcıyı ID'ye göre silme işlevi
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
