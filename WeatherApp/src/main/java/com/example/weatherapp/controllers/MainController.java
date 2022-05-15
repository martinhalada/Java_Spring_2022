package com.example.weatherapp.controllers;

import com.example.weatherapp.WeatherAppApplication;
import com.example.weatherapp.models.City;
import com.example.weatherapp.models.State;
import com.example.weatherapp.models.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@EnableScheduling
public class MainController {
    Service service;
    LiveWeatherService liveWeatherService;
    ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Value("${weather.readOnly}")
    private boolean readOnly;

    @Value("${weather.updateInterval}")
    private int updateInterval;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    public MainController(Service service, LiveWeatherService liveWeatherService) {
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
    public String createState(Model model, HttpServletRequest request, @RequestParam(value="name") String name, @RequestParam(value="code") String code){
        String referer = request.getHeader("Referer");
        if(readOnly){
            model.addAttribute("status", HttpStatus.METHOD_NOT_ALLOWED);
            model.addAttribute("error", "Stát nelze uložit, je nastaven read-only mód.");
            return "error";
        }
        if (!service.isInputValid(name) || !service.isInputValid(code)){
            model.addAttribute("status", HttpStatus.BAD_REQUEST);
            model.addAttribute("error", "Chybně zadané vstupní parametry. Číslovky ani speciální znaky nejsou povoleny.");
            return "error";
        }
        State state = new State(name,code);
        service.createNewState(state);

        return "redirect:" + referer;
    }
    @RequestMapping(value = "/createCity", method = RequestMethod.POST)
    public String createCity(Model model, HttpServletRequest request, @RequestParam(value="name") String name, @RequestParam(value="region") String region){
        String referer = request.getHeader("Referer");
        if(readOnly){
            model.addAttribute("status", HttpStatus.METHOD_NOT_ALLOWED);
            model.addAttribute("error", "Město nelze uložit, je nastaven read-only mód.");
            return "error";
        }
        if (!service.isInputValid(name) || !service.isInputValid(region)){
            model.addAttribute("status", HttpStatus.BAD_REQUEST);
            model.addAttribute("error", "Chybně zadané vstupní parametry. Číslovky ani speciální znaky nejsou povoleny.");
            return "error";
        }
        try{
            WeatherData weatherData = liveWeatherService.getCurrentWeather(name, region);
            service.createNewCity(name, region);
            service.createWeatherData(weatherData);
        }catch (Exception e){
            LOGGER.warn("City not found " + e.getMessage());
            model.addAttribute("status", HttpStatus.BAD_REQUEST);
            model.addAttribute("error", "Zadané město neexistuje");
            return "error";
        }

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
    public String deleteCity(Model model, HttpServletRequest request, @RequestParam(value = "name") String name) {
        String referer = request.getHeader("Referer");
        if(readOnly){
            model.addAttribute("status", HttpStatus.METHOD_NOT_ALLOWED);
            model.addAttribute("error", "Město nelze smazat, je nastaven read-only mód.");
            return "error";
        }
        service.deleteCity(name);

        return "redirect:" + referer;
    }
    @RequestMapping(value="/deleteState", method=RequestMethod.POST)
    public String deleteState(Model model, HttpServletRequest request, @RequestParam(value = "code") String code) {
        String referer = request.getHeader("Referer");
        if(readOnly){
            model.addAttribute("status", HttpStatus.METHOD_NOT_ALLOWED);
            model.addAttribute("error", "Stát nelze smazat, je nastaven read-only mód.");
            return "error";
        }
        service.deleteState(code);
        return "redirect:" + referer;
    }
    @RequestMapping(value="/search/city", method=RequestMethod.GET)
    public String searchForMeteoData(Model model, @RequestParam(value = "name") String name, @RequestParam(value = "code") String code){
        if(readOnly){
            model.addAttribute("status", HttpStatus.METHOD_NOT_ALLOWED);
            model.addAttribute("error", "Měření nelze stáhnout, je nastaven read-only mód.");
            return "error";
        }
        WeatherData weatherData = null;
        try{
            weatherData = liveWeatherService.getCurrentWeather(name, code);
            service.createWeatherData(weatherData);
            service.createNewCity(name, code);

            List<WeatherData> savedData = service.getWeatherData(name);
            model.addAttribute("currentWeather", weatherData);
            model.addAttribute("savedWeatherDataCity", savedData);
            return "current-weather";
        }catch (HttpClientErrorException httpClientErrorException){
            LOGGER.warn("City not found "+ httpClientErrorException.getMessage());
            model.addAttribute("status", HttpStatus.BAD_REQUEST);
            model.addAttribute("error", "Zadané město neexistuje");
            return "error";
        }catch (ResourceAccessException resourceAccessException){
            LOGGER.error("No internet connection " + resourceAccessException.getMessage());
            model.addAttribute("status", HttpStatus.NOT_FOUND);
            model.addAttribute("error", "Nelze stáhnout data, pravděpodobně nejste připojeni k intenetu.");
            return "error";
        }
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
        try {
            List<City> cities = service.getCities();
            for (City city : cities) {
                WeatherData weatherData = liveWeatherService.getCurrentWeather(city.getName(), city.getRegion());
                service.createWeatherData(weatherData);
            }
            LOGGER.info("New data downloaded");
        }catch (Exception e){
            LOGGER.error("Error while downloading new data "+ e.getMessage());
        }
    }
}
