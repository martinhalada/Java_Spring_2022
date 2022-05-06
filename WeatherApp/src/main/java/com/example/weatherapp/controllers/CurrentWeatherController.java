package com.example.weatherapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CurrentWeatherController {

    private final StubWeatherService stubWeatherService;
    private final LiveWeatherService liveWeatherService;

    public CurrentWeatherController(StubWeatherService stubWeatherService, LiveWeatherService liveWeatherService){
        this.stubWeatherService=stubWeatherService;
        this.liveWeatherService=liveWeatherService;
    }

    @GetMapping("/current-weather")
    public String getCurrentWeather(Model model, @RequestParam(value="location",defaultValue = "Liberec") String location){
        if(true){
            model.addAttribute("currentWeather",liveWeatherService.getCurrentWeather(location, "cz"));
        }else{
            model.addAttribute("currentWeather", stubWeatherService.getCurrentWeather("Liberec","cz"));
        }
        return "current-weather";
    }

}
