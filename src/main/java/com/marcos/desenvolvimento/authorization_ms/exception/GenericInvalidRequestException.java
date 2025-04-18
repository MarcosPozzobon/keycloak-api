package com.marcos.desenvolvimento.authorization_ms.exception;

public class GenericInvalidRequestException extends RuntimeException{

    public GenericInvalidRequestException(){}

    public GenericInvalidRequestException(String msg){
        super(msg);
    }

}
