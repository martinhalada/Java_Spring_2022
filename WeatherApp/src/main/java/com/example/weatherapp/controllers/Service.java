package com.example.weatherapp.controllers;

import com.example.weatherapp.models.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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
        if (!existsState(newState.getCode().toLowerCase())){
            stateRepositoryImpl.save(newState);
        }
    }

    public void createNewCity(String name, String stateCode){
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

    public void createWeatherData(WeatherData weatherData){
        String cityName = weatherData.getLocationName().toLowerCase();
        long newTime = weatherData.getTime();
        WeatherData savedData = weatherDataRepositoryImpl.findFirstByLocationNameAndTimeEquals(cityName, newTime);
        if (savedData!=null){
            return;
        }
        weatherDataRepositoryImpl.save(weatherData);
    }

    public void saveMultipleDocuments(ArrayList<WeatherData> weatherData){
        weatherDataRepositoryImpl.saveAll(weatherData);
    }

    public void deleteStates(){
        weatherDataRepositoryImpl.deleteAll();
        cityRepositoryImpl.deleteAll();
        stateRepositoryImpl.deleteAll();
    }

    @Transactional
    public void deleteState(String code){
        List<City> cities = getCitiesForState(code);
        for(City city : cities){
            deleteCity(city.getName());
        }
        stateRepositoryImpl.deleteStateByCode(code);
    }

    @Transactional
    public void deleteCity(String name){
        weatherDataRepositoryImpl.deleteDataForCity(name);
        cityRepositoryImpl.deleteCityByName(name);
    }

    public List<State> getAllStates(){
        return stateRepositoryImpl.findAll();
    }

    public State getState(String code){
        return stateRepositoryImpl.findStateByCode(code);
    }

    public List<City> getCities(){
        return cityRepositoryImpl.findAll();
    }

    public List<City> getCitiesForState(String stateCode){
        return cityRepositoryImpl.findCitiesByStateCode(stateCode);
    }

    public List<List<WeatherData>> getCitiesAndDataForState(String code){
        List<City> cities = getCitiesForState(code);
        List<List<WeatherData>> lists = new ArrayList<>();
        for(City city : cities){
            List<WeatherData> weatherData = getWeatherData(city.getName());
            lists.add(weatherData);
        }
        return lists;
    }

    public List<WeatherData> getWeatherData(String city){
        return weatherDataRepositoryImpl.findByLocationName(city);
    }

    public List<WeatherData> getAllWeatherData(){
        return weatherDataRepositoryImpl.findAll();
    }

    public List<Float> getAvgValues(String name, int days){
        return weatherDataRepositoryImpl.findAverageValues(name, days);
    }

    public boolean existsCity(String name){
        return cityRepositoryImpl.existsCity(name);
    }

    public boolean existsState(String code){
        return stateRepositoryImpl.existsState(code);
    }

    public boolean isInputValid(String text){
        return text.matches("[a-žA-Ž ]+");
    }

    public void updateState(List<String> data){
        String newValue = data.get(0).toLowerCase();
        String code = data.get(1).toLowerCase();
        stateRepositoryImpl.updateState(newValue, code);
    }
    public void updateCity(List<String> data){
        String oldName = data.get(0).toLowerCase();
        String newName = data.get(1).toLowerCase();
        String code = data.get(2).toLowerCase();
        boolean updated = cityRepositoryImpl.updateCity(oldName, newName, code);
        if(updated){
            weatherDataRepositoryImpl.updateWeatherDataLocation(oldName, newName, code);
        }
    }
    public void updateWeatherData(List<WeatherData> data){
        WeatherData oldData = data.get(0);
        WeatherData newData = data.get(1);
        weatherDataRepositoryImpl.updateWeatherData(oldData, newData);
    }
}
