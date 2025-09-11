package com.marcos.desenvolvimento.authorization_ms.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionTests {

    @Test
    void deveCriarUsuarioExistenteExceptionCorretamente() {
        String mensagem = "Usuário já existe";
        UsuarioExistenteException ex = new UsuarioExistenteException(mensagem);

        Assertions.assertThat(ex).isInstanceOf(UsuarioExistenteException.class);
        Assertions.assertThat(ex.getMessage()).isEqualTo(mensagem);
    }

    @Test
    void deveCriarGenericKeycloakExceptionCorretamente() {
        String mensagem = "Erro genérico Keycloak";
        GenericKeycloakException ex = new GenericKeycloakException(mensagem);

        Assertions.assertThat(ex).isInstanceOf(GenericKeycloakException.class);
        Assertions.assertThat(ex.getMessage()).isEqualTo(mensagem);
    }

    @Test
    void deveCriarInternalServerErrorExceptionComMensagem() {
        String mensagem = "Erro interno";
        InternalServerErrorException ex = new InternalServerErrorException(mensagem);

        Assertions.assertThat(ex).isInstanceOf(InternalServerErrorException.class);
        Assertions.assertThat(ex.getMessage()).isEqualTo(mensagem);
    }

    @Test
    void deveCriarInternalServerErrorExceptionComMensagemECausa() {
        String mensagem = "Erro interno";
        Throwable causa = new RuntimeException("Causa original");

        InternalServerErrorException ex = new InternalServerErrorException(mensagem, causa);

        Assertions.assertThat(ex).isInstanceOf(InternalServerErrorException.class);
        Assertions.assertThat(ex.getMessage()).isEqualTo(mensagem);
        Assertions.assertThat(ex.getCause()).isEqualTo(causa);
    }
}