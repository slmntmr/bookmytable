package com.bookmytable.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

// Kimlik doğrulama yanıtı için DTO sınıfı
@Data // Lombok'tan gelen @Data anotasyonu getter ve setter metotlarını otomatik oluşturur
@AllArgsConstructor // Lombok'tan gelen @AllArgsConstructor anotasyonu tüm alanlar için bir constructor oluşturur
public class LoginResponse {

    private String token;
}
