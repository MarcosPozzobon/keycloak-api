package com.marcos.desenvolvimento.authorization_ms.controller;

import com.marcos.desenvolvimento.authorization_ms.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

}
