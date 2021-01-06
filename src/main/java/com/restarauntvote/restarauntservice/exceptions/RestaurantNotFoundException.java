package com.restarauntvote.restarauntservice.exceptions;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(String exception) {
        super(exception);
    }
}