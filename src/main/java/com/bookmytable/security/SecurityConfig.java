package com.bookmytable.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF korumasını devre dışı bırakıyoruz
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register", "/api/authenticate").permitAll() // Kayıt ve giriş için izin
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // /api/admin/** yalnızca ADMIN rolüne sahip kullanıcılar için
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN") // /api/users/** USER ve ADMIN rolleri için
                        .anyRequest().authenticated() // Diğer tüm uç noktalara erişim için kimlik doğrulama gerekli
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless oturum yönetimi
                );

        // JwtRequestFilter'i UsernamePasswordAuthenticationFilter'den önce ekliyoruz
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Şifreleme için BCryptPasswordEncoder kullanıyoruz
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
