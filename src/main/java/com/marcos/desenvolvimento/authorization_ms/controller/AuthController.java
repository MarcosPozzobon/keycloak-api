package com.marcos.desenvolvimento.authorization_ms.controller;

import com.marcos.desenvolvimento.authorization_ms.dto.request.LoginRequest;
import com.marcos.desenvolvimento.authorization_ms.dto.request.RegisterRequest;
import com.marcos.desenvolvimento.authorization_ms.dto.response.TokenResponseDTO;
import com.marcos.desenvolvimento.authorization_ms.service.KeycloakService;
import com.marcos.desenvolvimento.authorization_ms.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final KeycloakService keycloakService;


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenResponseDTO> login(@RequestBody final LoginRequest loginRequest) {
        return ResponseEntity.status(200).body(loginService.setUserAuthenticationContext(loginRequest));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        keycloakService.createUser(request.username(), request.password(), request.email(), request.firstName(), request.lastName());
        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }
}
