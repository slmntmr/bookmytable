package com.bookmytable.dto.Request;

import lombok.Data;

// Kullanıcı kayıt isteği için DTO sınıfı
@Data
public class RegisterRequest {

    private String email;     // Kullanıcı e-posta adresi
    private String password;  // Kullanıcı şifresi

}
