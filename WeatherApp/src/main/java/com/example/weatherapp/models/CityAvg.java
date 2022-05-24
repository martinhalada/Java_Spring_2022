package com.example.weatherapp.models;

public class CityAvg {
    private final String locationName;
    private final float avgTemp;
    private final float avgPressure;
    private final float avgHumidity;
    private final float avgWindSpeed;

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

    public float getAvgHumidity() {
        return avgHumidity;
    }

    public float getAvgWindSpeed() {
        return avgWindSpeed;
    }

    public float getAvgTemp() {
        return avgTemp;
    }

}
