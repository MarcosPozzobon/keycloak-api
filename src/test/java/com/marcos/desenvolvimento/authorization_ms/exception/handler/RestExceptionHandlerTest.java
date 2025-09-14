package com.marcos.desenvolvimento.authorization_ms.exception.handler;


import com.marcos.desenvolvimento.authorization_ms.exception.GenericKeycloakException;
import com.marcos.desenvolvimento.authorization_ms.exception.UsuarioExistenteException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    void deveRetornarExceptionFiltersQuandoUsuarioExistenteExceptionForLancada() {
        UsuarioExistenteException ex = new UsuarioExistenteException("Usuário já existe");

        ExceptionFilters response = handler.handleExistentUserException(ex);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getDetalhes()).isEqualTo("Usuário já existe");
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
        Assertions.assertThat(response.getTitulo()).isEqualTo("Conflict");
        Assertions.assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void deveRetornarExceptionFiltersQuandoGenericKeycloakExceptionForLancada() {
        GenericKeycloakException ex = new GenericKeycloakException("Usuário já existe");

        ExceptionFilters response = handler.handleGenericKeycloakException(ex);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
        Assertions.assertThat(response.getTitulo()).isEqualTo("Bad request");
        Assertions.assertThat(response.getTimestamp()).isNotNull();
    }

}