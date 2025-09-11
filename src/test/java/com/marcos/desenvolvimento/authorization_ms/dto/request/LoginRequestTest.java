package com.marcos.desenvolvimento.authorization_ms.dto.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setup() {
        loginRequest = new LoginRequest("login-teste", "senha-teste");
    }

    @Test
    void testeLoginRequestDeveSerCriadoCorretamenteQuandoInstanciaNaoEhInvalida() {
        Assertions.assertThat(loginRequest).isNotNull();
        Assertions.assertThat(loginRequest.login()).isEqualTo("login-teste");
        Assertions.assertThat(loginRequest.senha()).isEqualTo("senha-teste");
    }

    @Test
    void testeDeveGerarToStringComCampos() {
        String toString = loginRequest.toString();
        Assertions.assertThat(toString).contains("login-teste");
        Assertions.assertThat(toString).contains("senha-teste");
    }

    @Test
    void testeEqualsEHashCode() {
        LoginRequest r1 = new LoginRequest("login-teste", "senha-teste");
        LoginRequest r2 = new LoginRequest("login-teste", "senha-teste");

        Assertions.assertThat(r1).isEqualTo(r2);
        Assertions.assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}