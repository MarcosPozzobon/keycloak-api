package com.marcos.desenvolvimento.authorization_ms.exception.handler;

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

}
