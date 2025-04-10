package com.marcos.desenvolvimento.authorization_ms.exception;

public class InvalidUserCreationException extends RuntimeException{

    public InvalidUserCreationException(){}

    public InvalidUserCreationException(String msg){
        super(msg);
    }

}
