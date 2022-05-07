package com.example.weatherapp.controllers;

import com.example.weatherapp.models.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {
    private final WeatherDataRepository weatherDataRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    @Autowired
    public Service(WeatherDataRepository weatherDataRepository, StateRepository stateRepository, CityRepository cityRepository){
        this.weatherDataRepository=weatherDataRepository;
        this.stateRepository=stateRepository;
        this.cityRepository=cityRepository;
    }

    public void createNewState(State newState){
        /** Vytvoří nový stát */
        stateRepository.save(newState);
    }
    public List<State> getAllStates(){
        /** Vrátí seznam všech států */
        return stateRepository.findAll();
    }

    public State getState(String code){
        /** vrátí stát */
        return stateRepository.findStateByCode(code);
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
        weatherDataRepository.deleteAll();
        cityRepository.deleteAll();
        stateRepository.deleteAll();
    }
    @Transactional
    public void deleteState(String code){
        /** Smaže stát, jeho všechny města, a měření těchto měst */
        List<City> cities = getCitiesForState(code);
        for(City city : cities){
            deleteCity(city.getName());
        }
        stateRepository.deleteStateByCode(code);
    }

    public void createNewCity(String name, String stateCode){
        /** Vytvoří nové město, případně i stát, pokud neexistuje */
        if (existsCity(name)){
            return;
        }
        if (getState(stateCode) == null){
            State state = new State(stateCode);
            createNewState(state);
        }
        State state = getState(stateCode);
        City city = new City(name, stateCode, state);
        cityRepository.save(city);
    }

    public List<City> getCities(){
        /** vrátí seznam všech měst */
        return cityRepository.findAll();
    }
    public List<City> getCitiesForState(String stateCode){
        return cityRepository.findCitiesByStateCode(stateCode);
    }
    public List<WeatherData> getDataForCity(String name){
        /** vrátí všechna měření pro dané město */
        return getWeatherData(name);
    }

    public boolean existsCity(String name){
        return cityRepository.existsCity(name);
    }

    public void deleteCities(){
        /** smaže všechna města a tím pádem i všechna měření */
        weatherDataRepository.deleteAll();
        cityRepository.deleteAll();
    }
    @Transactional
    public void deleteCity(String name){
        /** smaže město a jeho všechna měření */
        List<WeatherData> weatherData = getWeatherData(name);
        for(WeatherData data : weatherData){
            deleteWeatherData(data);
        }
        cityRepository.deleteCityByName(name);
    }

    public void createWeatherData(WeatherData weatherData){
        /** uloží nové měření */
        String cityName = weatherData.getLocationName();
        long newTime = weatherData.getTime();
        WeatherData savedData = weatherDataRepository.findFirstByLocationNameAndTimeEquals(cityName, newTime);
        if (savedData!=null){
            return;
        }
        weatherDataRepository.save(weatherData);
    }
    public void deleteWeatherData(WeatherData weatherData){
        /** smaže měření */
        weatherDataRepository.delete(weatherData);
    }
    public List<WeatherData> getWeatherData(String city){
        /** vrátí měření pro dané město */
        return weatherDataRepository.findByLocationName(city);
    }
}
