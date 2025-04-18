package com.marcos.desenvolvimento.authorization_ms.exception.handler;

import com.marcos.desenvolvimento.authorization_ms.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserExistsException.class)
    public ExceptionFilters handleExistentUserException(final UserExistsException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .status(409)
                .title("Conflict")
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public ExceptionFilters handleInternalServerErrorException(final InternalServerErrorException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .status(500)
                .title("Internal server error")
                .build();
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidUserCreationException.class)
    public ExceptionFilters handleInvalidUserCreationException(final InvalidUserCreationException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .status(422)
                .title("Unprocessable entity")
                .build();
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidLoginRequestException.class)
    public ExceptionFilters handleInvalidLoginRequestException(final InvalidLoginRequestException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .status(422)
                .title("Unprocessable entity")
                .build();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationContextException.class)
    public ExceptionFilters handleAuthenticationContextException(final AuthenticationContextException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .status(401)
                .title("Unauthorized")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GenericKeycloakException.class)
    public ExceptionFilters handleGenericKeycloakException(final GenericKeycloakException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .status(400)
                .title("Bad credentials or user not existent")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GenericTokenException.class)
    public ExceptionFilters handleGenericTokenException(final GenericTokenException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .status(401)
                .title("Invalid token!")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GenericInvalidRequestException.class)
    public ExceptionFilters handleGenericGenericInvalidRequestException(final GenericInvalidRequestException ex) {
        return ExceptionFilters.builder()
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage())
                .status(400)
                .title("Bad request")
                .build();
    }

}
