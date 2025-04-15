package com.marcos.desenvolvimento.authorization_ms.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KeycloakUserDTO(
        String username,
        String email,
        @JsonProperty(value = "first_name")
        String firstName,
        @JsonProperty(value = "last_name")
        String lastName) {
}
