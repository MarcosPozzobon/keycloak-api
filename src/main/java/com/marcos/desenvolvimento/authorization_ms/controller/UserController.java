package com.marcos.desenvolvimento.authorization_ms.controller;

import com.marcos.desenvolvimento.authorization_ms.dto.response.KeycloakUserDTO;
import com.marcos.desenvolvimento.authorization_ms.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final KeycloakService keycloakService;


    @GetMapping
    public List<KeycloakUserDTO> findAll(){
        return keycloakService.findAll();
    }

}
