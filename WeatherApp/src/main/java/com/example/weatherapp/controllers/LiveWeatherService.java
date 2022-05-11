package com.example.weatherapp.controllers;

import com.example.weatherapp.models.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

@Service
public class LiveWeatherService {

    //private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q={city},{country}&APPID={key}&units=metric";
    private static final String WEATHER_URL =  "https://api.openweathermap.org/data/2.5/weather?q={city},\"\",{country}&appid={API key}&units=metric&lang=cz";
    @Value("${weather.apikey}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public LiveWeatherService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper){
        this.restTemplate=restTemplateBuilder.build();
        this.objectMapper=objectMapper;
    }

    public WeatherData getCurrentWeather(String city, String country) {
        URI url = new UriTemplate(WEATHER_URL).expand(city,country,apiKey);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        WeatherData weatherData = convert(response);
        weatherData.setLocationName(city);
        return weatherData;
    }

    private WeatherData convert(ResponseEntity<String> response){
        try{
            JsonNode root = objectMapper.readTree(response.getBody());

            String locationName = root.path("name").asText();
            String country = root.path("sys").path("country").asText();
            long time = root.path("dt").asLong();

            float temp = (float)root.path("main").path("temp").asDouble();
            float pressure = (float)root.path("main").path("pressure").asDouble();
            float humidity = (float)root.path("main").path("humidity").asDouble();
            float visibility = (float)root.path("visibility").asDouble();;
            float windSpeed = (float)root.path("wind").path("speed").asDouble();
            float windDegree = (float)root.path("wind").path("deg").asDouble();;
            float clouds = (float)root.path("clouds").path("all").asDouble();;

            return new WeatherData(locationName, country, time, temp, pressure, humidity, visibility, windSpeed, windDegree, clouds);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Error parsing JSON", e);
        }
    }
}
