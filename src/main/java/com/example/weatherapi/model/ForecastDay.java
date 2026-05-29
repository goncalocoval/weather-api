package com.example.weatherapi.model;

import java.io.Serializable;

public record ForecastDay(
        String date,
        double tempMin,
        double tempMax,
        String description,
        int humidity,
        int precipitationProbability
) implements Serializable {}
