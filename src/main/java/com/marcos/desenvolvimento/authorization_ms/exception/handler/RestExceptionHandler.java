package com.marcos.desenvolvimento.authorization_ms.exception.handler;

import com.marcos.desenvolvimento.authorization_ms.exception.GenericKeycloakException;
import com.marcos.desenvolvimento.authorization_ms.exception.UsuarioExistenteException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UsuarioExistenteException.class)
    public ExceptionFilters handleExistentUserException(final UsuarioExistenteException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .detalhes(ex.getMessage())
                .status(409)
                .titulo("Conflict")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GenericKeycloakException.class)
    public ExceptionFilters handleGenericKeycloakException(final GenericKeycloakException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .detalhes(ex.getMessage())
                .status(400)
                .titulo("Bad request")
                .build();
    }

}
