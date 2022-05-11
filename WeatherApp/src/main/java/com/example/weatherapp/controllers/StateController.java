package com.example.weatherapp.controllers;

import com.example.weatherapp.models.City;
import com.example.weatherapp.models.State;
import com.example.weatherapp.models.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@EnableScheduling
public class StateController {
    Service service;
    LiveWeatherService liveWeatherService;
    ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Value("${weather.readOnly}")
    private boolean readOnly;

    @Value("${weather.updateInterval}")
    private int updateInterval;

    @Autowired
    public StateController(Service service, LiveWeatherService liveWeatherService) {
        this.service = service;
        this.liveWeatherService = liveWeatherService;
    }

    @Autowired
    public void setThreadPoolTaskScheduler(ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        if(readOnly){
            return;
        }
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        downloadNewData();
    }

    @RequestMapping(value = "/createState", method = RequestMethod.POST)
    public String createState(HttpServletRequest request, @RequestParam(value="name") String name, @RequestParam(value="code") String code){
        String referer = request.getHeader("Referer");
        if(readOnly){
            return "redirect:" + referer;
        }
        State state = new State(name,code);
        service.createNewState(state);

        return "redirect:" + referer;
    }
    @RequestMapping(value = "/createCity", method = RequestMethod.POST)
    public String createCity(HttpServletRequest request, @RequestParam(value="name") String name, @RequestParam(value="region") String region){
        String referer = request.getHeader("Referer");
        if(readOnly){
            return "redirect:" + referer;
        }
        service.createNewCity(name, region);

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
        String referer = request.getHeader("Referer");
        if(readOnly){
            return "redirect:" + referer;
        }
        service.deleteCity(name);

        return "redirect:" + referer;
    }
    @RequestMapping(value="/deleteState", method=RequestMethod.POST)
    public String deleteState(HttpServletRequest request, @RequestParam(value = "code") String code) {
        String referer = request.getHeader("Referer");
        if(readOnly){
            return "redirect:" + referer;
        }
        service.deleteState(code);
        return "redirect:" + referer;
    }
    @RequestMapping(value="/search/city", method=RequestMethod.GET)
    public String searchForMeteoData(HttpServletRequest request, Model model, @RequestParam(value = "name") String name, @RequestParam(value = "code") String code){
        String referer = request.getHeader("Referer");
        if(readOnly){
            return "redirect:" + referer;
        }
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
        model.addAttribute("stateCode",code);
        model.addAttribute("savedWeatherDataState", dataForState);
        return "saved-data-state";
    }

    private void downloadNewData(){
        threadPoolTaskScheduler.scheduleAtFixedRate(this::download, updateInterval);
    }
    private void download(){
        System.out.println("Downloading new data");
        List<City> cities = service.getCities();
        for(City city : cities){
            WeatherData weatherData = liveWeatherService.getCurrentWeather(city.getName(), city.getRegion());
            service.createWeatherData(weatherData);
        }
    }
}
