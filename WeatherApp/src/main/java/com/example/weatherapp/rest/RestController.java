package com.example.weatherapp.rest;

import com.example.weatherapp.controllers.LiveWeatherService;
import com.example.weatherapp.controllers.Service;
import com.example.weatherapp.models.City;
import com.example.weatherapp.models.State;
import com.example.weatherapp.models.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    Service service;
    LiveWeatherService liveWeatherService;
    @Value("${weather.csvFileName}")
    private String csvFileName;
    @Value("${weather.csvToUpload}")
    private String csvFileToUpload;
    @Value("${weather.readOnly}")
    private boolean readOnly;

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
        if(readOnly){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        service.createNewState(state);
        return new ResponseEntity<>("State saved", HttpStatus.OK);
    }
    @RequestMapping(value = "/api/cities", method = RequestMethod.POST)
    public ResponseEntity<String> createNewCity(@RequestBody City city){
        if(readOnly){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        WeatherData weatherData = liveWeatherService.getCurrentWeather(city.getName(), city.getRegion());
        service.createWeatherData(weatherData);
        service.createNewCity(city);
        return new ResponseEntity<>("City saved", HttpStatus.OK);
    }
    @RequestMapping(value = "/api/states/{code}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteState(@PathVariable("code") String code){
        if(readOnly){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        service.deleteState(code);
        return new ResponseEntity<>("State deleted", HttpStatus.OK);
    }
    @RequestMapping(value = "/api/cities/{name}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCity(@PathVariable("name") String name){
        if(readOnly){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        service.deleteCity(name);
        return new ResponseEntity<>("City deleted", HttpStatus.OK);
    }

    @RequestMapping(value="/api/search/city/{name}/{code}", method=RequestMethod.GET)
    public ResponseEntity<List<WeatherData>> searchForCityData(@PathVariable(value = "name") String name, @PathVariable(value = "code") String code){
        if(readOnly){
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
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

    @RequestMapping(value="/api/downloadCSV", method=RequestMethod.GET)
    public ResponseEntity<String> downloadCSV() throws IOException {
        List<WeatherData> weatherData = service.getAllWeatherData();

        File f = new File(csvFileName);
        f.createNewFile();
        FileWriter csvWriter = new FileWriter(f);
        csvWriter.append("id; location; country; time; temp; pressure; humidity; visibility; wind speed; wind degree; clouds; date\n");
        for (WeatherData data : weatherData){
            csvWriter.append(data.getId()+";");
            csvWriter.append(data.getLocationName()+";");
            csvWriter.append(data.getCountry()+";");
            csvWriter.append(data.getTime() +";");
            csvWriter.append(data.getTemp() +";");
            csvWriter.append(data.getPressure() +";");
            csvWriter.append(data.getHumidity() +";");
            csvWriter.append(data.getVisibility() +";");
            csvWriter.append(data.getWindSpeed() +";");
            csvWriter.append(data.getWindDegree() +";");
            csvWriter.append(data.getClouds() +";");
            csvWriter.append(data.getDate() +"\n");
        }

        csvWriter.flush();
        csvWriter.close();

        return new ResponseEntity<>("CSV file saved", HttpStatus.OK);
    }

    @RequestMapping(value="/api/uploadCSV", method=RequestMethod.POST)
    public void uploadCSV() throws IOException, ParseException {
        if(readOnly){
            return;
        }
        BufferedReader reader = new BufferedReader(new FileReader(csvFileToUpload));
        String line;
        while((line = reader.readLine()) != null){
            String[] row = line.split(";");
            String name = row[1];
            String country = row[2];
            long time = Long.parseLong(row[3]);
            float temp = Float.parseFloat(row[4]);
            float pressure = Float.parseFloat(row[5]);
            float humidity = Float.parseFloat(row[6]);
            float visibility = Float.parseFloat(row[7]);
            float windSpeed = Float.parseFloat(row[8]);
            float windDegree = Float.parseFloat(row[9]);
            float clouds = Float.parseFloat(row[10]);
            String dateStr = row[11];
            Date date = new SimpleDateFormat("EEE LLL d HH:mm:ss z yyyy", Locale.ENGLISH).parse(dateStr);
            WeatherData weatherData = new WeatherData(name,country,time,temp,pressure,humidity,visibility,windSpeed,windDegree,clouds,date);
            service.createWeatherData(weatherData);
        }
        reader.close();
    }
}
