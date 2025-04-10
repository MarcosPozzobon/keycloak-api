package com.marcos.desenvolvimento.authorization_ms.dto.response;

public record TokenResponseDTO(String token, String refreshToken, Integer expiresIn) {
}
