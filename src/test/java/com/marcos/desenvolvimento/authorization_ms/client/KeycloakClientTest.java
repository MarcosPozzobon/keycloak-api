package com.marcos.desenvolvimento.authorization_ms.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class KeycloakClientTest {

    @Test
    void metodoObterTokendisparaExcecaoComUrlInvalida() {
        KeycloakClient client = new KeycloakClient();

        assertThrows(RuntimeException.class, () ->
                client.obterTokenKeycloakPorLoginESenha("usuario", "senha")
        );
    }
}