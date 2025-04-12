package com.marcos.desenvolvimento.authorization_ms.exception;

public class AuthenticationContextException extends RuntimeException{

    public AuthenticationContextException(){}

    public AuthenticationContextException(String msg){
        super(msg);
    }

}
