package com.example.weatherapi.model;

import java.io.Serializable;

public record WeatherResponse(
        String city,
        String country,
        double temperature,
        String description,
        int humidity
) implements Serializable {}