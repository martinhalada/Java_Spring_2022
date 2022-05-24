package com.example.weatherapp.models;

import javax.persistence.*;

@Entity
@Table(name="City")
@IdClass(CityId.class)
public class City{

    @Id
    @Column(name="name")
    private String name;
    @Id
    @Column(name="region")
    private String region;

    @ManyToOne
    @JoinColumn(name="state")
    private State state;

    public City() {
    }

    public City(String name, String region, State state) {
        this.name = name;
        this.region = region;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", state=" + state +
                '}';
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
