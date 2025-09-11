package com.marcos.desenvolvimento.authorization_ms.dto.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterRequestTest {

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest(
                "login-teste",
                "senha-teste",
                "email-teste",
                "primeiro-nome-teste",
                "ultimo-nome-teste"
        );
    }

    @Test
    void testeRegisterRequestDeveSerCriadoCorretamenteQuandoInstanciaNaoEhInvalida() {
        Assertions.assertThat(registerRequest).isNotNull();
        Assertions.assertThat(registerRequest.login()).isEqualTo("login-teste");
        Assertions.assertThat(registerRequest.senha()).isEqualTo("senha-teste");
        Assertions.assertThat(registerRequest.email()).isEqualTo("email-teste");
        Assertions.assertThat(registerRequest.primeiroNome()).isEqualTo("primeiro-nome-teste");
        Assertions.assertThat(registerRequest.ultimoNome()).isEqualTo("ultimo-nome-teste");
    }

    @Test
    void testeDeveGerarToStringComCampos() {
        String toString = registerRequest.toString();

        Assertions.assertThat(toString).contains("login-teste");
        Assertions.assertThat(toString).contains("senha-teste");
        Assertions.assertThat(toString).contains("email-teste");
        Assertions.assertThat(toString).contains("primeiro-nome-teste");
        Assertions.assertThat(toString).contains("ultimo-nome-teste");
    }

    @Test
    void testeEqualsEHashCode() {
        RegisterRequest r1 = new RegisterRequest(
                "login-teste",
                "senha-teste",
                "email-teste",
                "primeiro-nome-teste",
                "ultimo-nome-teste"
        );
        RegisterRequest r2 = new RegisterRequest(
                "login-teste",
                "senha-teste",
                "email-teste",
                "primeiro-nome-teste",
                "ultimo-nome-teste"
        );

        Assertions.assertThat(r1).isEqualTo(r2);
        Assertions.assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}