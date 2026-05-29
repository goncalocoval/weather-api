package com.example.weatherapi.service;

import com.example.weatherapi.exception.CityNotFoundException;
import com.example.weatherapi.model.ForecastDay;
import com.example.weatherapi.model.ForecastResponse;
import com.example.weatherapi.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherService{

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable(value = "weather", key = "#city + '_' + #country + '_' + #units + '_' + #lang")
    public WeatherResponse getWeather(String units, String lang, String city, String country){

        String query = (country != null && !country.isBlank()) ? city + "," + country : city;

        String url = UriComponentsBuilder.fromUriString(apiUrl + "/weather")
                .queryParam("q", query)
                .queryParam("appid", apiKey)
                .queryParam("units", units)
                .queryParam("lang", lang)
                .toUriString();

        try {

            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            String cityName = response.get("name").asText();
            String countryName = response.get("sys").get("country").asText();
            double temperature = response.get("main").get("temp").asDouble();
            String description = capitalizeText(response.get("weather").get(0).get("description").asText());

            int humidity = response.get("main").get("humidity").asInt();

            return new WeatherResponse(cityName, countryName, temperature, description, humidity);

        }catch (HttpClientErrorException.NotFound ex){
            throw new CityNotFoundException(city);
        }

    }

    @Cacheable(value = "forecast", key = "#city + '_' + #country + '_' + #units + '_' + #lang")
    public ForecastResponse getForecast(String units, String lang, String city, String country){

        String query = (country != null && !country.isBlank()) ? city + "," + country : city;

        String url = UriComponentsBuilder.fromUriString(apiUrl + "/forecast")
                .queryParam("q", query)
                .queryParam("appid", apiKey)
                .queryParam("units", units)
                .queryParam("lang", lang)
                .toUriString();

        try{

            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            String cityName = response.get("city").get("name").asText();
            String countryCode = response.get("city").get("country").asText();

            Map<String, List<JsonNode>> byDay = new LinkedHashMap<>();
            for (JsonNode item : response.get("list")) {
                String date = item.get("dt_txt").asText().substring(0, 10);
                byDay.computeIfAbsent(date, k -> new ArrayList<>()).add(item);
            }

            List<ForecastDay> forecastDays = new ArrayList<>();
            for (Map.Entry<String, List<JsonNode>> entry : byDay.entrySet()) {
                String date = entry.getKey();
                List<JsonNode> items = entry.getValue();

                double tempMin = items.stream()
                        .mapToDouble(i -> i.get("main").get("temp_min").asDouble())
                        .min().orElse(0);

                double tempMax = items.stream()
                        .mapToDouble(i -> i.get("main").get("temp_max").asDouble())
                        .max().orElse(0);

                int maxPop = items.stream()
                        .mapToInt(i -> (int) Math.round(i.get("pop").asDouble() * 100))
                        .max().orElse(0);

                JsonNode midday = items.stream()
                        .filter(i -> i.get("dt_txt").asText().contains("12:00:00"))
                        .findFirst()
                        .orElse(items.get(items.size() / 2));

                String description = capitalizeText(midday.get("weather").get(0).get("description").asText());

                int humidity = midday.get("main").get("humidity").asInt();

                forecastDays.add(new ForecastDay(date, tempMin, tempMax, description, humidity, maxPop));
            }

            return new ForecastResponse(cityName, countryCode, forecastDays);

        }catch (HttpClientErrorException.NotFound ex){
            throw new CityNotFoundException(city);
        }

    }

    public String capitalizeText(String text){
        return Arrays.stream(text.split("\\s+"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }

}