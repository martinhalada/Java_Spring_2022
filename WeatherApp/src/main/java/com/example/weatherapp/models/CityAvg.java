package com.example.weatherapp.models;

public class CityAvg {
    private String locationName;
    private float avgTemp;
    private float avgPressure;
    private float avgHumidity;
    private float avgWindSpeed;

    public CityAvg(String locationName, float avgTemp, float avgPressure, float avgHumidity, float avgWindSpeed) {
        this.locationName = locationName;
        this.avgTemp = avgTemp;
        this.avgPressure = avgPressure;
        this.avgHumidity = avgHumidity;
        this.avgWindSpeed = avgWindSpeed;
    }

    public float getAvgPressure() {
        return avgPressure;
    }

    public void setAvgPressure(float avgPressure) {
        this.avgPressure = avgPressure;
    }

    public float getAvgHumidity() {
        return avgHumidity;
    }

    public void setAvgHumidity(float avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    public float getAvgWindSpeed() {
        return avgWindSpeed;
    }

    public void setAvgWindSpeed(float avgWindSpeed) {
        this.avgWindSpeed = avgWindSpeed;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public float getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(float avgTemp) {
        this.avgTemp = avgTemp;
    }
}
