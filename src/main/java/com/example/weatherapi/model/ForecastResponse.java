package com.example.weatherapi.model;

import java.io.Serializable;
import java.util.List;

public record ForecastResponse(
        String city,
        String country,
        List<ForecastDay> forecast
) implements Serializable {}
