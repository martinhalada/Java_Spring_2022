package com.example.weatherapp.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class WeatherDataRepositoryImpl {
    private final MongoTemplate mongoTemplate;
    @Value("${spring.data.mongodb.database}")
    private String collectionName;

    @Autowired
    public WeatherDataRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void save(WeatherData weatherData){
        mongoTemplate.insert(weatherData);
    }

    public void saveAll(ArrayList<WeatherData> weatherData){
        mongoTemplate.insertAll(weatherData);
    }

    public void deleteDataForCity(String name){
        Query query = new Query();
        query.addCriteria(Criteria.where("locationName").is(name));
        mongoTemplate.findAllAndRemove(query, WeatherData.class);
    }

    public void deleteAll(){
        mongoTemplate.remove(new Query(),collectionName);
    }

    public List<WeatherData> findAll(){
        return mongoTemplate.findAll(WeatherData.class);
    }

    public List<WeatherData> findByLocationName(String locationName){
        Query query = new Query();
        query.addCriteria(Criteria.where("locationName").is(locationName));
        query.with(Sort.by(Direction.DESC, "time"));
        return mongoTemplate.find(query,WeatherData.class);
    }

    public WeatherData findFirstByLocationNameAndTimeEquals(String locationName, long time){
        Query query = new Query();
        query.addCriteria(Criteria.where("locationName").is(locationName));
        query.addCriteria(Criteria.where("time").is(time));
        return mongoTemplate.findOne(query,WeatherData.class);
    }

    public ArrayList<Float> findAverageValues(String locationName, int numberOfDays){
        long time = Instant.now().getEpochSecond() - (long) numberOfDays *24*60*60;

        Criteria criteria = Criteria.where("locationName").is(locationName).and("time").gte(time);
        MatchOperation matchOperation = match(criteria);
        GroupOperation groupOperation = group("locationName")
                .avg("temp").as("avgTemp")
                .avg("pressure").as("avgPressure")
                .avg("humidity").as("avgHumidity")
                .avg("windSpeed").as("avgWindSpeed");
        ProjectionOperation projectionOperation = project("locationName", "avgTemp", "avgPressure", "avgHumidity", "avgWindSpeed");
        List<CityAvg> result = mongoTemplate.aggregate(Aggregation.newAggregation(
                matchOperation,
                groupOperation,
                projectionOperation
        ), WeatherData.class, CityAvg.class).getMappedResults();
        ArrayList<Float> avg = new ArrayList<>(
                Arrays.asList(result.get(0).getAvgTemp(),
                        result.get(0).getAvgPressure(),
                        result.get(0).getAvgHumidity(),
                        result.get(0).getAvgWindSpeed())
        );
        return avg;
    }

    public void updateWeatherDataLocation(String oldName, String newName, String code){
        Query query = new Query();
        query.addCriteria(Criteria.where("locationName").is(oldName));
        query.addCriteria(Criteria.where("country").is(code));
        Update update = new Update().set("locationName", newName);
        mongoTemplate.updateMulti(query, update, WeatherData.class);
    }

    public void updateWeatherData(WeatherData oldData, WeatherData newData){
        Query query = new Query();
        query.addCriteria(Criteria.where("locationName").is(oldData.getLocationName()));
        query.addCriteria(Criteria.where("time").is(oldData.getTime()));
        Update update = new Update().set("temp", newData.getTemp())
                .set("pressure", newData.getPressure())
                .set("humidity", newData.getHumidity())
                .set("visibility", newData.getVisibility())
                .set("windSpeed", newData.getWindSpeed())
                .set("windDegree", newData.getWindDegree())
                .set("clouds", newData.getClouds());
        mongoTemplate.findAndModify(query, update, WeatherData.class);
    }
}
