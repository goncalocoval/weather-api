package com.example.weatherapi.exception;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String city){
        super("City Not Found: " + city);
    }
}
