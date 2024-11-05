package com.bookmytable.security;

import com.bookmytable.dto.Request.LoginRequest;
import com.bookmytable.dto.Request.RegisterRequest;
import com.bookmytable.dto.Response.LoginResponse;
import com.bookmytable.entity.business.User;
import com.bookmytable.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // Kullanıcının kimlik doğrulamasını sağlıyor ve token oluşturuyor
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Kullanıcı kimlik bilgilerini doğruluyoruz
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            // Token oluşturup yanıt olarak döndürüyoruz
            String token = jwtUtil.generateToken(request.getEmail());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
    // Kullanıcı kayıt işlemi
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Kayıt için yeni bir User nesnesi oluşturuyoruz
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword()); // Şifre hashlenmesi UserService içinde yapılacak


            User registeredUser = userService.registerUser(user); // Kullanıcıyı kaydediyoruz
            return ResponseEntity.status(201).body("User registered successfully: " + registeredUser.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Registration failed: " + e.getMessage());
        }
    }
}
