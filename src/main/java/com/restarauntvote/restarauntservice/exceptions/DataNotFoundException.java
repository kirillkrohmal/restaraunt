package com.restarauntvote.restarauntservice.exceptions;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String exception) {
        super(exception);
    }
}
