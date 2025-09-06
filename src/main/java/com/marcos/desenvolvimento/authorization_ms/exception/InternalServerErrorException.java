package com.marcos.desenvolvimento.authorization_ms.exception;

public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(String mensagem) {
        super(mensagem);
    }

    public InternalServerErrorException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }

}
