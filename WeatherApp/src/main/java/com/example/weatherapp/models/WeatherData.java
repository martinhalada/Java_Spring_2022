package com.example.weatherapp.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.index.Indexed;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setCountry(String country) {
        this.country = country;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(float windDegree) {
        this.windDegree = windDegree;
    }

    public float getClouds() {
        return clouds;
    }

    public void setClouds(float clouds) {
        this.clouds = clouds;
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
