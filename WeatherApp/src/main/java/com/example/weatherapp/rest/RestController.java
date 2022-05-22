package com.example.weatherapp.rest;

import com.example.weatherapp.WeatherAppApplication;
import com.example.weatherapp.controllers.LiveWeatherService;
import com.example.weatherapp.controllers.Service;
import com.example.weatherapp.models.City;
import com.example.weatherapp.models.State;
import com.example.weatherapp.models.WeatherData;
import jdk.jfr.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
@EnableScheduling
public class RestController {

    Service service;
    LiveWeatherService liveWeatherService;
    ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Value("${weather.csvFileName}")
    private String csvFileName;
    @Value("${weather.csvToUpload}")
    private String csvFileToUpload;
    @Value("${weather.readOnly}")
    private boolean readOnly;
    @Value("${weather.updateInterval}")
    private int updateInterval;
    @Value("${weather.apiLimit}")
    private int apiLimit;

    private int numberOfRequests;

    private static final Logger LOGGER = LoggerFactory.getLogger(RestController.class);


    @Autowired
    public RestController(Service service, LiveWeatherService liveWeatherService) {
        this.service = service;
        this.liveWeatherService = liveWeatherService;
        this.numberOfRequests = 0;
    }
    @Autowired
    public void setThreadPoolTaskScheduler(ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        if(readOnly){
            return;
        }
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        downloadNewData();
        clearAPICallsInfo();
    }
    private void downloadNewData(){
        threadPoolTaskScheduler.scheduleAtFixedRate(this::download, updateInterval);
    }
    private void clearAPICallsInfo(){threadPoolTaskScheduler.scheduleAtFixedRate(this::clearNumber, 60000);}
    private void clearNumber(){ numberOfRequests = 0; }
    private void download(){
        try {
            List<City> cities = service.getCities();
            for (City city : cities) {
                if(numberOfRequests >= apiLimit){
                    LOGGER.warn("Maximum API calls reached, not all cities data were updated.");
                    return;
                }
                WeatherData weatherData = liveWeatherService.getCurrentWeather(city.getName(), city.getRegion());
                service.createWeatherData(weatherData);
                numberOfRequests++;
            }
            LOGGER.info("New data downloaded");
        }catch (Exception e){
            LOGGER.error("Error while downloading new data "+ e.getMessage());
        }
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
        if(readOnly){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if(state.getName().isEmpty() || state.getCode().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!service.isInputValid(state.getName()) || !service.isInputValid(state.getCode())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        service.createNewState(state);
        return new ResponseEntity<>("State saved", HttpStatus.OK);
    }
    @RequestMapping(value = "/api/cities", method = RequestMethod.POST)
    public ResponseEntity<String> createNewCity(@RequestBody City city){
        if(readOnly){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if(city.getName().isEmpty() || city.getRegion().isEmpty() || city.getState()==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!service.isInputValid(city.getName()) || !service.isInputValid(city.getRegion())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            if(numberOfRequests >= apiLimit){
                LOGGER.warn("Maximum API calls reached, not all cities data were updated.");
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }
            WeatherData weatherData = liveWeatherService.getCurrentWeather(city.getName(), city.getRegion());
            numberOfRequests++;
            service.createWeatherData(weatherData);
            service.createNewCity(city);
        }catch (Exception e){
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("City saved", HttpStatus.OK);
    }
    @RequestMapping(value = "/api/states/{code}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteState(@PathVariable("code") String code){
        if(readOnly){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if(!service.isInputValid(code)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        service.deleteState(code);
        return new ResponseEntity<>("State deleted", HttpStatus.OK);
    }
    @RequestMapping(value = "/api/cities/{name}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCity(@PathVariable("name") String name){
        if(readOnly){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if(!service.isInputValid(name)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        service.deleteCity(name);
        return new ResponseEntity<>("City deleted", HttpStatus.OK);
    }

    @RequestMapping(value="/api/search/city/{name}/{code}", method=RequestMethod.GET)
    public ResponseEntity<List<WeatherData>> searchForCityData(@PathVariable(value = "name") String name, @PathVariable(value = "code") String code){
        if(readOnly){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if(!service.isInputValid(name) || !service.isInputValid(code)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            if(numberOfRequests >= apiLimit){
                LOGGER.warn("Maximum API calls reached, not all cities data were updated.");
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }
            WeatherData weatherData = liveWeatherService.getCurrentWeather(name, code);
            numberOfRequests++;
            service.createWeatherData(weatherData);
            service.createNewCity(name, code);
        }catch (HttpClientErrorException httpClientErrorException){
            LOGGER.warn("City:"+name+", "+code+" "+" not found "+httpClientErrorException.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (ResourceAccessException resourceAccessException) {
            LOGGER.error("No internet connection "+resourceAccessException.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<WeatherData> savedData = service.getWeatherData(name);
        return new ResponseEntity<>(savedData, HttpStatus.OK);
    }
    @RequestMapping(value="/api/search/state/{code}", method=RequestMethod.GET)
    public ResponseEntity<List<List<WeatherData>>> searchForStateData(@PathVariable(value = "code") String code){
        if(!service.isInputValid(code)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<List<WeatherData>> dataForState = service.getCitiesAndDataForState(code);
        return new ResponseEntity<>(dataForState, HttpStatus.OK);
    }
    @RequestMapping(value="/api/avg/{name}", method=RequestMethod.GET)
    public ResponseEntity<List<List<Float>>> searchForAvg(@PathVariable(value = "name") String name){
        if(!service.isInputValid(name)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Float> avgValuesDay = service.getAvgValues(name, 1);
        List<Float> avgValuesWeek = service.getAvgValues(name, 7);
        List<Float> avgValues2Weeks = service.getAvgValues(name, 14);
        List<List<Float>> avg = new ArrayList<>(
                Arrays.asList(avgValuesDay, avgValuesWeek, avgValues2Weeks));
        return new ResponseEntity<>(avg, HttpStatus.OK);
    }
    @RequestMapping(value="/api/updateState", method=RequestMethod.POST)
    public ResponseEntity<String> updateState(@RequestBody List<String> data){
        service.updateState(data);
        return new ResponseEntity<>("State updated", HttpStatus.OK);
    }
    @RequestMapping(value="/api/updateCity", method=RequestMethod.POST)
    public ResponseEntity<String> updateCity(@RequestBody List<String> data){
        service.updateCity(data);
        return new ResponseEntity<>("City updated", HttpStatus.OK);
    }
    @RequestMapping(value="/api/updateData", method=RequestMethod.POST)
    public ResponseEntity<String> updateWeatherData(@RequestBody List<WeatherData> data){
        service.updateWeatherData(data);
        return new ResponseEntity<>("Weather data updated", HttpStatus.OK);
    }

    @RequestMapping(value="/api/downloadCSV", method=RequestMethod.GET)
    public ResponseEntity<String> downloadCSV() throws IOException {
        List<WeatherData> weatherData = service.getAllWeatherData();
        try {
            File f = new File(csvFileName);
            f.createNewFile();
            FileWriter csvWriter = new FileWriter(f);
            csvWriter.append("location, country, time, temp, pressure, humidity, visibility, wind speed, wind degree, clouds, date\n");
            for (WeatherData data : weatherData) {
                csvWriter.append(data.getLocationName()).append(",");
                csvWriter.append(data.getCountry()).append(",");
                csvWriter.append(String.valueOf(data.getTime())).append(",");
                csvWriter.append(String.valueOf(data.getTemp())).append(",");
                csvWriter.append(String.valueOf(data.getPressure())).append(",");
                csvWriter.append(String.valueOf(data.getHumidity())).append(",");
                csvWriter.append(String.valueOf(data.getVisibility())).append(",");
                csvWriter.append(String.valueOf(data.getWindSpeed())).append(",");
                csvWriter.append(String.valueOf(data.getWindDegree())).append(",");
                csvWriter.append(String.valueOf(data.getClouds())).append(",");
                csvWriter.append(String.valueOf(data.getDate())).append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        }catch (Exception e){
            LOGGER.error("Error while downloading csv "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("CSV file saved", HttpStatus.OK);
    }

    @RequestMapping(value="/api/uploadCSV", method=RequestMethod.POST)
    public ResponseEntity<String> uploadCSV() throws IOException, ParseException {
        if(readOnly){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFileToUpload));
            String line = reader.readLine();
            ArrayList<WeatherData> dataToSave = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                String name = row[0];
                String country = row[1];
                long time = Long.parseLong(row[2]);
                float temp = Float.parseFloat(row[3]);
                float pressure = Float.parseFloat(row[4]);
                float humidity = Float.parseFloat(row[5]);
                float visibility = Float.parseFloat(row[6]);
                float windSpeed = Float.parseFloat(row[7]);
                float windDegree = Float.parseFloat(row[8]);
                float clouds = Float.parseFloat(row[9]);
                String dateStr = row[10];
                Date date = new SimpleDateFormat("EEE LLL d HH:mm:ss z yyyy", Locale.ENGLISH).parse(dateStr);
                WeatherData weatherData = new WeatherData(name, country, time, temp, pressure, humidity, visibility, windSpeed, windDegree, clouds, date);
                dataToSave.add(weatherData);
                service.createNewCity(name, country);
            }
            service.saveMultipleDocuments(dataToSave);
            reader.close();
        }catch (Exception e){
            LOGGER.error("Error while uploading csv "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("CSV file uploaded to DB", HttpStatus.OK);
    }
}
