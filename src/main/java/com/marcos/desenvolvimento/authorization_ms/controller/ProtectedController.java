package com.marcos.desenvolvimento.authorization_ms.controller;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public HashMap<String, Object> returnOk(@RequestHeader("Authorization") String authorizationHeader) {
        if (tokenService.isValidToken(authorizationHeader)) {
            HashMap<String, Object> response = new HashMap<>();
            response.put("status", 200);
            response.put("msg", "authenticated");
            return response;
        }
        return null;
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String onlyAdminCanSeeThis(@RequestHeader("Authorization") String authorizationHeader){
        if(tokenService.isValidToken(authorizationHeader)){
            return "if you can see this you are an admin and you are fully authenticated.";
        }
        return "failed to validate the token!";
    }
}
