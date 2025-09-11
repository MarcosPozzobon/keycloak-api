package com.marcos.desenvolvimento.authorization_ms.service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        tokenService = new TokenService();
        Field field = TokenService.class.getDeclaredField("JWKS_URL");
        field.setAccessible(true); // libera acesso privado
        field.set(tokenService, "http://localhost/jwks");
    }

    @Test
    void getClaims_tokenInvalido_retornaNull() {
        JWTClaimsSet claims = tokenService.getClaims("tokenInvalido");
        assertNull(claims);
    }

    @Test
    void getClaims_tokenValido_retornaClaims() throws Exception {
        SignedJWT jwt = mock(SignedJWT.class);
        JWTClaimsSet mockClaims = new JWTClaimsSet.Builder().subject("usuario").build();
        when(jwt.getJWTClaimsSet()).thenReturn(mockClaims);

        JWTClaimsSet claims = tokenService.getClaims("Bearer tokenFake");
        assertNull(claims);
    }

    @Test
    void isTokenValido_tokenInvalido_retornaFalse() {
        boolean valido = tokenService.isTokenValido("Bearer tokenInvalido");
        assertFalse(valido);
    }

    @Test
    void isTokenValido_tokenDisparaErro_retornaFalse() {
        boolean valido = tokenService.isTokenValido("tokenErrado");
        assertFalse(valido);
    }
}