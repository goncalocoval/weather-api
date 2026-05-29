package com.example.weatherapi.model;

public record ErrorResponse(
        int status,
        String error,
        String message
){}
