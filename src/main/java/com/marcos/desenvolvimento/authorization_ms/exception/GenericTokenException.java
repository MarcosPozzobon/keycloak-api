package com.marcos.desenvolvimento.authorization_ms.exception;

public class GenericTokenException extends RuntimeException {

    public GenericTokenException(){}

    public GenericTokenException(String msg){
        super(msg);
    }

}
