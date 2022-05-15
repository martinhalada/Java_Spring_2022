package com.example.weatherapp.controllers;

import com.example.weatherapp.models.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@org.springframework.stereotype.Service
public class Service {
    private final WeatherDataRepositoryImpl weatherDataRepositoryImpl;
    private final StateRepositoryImpl stateRepositoryImpl;
    private final CityRepositoryImpl cityRepositoryImpl;

    @Autowired
    public Service(WeatherDataRepositoryImpl weatherDataRepositoryImpl, StateRepositoryImpl stateRepositoryImpl, CityRepositoryImpl cityRepositoryImpl){
        this.weatherDataRepositoryImpl = weatherDataRepositoryImpl;
        this.stateRepositoryImpl = stateRepositoryImpl;
        this.cityRepositoryImpl = cityRepositoryImpl;
    }

    public void createNewState(State newState){
        /** Vytvoří nový stát */
        //if (getState(newState.getCode()) == null) {
        if (!existsState(newState.getCode())){
            stateRepositoryImpl.save(newState);
        }
    }
    public List<State> getAllStates(){
        /** Vrátí seznam všech států */
        return stateRepositoryImpl.findAll();
    }

    public State getState(String code){
        /** vrátí stát */
        return stateRepositoryImpl.findStateByCode(code);
    }

    public List<List<WeatherData>> getCitiesAndDataForState(String code){
        /** pro daný stát, vrátí pro každé jeho město jeho naměřená data */
        List<City> cities = getCitiesForState(code);
        List<List<WeatherData>> lists = new ArrayList<>();
        for(City city : cities){
            List<WeatherData> weatherData = getWeatherData(city.getName());
            lists.add(weatherData);
        }
        return lists;
    }
    public void deleteStates(){
        /** smaže úplně všechna data */
        weatherDataRepositoryImpl.deleteAll();
        cityRepositoryImpl.deleteAll();
        stateRepositoryImpl.deleteAll();
    }
    @Transactional
    public void deleteState(String code){
        /** Smaže stát, jeho všechny města, a měření těchto měst */
        List<City> cities = getCitiesForState(code);
        for(City city : cities){
            deleteCity(city.getName());
        }
        stateRepositoryImpl.deleteStateByCode(code);
    }

    public void createNewCity(String name, String stateCode){
        /** Vytvoří nové město, případně i stát, pokud neexistuje */
        if (existsCity(name)){
            return;
        }
        if (!existsState(stateCode)){
            State state = new State(stateCode);
            createNewState(state);
        }
        State state = getState(stateCode);
        City city = new City(name, stateCode, state);
        cityRepositoryImpl.save(city);
    }
    public void createNewCity(City city){
        createNewCity(city.getName(), city.getRegion());
    }

    public List<City> getCities(){
        /** vrátí seznam všech měst */
        return cityRepositoryImpl.findAll();
    }
    public List<City> getCitiesForState(String stateCode){
        return cityRepositoryImpl.findCitiesByStateCode(stateCode);
    }
    public List<WeatherData> getDataForCity(String name){
        /** vrátí všechna měření pro dané město */
        return getWeatherData(name);
    }

    public boolean existsCity(String name){
        return cityRepositoryImpl.existsCity(name);
    }
    public boolean existsState(String code){
        return stateRepositoryImpl.existsState(code);
    }

    public void deleteCities(){
        /** smaže všechna města a tím pádem i všechna měření */
        weatherDataRepositoryImpl.deleteAll();
        cityRepositoryImpl.deleteAll();
    }
    @Transactional
    public void deleteCity(String name){
        /** smaže město a jeho všechna měření */
        List<WeatherData> weatherData = getWeatherData(name);
        for(WeatherData data : weatherData){
            deleteWeatherData(data);
        }
        cityRepositoryImpl.deleteCityByName(name);
    }

    public void createWeatherData(WeatherData weatherData){
        /** uloží nové měření */
        String cityName = weatherData.getLocationName();
        long newTime = weatherData.getTime();
        WeatherData savedData = weatherDataRepositoryImpl.findFirstByLocationNameAndTimeEquals(cityName, newTime);
        if (savedData!=null){
            return;
        }
        weatherDataRepositoryImpl.save(weatherData);
    }
    public void deleteWeatherData(WeatherData weatherData){
        /** smaže měření */
        weatherDataRepositoryImpl.delete(weatherData);
    }
    public List<WeatherData> getWeatherData(String city){
        /** vrátí měření pro dané město */
        return weatherDataRepositoryImpl.findByLocationName(city);
    }
    public List<WeatherData> getAllWeatherData(){
        /** vrátí všechna měření */
        return weatherDataRepositoryImpl.findAll();
    }

    public boolean isInputValid(String text){
        if (!text.matches("[a-žA-Ž ]+")){
            return false;
        }
        return true;
    }
}
