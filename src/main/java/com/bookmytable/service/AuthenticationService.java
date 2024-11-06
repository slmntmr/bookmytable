package com.bookmytable.service;


import com.bookmytable.dto.Request.LoginRequest;
import com.bookmytable.dto.Response.LoginResponse;
import com.bookmytable.repository.UserRepository;
import com.bookmytable.security.jwt.JwtUtils;
import com.bookmytable.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public ResponseEntity<LoginResponse> authenticateUser(LoginRequest loginRequest) {
        // Gelen email ve parola bilgilerini kullanarak doğrulama yapıyoruz
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // Doğrulanan kullanıcı SecurityContext'e atanıyor
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT token oluşturuluyor
        String token = jwtUtils.generateJwtToken(authentication);

        // Yanıt olarak LoginResponse ile token döndürülüyor
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
