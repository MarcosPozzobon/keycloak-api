package com.marcos.desenvolvimento.authorization_ms.dto.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KeycloakUserDTOTest {

    private KeycloakUserDTO keycloakUserDTO;

    @BeforeEach
    void setup(){
        keycloakUserDTO = new KeycloakUserDTO(
                "username-teste",
                "email-teste",
                "first-name-teste",
                "last-name-teste"
        );
    }

    @Test
    void testeKeycloakUserDTODeveSerCriadoCorretamenteQuandoInstanciaNaoEhInvalida() {
        Assertions.assertThat(keycloakUserDTO).isNotNull();
        Assertions.assertThat(keycloakUserDTO.username()).isEqualTo("username-teste");
        Assertions.assertThat(keycloakUserDTO.email()).isEqualTo("email-teste");
        Assertions.assertThat(keycloakUserDTO.firstName()).isEqualTo("first-name-teste");
        Assertions.assertThat(keycloakUserDTO.lastName()).isEqualTo("last-name-teste");
    }

    @Test
    void testeDeveGerarToStringComCampos() {
        String toString = keycloakUserDTO.toString();
        Assertions.assertThat(toString).contains("username-teste");
        Assertions.assertThat(toString).contains("email-teste");
        Assertions.assertThat(toString).contains("first-name-teste");
        Assertions.assertThat(toString).contains("last-name-teste");
    }

    @Test
    void testeEqualsEHashCode() {
        KeycloakUserDTO keycloakUser1 = new KeycloakUserDTO("username-teste", "email-teste", "first-name-teste", "last-name-teste");
        KeycloakUserDTO keycloakUser2 = new KeycloakUserDTO("username-teste", "email-teste", "first-name-teste", "last-name-teste");

        Assertions.assertThat(keycloakUser1).isEqualTo(keycloakUser2);
        Assertions.assertThat(keycloakUser1.hashCode()).isEqualTo(keycloakUser2.hashCode());
    }
}