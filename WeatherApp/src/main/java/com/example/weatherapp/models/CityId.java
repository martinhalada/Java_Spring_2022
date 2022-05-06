package com.example.weatherapp.models;

import java.io.Serializable;
import java.util.Objects;

public class CityId implements Serializable {
    private String name;
    private String region;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityId cityId = (CityId) o;
        return Objects.equals(name, cityId.name) && Objects.equals(region, cityId.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, region);
    }

    public CityId(String name, String region) {
        this.name = name;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
