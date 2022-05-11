package com.example.weatherapp.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WeatherDataRepositoryImpl {
    @Autowired
    private final MongoTemplate mongoTemplate;
    @Value("${spring.data.mongodb.database}")
    private String collectionName;

    public WeatherDataRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void save(WeatherData weatherData){
        mongoTemplate.insert(weatherData);
    }
    public void deleteAll(){
        mongoTemplate.remove(new Query(),collectionName);
    }
    public void delete(WeatherData weatherData){
        mongoTemplate.remove(weatherData);
    }
    public List<WeatherData> findAll(){
        return mongoTemplate.findAll(WeatherData.class);
    }
    public List<WeatherData> findByLocationName(String locationName){
        Query query = new Query();
        query.addCriteria(Criteria.where("locationName").is(locationName));
        return mongoTemplate.find(query,WeatherData.class);
    }
    public WeatherData findFirstByLocationNameAndTimeEquals(String locationName, long time){
        Query query = new Query();
        query.addCriteria(Criteria.where("locationName").is(locationName));
        query.addCriteria(Criteria.where("time").is(time));
        return mongoTemplate.findOne(query,WeatherData.class);
    }
}
