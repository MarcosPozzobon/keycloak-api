package com.marcos.desenvolvimento.authorization_ms.service;

import com.marcos.desenvolvimento.authorization_ms.client.KeycloakClient;
import com.marcos.desenvolvimento.authorization_ms.dto.request.LoginRequest;
import com.marcos.desenvolvimento.authorization_ms.dto.response.TokenResponseDTO;
import com.marcos.desenvolvimento.authorization_ms.exception.GenericKeycloakException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginServiceTest {

    private KeycloakClient keycloakClient;
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        keycloakClient = mock(KeycloakClient.class);
        loginService = new LoginService(keycloakClient);
    }

    @Test
    void deveRetornarTokenQuandoCredenciaisEstiveremCorretas() {
        JSONObject json = new JSONObject();
        json.put("access_token", "token123");
        json.put("refresh_token", "refresh123");
        json.put("expires_in", 3600);

        when(keycloakClient.obterTokenKeycloakPorLoginESenha("user", "pass"))
                .thenReturn(json.toString());

        LoginRequest request = new LoginRequest("user", "pass");
        TokenResponseDTO tokenResponse = loginService.obterToken(request);

        assertNotNull(tokenResponse);
        assertEquals("refresh123", tokenResponse.refreshToken());
        assertEquals(3600, tokenResponse.expiresIn());
    }

    @Test
    void deveLancarGenericKeycloakExceptionQuandoCredenciaisForemInvalidas() {
        when(keycloakClient.obterTokenKeycloakPorLoginESenha("user", "wrongpass"))
                .thenReturn("invalid_grant");

        LoginRequest request = new LoginRequest("user", "wrongpass");

        assertThrows(GenericKeycloakException.class, () -> loginService.obterToken(request));
    }
}