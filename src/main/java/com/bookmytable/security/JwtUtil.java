package com.bookmytable.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // Bu sınıfın bir Spring bileşeni olduğunu belirtir
public class JwtUtil {

    private final String SECRET_KEY = "my_secret_key"; // Gizli anahtarımız

    // Token oluşturma işlevi
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 saat geçerli olacak
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Token içindeki kullanıcı adını alma işlevi
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Token içindeki tüm bilgileri çıkarma işlevi
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Token süresinin geçerli olup olmadığını kontrol etme işlevi
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Token doğrulama işlevi
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
