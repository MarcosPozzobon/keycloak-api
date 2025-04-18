package com.marcos.desenvolvimento.authorization_ms.controller;

import com.marcos.desenvolvimento.authorization_ms.dto.request.RegisterRequest;
import com.marcos.desenvolvimento.authorization_ms.dto.response.KeycloakUserDTO;
import com.marcos.desenvolvimento.authorization_ms.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final KeycloakService keycloakService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<KeycloakUserDTO> findAll(){
        return keycloakService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> findById(@PathVariable(value = "id") String id){
        keycloakService.findById(id);
        return ResponseEntity.status(200).build();
    }

    @PatchMapping("/{id}/status/{enable}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> setUserStatus(
            @PathVariable String id,
            @PathVariable boolean enable
    ) {
        boolean success = keycloakService.setUserStatus(id, enable);
        return success
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@RequestBody final RegisterRequest request) {
        keycloakService.createUser(request);
        return ResponseEntity.ok("User successfully registered.");
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(String id, final KeycloakUserDTO userDTO){
        keycloakService.updateUser(id, userDTO);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable(value = "id") String id){
        keycloakService.deleteUser(id);
        return ResponseEntity.status(200).build();
    }
}
