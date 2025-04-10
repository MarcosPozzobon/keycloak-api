package com.marcos.desenvolvimento.authorization_ms.exception;

public class InternalServerErrorException extends RuntimeException{

    public InternalServerErrorException(){}

    public InternalServerErrorException(String msg){
        super(msg);
    }
}
