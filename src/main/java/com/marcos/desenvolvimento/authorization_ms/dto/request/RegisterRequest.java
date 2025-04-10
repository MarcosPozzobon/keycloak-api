package com.marcos.desenvolvimento.authorization_ms.dto.request;

public record RegisterRequest(String username, String password, String email, String firstName, String lastName) {
}
