package com.example.weatherapi.controller;

import com.example.weatherapi.model.ForecastResponse;
import com.example.weatherapi.model.WeatherResponse;
import com.example.weatherapi.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Weather", description = "Endpoint to check the weather in different cities.")
@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController (WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @Operation(summary = "Get weather data", description = "Returns the temperature and related data for a city.")
    @GetMapping
    public WeatherResponse getWeather(
            @RequestParam(defaultValue = "metric") String units,
            @RequestParam(defaultValue = "pt") String lang,
            @RequestParam String city,
            @RequestParam(required = false) String country)
    {
        return weatherService.getWeather(units, lang, city, country);
    }

    @Operation(summary = "Get weather forecast data", description = "Returns the weather forecast for the next 5 days, for a city.")
    @GetMapping("/forecast")
    public ForecastResponse getForecast(
            @RequestParam(defaultValue = "metric") String units,
            @RequestParam(defaultValue = "pt") String lang,
            @RequestParam String city,
            @RequestParam(required = false) String country
    ){
        return weatherService.getForecast(units, lang, city, country);
    }


}