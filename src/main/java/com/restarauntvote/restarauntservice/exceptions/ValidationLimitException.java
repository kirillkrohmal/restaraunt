package com.restarauntvote.restarauntservice.exceptions;

public class ValidationLimitException extends RuntimeException{
    public ValidationLimitException(String exception) {
        super(exception);
    }
}
