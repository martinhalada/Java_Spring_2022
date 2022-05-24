package com.example.weatherapp.models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.Instant;
import java.util.Date;

@Document("forecast")
public class WeatherData {

    @Id
    private String id;
    private String locationName;
    private String country;

    private long time; //unix

    private float temp;
    private float pressure;
    private float humidity;
    private float visibility;
    private float windSpeed;
    private float windDegree;
    private float clouds;

    @Field
    Date date; //UTC

    public WeatherData() {
    }

    public WeatherData(String locationName, String country, long time, float temp, float pressure, float humidity, float visibility, float windSpeed, float windDegree, float clouds) {
        this.locationName = locationName;
        this.country = country;
        this.time = time;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windDegree = windDegree;
        this.clouds = clouds;
        date = Date.from(Instant.now());
    }

    public WeatherData(String locationName, String country, long time, float temp, float pressure, float humidity, float visibility, float windSpeed, float windDegree, float clouds, Date date) {
        this.locationName = locationName;
        this.country = country;
        this.time = time;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windDegree = windDegree;
        this.clouds = clouds;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCountry() {
        return country;
    }

    public long getTime() {
        return time;
    }

    public float getTemp() {
        return temp;
    }

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public float getWindDegree() {
        return windDegree;
    }

    public float getClouds() {
        return clouds;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "id='" + id + '\'' +
                ", locationName='" + locationName + '\'' +
                ", country='" + country + '\'' +
                ", time=" + time +
                ", temp=" + temp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", visibility=" + visibility +
                ", windSpeed=" + windSpeed +
                ", windDegree=" + windDegree +
                ", clouds=" + clouds +
                '}';
    }
}
