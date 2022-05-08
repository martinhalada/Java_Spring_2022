package com.example.weatherapp.rest;

import com.example.weatherapp.controllers.LiveWeatherService;
import com.example.weatherapp.controllers.Service;
import com.example.weatherapp.models.City;
import com.example.weatherapp.models.State;
import com.example.weatherapp.models.WeatherData;
import netscape.javascript.JSObject;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    Service service;
    LiveWeatherService liveWeatherService;

    @Autowired
    public RestController(Service service, LiveWeatherService liveWeatherService) {
        this.service = service;
        this.liveWeatherService = liveWeatherService;
    }

    @RequestMapping(value = "/api/states", method = RequestMethod.GET)
    public ResponseEntity<List<State>> getAllStates(){
        List<State> states = service.getAllStates();
        return new ResponseEntity<>(states, HttpStatus.OK);
    }
    @RequestMapping(value = "/api/cities", method = RequestMethod.GET)
    public ResponseEntity<List<City>> getAllCities(){
        List<City> cities = service.getCities();
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
    @RequestMapping(value = "/api/states", method = RequestMethod.POST)
    public ResponseEntity<String> createNewState(@RequestBody State state){
        service.createNewState(state);
        return new ResponseEntity<>("State saved", HttpStatus.OK);
    }
    @RequestMapping(value = "/api/cities", method = RequestMethod.POST)
    public ResponseEntity<String> createNewCity(@RequestBody City city){
        WeatherData weatherData = liveWeatherService.getCurrentWeather(city.getName(), city.getRegion());
        service.createWeatherData(weatherData);
        service.createNewCity(city);
        return new ResponseEntity<>("City saved", HttpStatus.OK);
    }
    @RequestMapping(value = "/api/states/{code}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteState(@PathVariable("code") String code){
        service.deleteState(code);
        return new ResponseEntity<>("State deleted", HttpStatus.OK);
    }
    @RequestMapping(value = "/api/cities/{name}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCity(@PathVariable("name") String name){
        service.deleteCity(name);
        return new ResponseEntity<>("City deleted", HttpStatus.OK);
    }

    @RequestMapping(value="/api/search/city/{name}/{code}", method=RequestMethod.GET)
    public ResponseEntity<List<WeatherData>> searchForCityData(@PathVariable(value = "name") String name, @PathVariable(value = "code") String code){
        WeatherData weatherData = liveWeatherService.getCurrentWeather(name, code);
        service.createWeatherData(weatherData);
        service.createNewCity(name, code);

        List<WeatherData> savedData = service.getWeatherData(name);
        return new ResponseEntity<>(savedData, HttpStatus.OK);
    }
    @RequestMapping(value="/api/search/state/{code}", method=RequestMethod.GET)
    public ResponseEntity<List<List<WeatherData>>> searchForStateData(@PathVariable(value = "code") String code){
        List<List<WeatherData>> dataForState = service.getCitiesAndDataForState(code);
        return new ResponseEntity<>(dataForState, HttpStatus.OK);
    }


}
