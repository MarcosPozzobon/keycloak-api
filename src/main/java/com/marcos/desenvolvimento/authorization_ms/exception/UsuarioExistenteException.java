package com.marcos.desenvolvimento.authorization_ms.exception;

public class UsuarioExistenteException extends RuntimeException {

    public UsuarioExistenteException(String mensagem) {
        super(mensagem);
    }

    public UsuarioExistenteException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }
}
