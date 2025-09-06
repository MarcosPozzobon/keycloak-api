package com.marcos.desenvolvimento.authorization_ms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterRequest(
        String login,
        String senha,
        String email,
        @JsonProperty(value = "primeiro_nome")
        String primeiroNome,
        @JsonProperty(value = "ultimo_nome")
        String ultimoNome
) {
}
