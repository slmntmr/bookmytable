package com.bookmytable.controller;

import com.bookmytable.entity.business.User;
import com.bookmytable.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Tüm kullanıcıları listeleme işlemi, yalnızca ADMIN rolüne sahip kullanıcılar erişebilir
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all")  // GET (http://localhost:8080/api/users/all)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Belirli bir kullanıcıyı ID'ye göre alma işlemi, yalnızca ADMIN rolüne sahip kullanıcılar erişebilir
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/view/{id}")  // GET (http://localhost:8080/api/users/view/{id})
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Var olan bir kullanıcıyı güncelleme işlemi, yalnızca ADMIN rolüne sahip kullanıcılar erişebilir
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/{id}")  // PUT (http://localhost:8080/api/users/update/{id})
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    // Belirli bir kullanıcıyı silme işlemi, yalnızca ADMIN rolüne sahip kullanıcılar erişebilir
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")  // DELETE (http://localhost:8080/api/users/delete/{id})
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
