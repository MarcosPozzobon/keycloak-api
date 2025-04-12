package com.marcos.desenvolvimento.authorization_ms.exception;

public class InvalidLoginRequestException extends RuntimeException{

    public InvalidLoginRequestException(){}

    public InvalidLoginRequestException(String msg){
        super(msg);
    }

}
