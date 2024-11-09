package com.bookmytable.repository;

import com.bookmytable.entity.business.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

    // Kullanıcının e-posta adresine göre var olup olmadığını kontrol eden metot
    boolean existsByEmail(String email);
}
