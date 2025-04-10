package com.marcos.desenvolvimento.authorization_ms.exception;

public class UserExistsException extends RuntimeException{

    public UserExistsException(){}

    public UserExistsException(String msg){
        super(msg);
    }

}
