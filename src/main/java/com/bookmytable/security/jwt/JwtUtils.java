package com.bookmytable.security.jwt;

import com.bookmytable.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${backendapi.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    // 512 bitlik güvenli bir SecretKey oluştur
    private final SecretKey jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Email'e göre token oluşturma
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return generateTokenFromEmail(userDetails.getEmail());
    }

    public String generateTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(jwtSecretKey)  // Güvenli anahtar kullan
                .compact();
    }

    // Token doğrulama
    public boolean validateJwtToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(jwtToken);  // Güvenli anahtar ile doğrula
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.error("Jwt token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Jwt token is unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Jwt token is invalid: {}", e.getMessage());
        } catch (SignatureException e) {
            LOGGER.error("Jwt Signature is invalid: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Jwt token is empty: {}", e.getMessage());
        }
        return false;
    }

    // Token içinden email'i çıkarma
    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)  // Güvenli anahtar ile doğrula
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
