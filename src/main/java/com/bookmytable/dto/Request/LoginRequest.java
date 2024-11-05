package com.bookmytable.dto.Request;

import lombok.Data;

// Kullanıcı kimlik doğrulama isteği için DTO sınıfı
@Data // Lombok'tan gelen @Data anotasyonu getter, setter ve diğer gerekli metodları oluşturur
public class LoginRequest {

    private String email; // Kullanıcı e-posta ile giriş yapacak

    private String password; // Kullanıcı şifresi
}
