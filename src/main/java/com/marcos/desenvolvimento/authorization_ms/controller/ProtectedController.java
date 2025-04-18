package com.marcos.desenvolvimento.authorization_ms.controller;

import com.marcos.desenvolvimento.authorization_ms.exception.AuthenticationContextException;
import com.marcos.desenvolvimento.authorization_ms.service.LoginService;
import com.marcos.desenvolvimento.authorization_ms.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "protected")
@RequiredArgsConstructor
public class ProtectedController {

    private final TokenService tokenService;
    private final LoginService loginService;

    @GetMapping
    public String onlyAdminCanSeeThis(@RequestHeader("Authorization") String authorizationHeader){
        loginService.verifyAuthenticationContext(authorizationHeader, "ADMIN", ProtectedController::showMessage);
        return "";
    }

    public static String showMessage(){
        return "admin access granted!";
    }

}
