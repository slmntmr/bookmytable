package com.bookmytable.controller;

import com.bookmytable.dto.Request.LoginRequest;
import com.bookmytable.dto.Response.LoginResponse;
import com.bookmytable.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        return authenticationService.authenticateUser(loginRequest);
    }
}
