package com.bookmytable.service;

import com.bookmytable.dto.Request.LoginRequest;
import com.bookmytable.dto.Request.RegisterRequest;
import com.bookmytable.dto.Response.LoginResponse;
import com.bookmytable.entity.business.User;
import com.bookmytable.repository.UserRepository;
import com.bookmytable.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<LoginResponse> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        // Kullanıcının daha önce kayıtlı olup olmadığını kontrol et
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // Yeni kullanıcı oluştur ve şifreyi şifrele
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Veritabanına kaydet
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }
}
