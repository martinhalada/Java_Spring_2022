package com.example.weatherapp.controllers;

import com.example.weatherapp.models.CurrentWeather;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@RestController
public class WeatherController {

    @GetMapping("/search-location")
    public String searchLocation(@RequestParam(value="location",defaultValue = "Liberec") String location){
        return String.format("Location: %s",location);
    }

}
