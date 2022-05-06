package com.example.weatherapp.controllers;

import com.example.weatherapp.models.City;
import com.example.weatherapp.models.CurrentWeather;
import com.example.weatherapp.models.State;
import com.example.weatherapp.models.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class StateController {
    Service service;
    LiveWeatherService liveWeatherService;

    @Autowired
    public StateController(Service service, LiveWeatherService liveWeatherService) {
        this.service = service;
        this.liveWeatherService = liveWeatherService;
    }

    @RequestMapping(value = "/createState", method = RequestMethod.POST)
    public String createState(HttpServletRequest request, @RequestParam(value="name") String name, @RequestParam(value="code") String code){
        State state = new State(name,code);
        service.createNewState(state);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @RequestMapping(value = "/createCity", method = RequestMethod.POST)
    public String createCity(HttpServletRequest request, @RequestParam(value="name") String name, @RequestParam(value="region") String region){
        service.createNewCity(name, region);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String getStates(Model model) {
        List<State> states = service.getAllStates();
        List<City> cities = service.getCities();
        model.addAttribute("states", states);
        model.addAttribute("cities", cities);
        return "index";
    }
    @RequestMapping(value="/deleteCity", method=RequestMethod.POST)
    public String deleteCity(HttpServletRequest request, @RequestParam(value = "name") String name) {
        service.deleteCity(name);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @RequestMapping(value="/deleteState", method=RequestMethod.POST)
    public String deleteState(HttpServletRequest request, @RequestParam(value = "code") String code) {
        service.deleteState(code);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @RequestMapping(value="/search/city", method=RequestMethod.GET)
    public String searchForMeteoData(Model model, @RequestParam(value = "name") String name, @RequestParam(value = "code") String code){
        WeatherData weatherData = liveWeatherService.getCurrentWeather(name, code);
        service.createWeatherData(weatherData);
        service.createNewCity(name, code);

        List<WeatherData> savedData = service.getWeatherData(name);
        model.addAttribute("currentWeather", weatherData);
        model.addAttribute("savedWeatherDataCity", savedData);
        return "current-weather";
    }
    @RequestMapping(value="/search/state", method=RequestMethod.GET)
    public String searchForMeteoDataState(Model model, @RequestParam(value = "code") String code){
        List<List<WeatherData>> dataForState = service.getCitiesAndDataForState(code);
        model.addAttribute("savedWeatherDataState", dataForState);
        return "saved-data-state";
    }

    @RequestMapping(value="/states/{name}", method=RequestMethod.GET)
    public List<List<WeatherData>> getState(@PathVariable(value = "name") String name) {
        return service.getCitiesAndDataForState(name);
    }
    @RequestMapping(value="/states/", method=RequestMethod.DELETE)
    public void deleteStates() {
        service.deleteStates();
    }
}
