package com.bookmytable.dto.Request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class LoginRequest {

    private String email; // Kullanıcı e-posta ile giriş yapacak

    private String password; // Kullanıcı şifresi
}
