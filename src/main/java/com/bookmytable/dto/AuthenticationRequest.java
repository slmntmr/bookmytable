package com.bookmytable.dto;

import lombok.Data;

// Kullanıcı kimlik doğrulama isteği için DTO sınıfı
@Data // Lombok'tan gelen @Data anotasyonu getter, setter ve diğer gerekli metodları oluşturur
public class AuthenticationRequest {
    private String username;
    private String password;
}
