package com.example.weatherapp.models;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherDataRepository extends MongoRepository<WeatherData, String> {

    public List<WeatherData> findByLocationName(String locationName);
}
