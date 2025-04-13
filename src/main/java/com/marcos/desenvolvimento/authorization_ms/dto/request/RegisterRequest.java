package com.marcos.desenvolvimento.authorization_ms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterRequest(
        String username,
        String password,
        String email,
        @JsonProperty(value = "first_name")
        String firstName,
        @JsonProperty(value = "last_name")
        String lastName
) {
}
