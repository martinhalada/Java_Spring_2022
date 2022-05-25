package com.example.weatherapp.rest;

import com.example.weatherapp.controllers.LiveWeatherService;
import com.example.weatherapp.controllers.Service;
import com.example.weatherapp.models.City;
import com.example.weatherapp.models.State;
import com.example.weatherapp.models.WeatherData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
@EnableScheduling
public class RestController {

    Service service;
    LiveWeatherService liveWeatherService;
    ThreadPoolTaskScheduler threadPoolTaskScheduler;
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
            LOGGER.info("Read only mode is set");
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
                    LOGGER.warn("Maximum API calls reached, not all cities data were updated. Delete a few cities.");
                    return;
                }
                WeatherData weatherData = liveWeatherService.getCurrentWeather(city.getName(), city.getRegion());
                numberOfRequests++;
                if(weatherData==null){
                    LOGGER.error("Request fails because of a server error response");
                    return;
                }
                service.createWeatherData(weatherData);
            }
            LOGGER.info("New data downloaded");
        }catch (Exception e){
            LOGGER.error("Error while downloading new data "+ e.getMessage());
        }
    }

    @RequestMapping(value = "/api/states", method = RequestMethod.GET)
    public ResponseEntity<List<State>> getAllStates(){
        try {
            List<State> states = service.getAllStates();
            return new ResponseEntity<>(states, HttpStatus.OK);
        }catch(Exception e){
            LOGGER.error("Error while getting data from DB: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/api/cities", method = RequestMethod.GET)
    public ResponseEntity<List<City>> getAllCities(){
        try{
            List<City> cities = service.getCities();
            return new ResponseEntity<>(cities, HttpStatus.OK);
        }catch(Exception e){
            LOGGER.error("Error while getting data from DB: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/api/states", method = RequestMethod.POST)
    public ResponseEntity<String> createNewState(@RequestBody State state){
        if(readOnly){
            LOGGER.warn("Cannot create new state, because of read only mode");
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if(state.getName().isEmpty() || state.getCode().isEmpty()){
            LOGGER.warn("Invalid request body");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!service.isInputValid(state.getName().toLowerCase()) || !service.isInputValid(state.getCode().toLowerCase())){
            LOGGER.warn("Invalid state parameters");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        service.createNewState(state);
        return new ResponseEntity<>("State saved", HttpStatus.OK);
    }

    @RequestMapping(value = "/api/cities", method = RequestMethod.POST)
    public ResponseEntity<String> createNewCity(@RequestBody City city){
        if(readOnly){
            LOGGER.warn("Cannot create new city, because of read only mode");
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if(city.getName().isEmpty() || city.getRegion().isEmpty() || city.getState()==null){
            LOGGER.warn("Invalid request body");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!service.isInputValid(city.getName().toLowerCase()) || !service.isInputValid(city.getRegion().toLowerCase())){
            LOGGER.warn("Invalid city parameters");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            if(numberOfRequests >= apiLimit){
                LOGGER.warn("Maximum API calls reached, not all cities data were updated. Delete a few cities.");
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }
            WeatherData weatherData = liveWeatherService.getCurrentWeather(city.getName().toLowerCase(), city.getRegion().toLowerCase());
            numberOfRequests++;
            if(weatherData==null){
                LOGGER.error("Request fails because of a server error response");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            service.createWeatherData(weatherData);
            service.createNewCity(city.getName().toLowerCase(), city.getRegion().toLowerCase());
        }catch (Exception e){
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("City saved", HttpStatus.OK);
    }

    @RequestMapping(value = "/api/states/{code}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteState(@PathVariable("code") String code){
        if(readOnly){
            LOGGER.warn("Cannot delete state, because of read only mode");
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if(!service.isInputValid(code.toLowerCase())){
            LOGGER.warn("Invalid state code");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        service.deleteState(code.toLowerCase());
        return new ResponseEntity<>("State deleted", HttpStatus.OK);
    }

    @RequestMapping(value = "/api/cities/{name}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCity(@PathVariable("name") String name){
        if(readOnly){
            LOGGER.warn("Cannot delete city, because of read only mode");
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if(!service.isInputValid(name.toLowerCase())){
            LOGGER.warn("Invalid city name");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        service.deleteCity(name.toLowerCase());
        return new ResponseEntity<>("City deleted", HttpStatus.OK);
    }

    @RequestMapping(value="/api/search/city/{name}/{code}", method=RequestMethod.GET)
    public ResponseEntity<List<WeatherData>> searchForCityData(@PathVariable(value = "name") String name, @PathVariable(value = "code") String code){
        if(readOnly){
            LOGGER.warn("Cannot search for city data, because of read only mode");
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if(!service.isInputValid(name.toLowerCase()) || !service.isInputValid(code.toLowerCase())){
            LOGGER.warn("Invalid parameters");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            if(numberOfRequests >= apiLimit){
                LOGGER.warn("Maximum API calls reached, not all cities data were updated. Delete a few cities.");
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }
            WeatherData weatherData = liveWeatherService.getCurrentWeather(name.toLowerCase(), code.toLowerCase());
            numberOfRequests++;
            if(weatherData==null){
                LOGGER.error("Request fails because of a server error response");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            service.createWeatherData(weatherData);
            service.createNewCity(name.toLowerCase(), code.toLowerCase());
        }catch (HttpClientErrorException httpClientErrorException){
            LOGGER.warn("City:"+name+", "+code+" "+" not found "+httpClientErrorException.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (ResourceAccessException resourceAccessException) {
            LOGGER.error("No internet connection "+resourceAccessException.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<WeatherData> savedData = service.getWeatherData(name.toLowerCase());
        return new ResponseEntity<>(savedData, HttpStatus.OK);
    }

    @RequestMapping(value="/api/search/state/{code}", method=RequestMethod.GET)
    public ResponseEntity<List<List<WeatherData>>> searchForStateData(@PathVariable(value = "code") String code){
        if(!service.isInputValid(code.toLowerCase())){
            LOGGER.warn("Invalid state code");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<List<WeatherData>> dataForState = service.getCitiesAndDataForState(code.toLowerCase());
        return new ResponseEntity<>(dataForState, HttpStatus.OK);
    }

    @RequestMapping(value="/api/avg/{name}", method=RequestMethod.GET)
    public ResponseEntity<List<List<Float>>> searchForAvg(@PathVariable(value = "name") String name){
        if(!service.isInputValid(name.toLowerCase())){
            LOGGER.warn("Invalid city name");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            List<Float> avgValuesDay = service.getAvgValues(name.toLowerCase(), 1);
            List<Float> avgValuesWeek = service.getAvgValues(name.toLowerCase(), 7);
            List<Float> avgValues2Weeks = service.getAvgValues(name.toLowerCase(), 14);
            List<List<Float>> avg = new ArrayList<>(
                    Arrays.asList(avgValuesDay, avgValuesWeek, avgValues2Weeks));
            return new ResponseEntity<>(avg, HttpStatus.OK);
        }catch (Exception e){
            LOGGER.warn("Error while getting avg values, check if city exist and if has some data: "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/api/updateState", method=RequestMethod.POST)
    public ResponseEntity<String> updateState(@RequestBody List<String> data){
        if(readOnly){
            LOGGER.warn("Cannot update state name, because of read only mode");
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        service.updateState(data);
        return new ResponseEntity<>("State updated", HttpStatus.OK);
    }

    @RequestMapping(value="/api/updateCity", method=RequestMethod.POST)
    public ResponseEntity<String> updateCity(@RequestBody List<String> data){
        if(readOnly){
            LOGGER.warn("Cannot update city, because of read only mode");
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        try{
            String newName = data.get(1).toLowerCase();
            String code = data.get(2).toLowerCase();
            if(numberOfRequests >= apiLimit){
                LOGGER.warn("Maximum API calls reached, not all cities data were updated. Delete a few cities.");
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }
            WeatherData weatherData = liveWeatherService.getCurrentWeather(newName, code);
            numberOfRequests++;
            if(weatherData==null){
                LOGGER.error("Request fails because of a server error response");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            LOGGER.warn("New City not found: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        service.updateCity(data);
        return new ResponseEntity<>("City updated", HttpStatus.OK);
    }

    @RequestMapping(value="/api/downloadCSV", method=RequestMethod.GET)
    public ResponseEntity<Resource> downloadCSV(@RequestBody String fileName){
        List<WeatherData> weatherData = service.getAllWeatherData();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT);

            csvPrinter.printRecord(Arrays.asList(
                    "location", "country", "time", "temp", "pressure",
                    "humidity", "visibility", "wind speed", "wind degree",
                    "clouds", "date"));
            for (WeatherData data : weatherData) {
                csvPrinter.printRecord(Arrays.asList(
                data.getLocationName(),
                data.getCountry(),
                String.valueOf(data.getTime()),
                String.valueOf(data.getTemp()),
                String.valueOf(data.getPressure()),
                String.valueOf(data.getHumidity()),
                String.valueOf(data.getVisibility()),
                String.valueOf(data.getWindSpeed()),
                String.valueOf(data.getWindDegree()),
                String.valueOf(data.getClouds()),
                String.valueOf(data.getDate())));
            }
            csvPrinter.flush();
            csvPrinter.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
            InputStreamResource file = new InputStreamResource(inputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .body(file);
        }catch (Exception e){
            LOGGER.error("Error while downloading csv: "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/api/uploadCSV", method=RequestMethod.POST)
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file){
        if(readOnly){
            LOGGER.warn("Cannot upload CSV file, because of read only mode");
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if(file.getContentType()==null){
            LOGGER.warn("CSV file in request not found.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!file.getContentType().equals("text/csv")){
            LOGGER.warn("File is not CSV, cannot upload.");
            return new ResponseEntity<>("File is not CSV, cannot upload.", HttpStatus.BAD_REQUEST);
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "utf-8"));
            String line = reader.readLine();
            ArrayList<WeatherData> dataToSave = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                String name = row[0].toLowerCase();
                String country = row[1].toLowerCase();
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
            LOGGER.error("Error while uploading csv: "+ e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("CSV file uploaded to DB", HttpStatus.OK);
    }
}
