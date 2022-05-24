package com.example.weatherapp;

import com.example.weatherapp.controllers.Service;
import com.example.weatherapp.models.City;
import com.example.weatherapp.models.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan({"com.example.weatherapp.config", "com.example.weatherapp.controllers","com.example.weatherapp.models", "com.example.weatherapp.rest"})
@EntityScan("com.example.weatherapp.models")
@EnableMongoRepositories
public class WeatherAppApplication implements CommandLineRunner {

    @Autowired
    Service service;

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherAppApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WeatherAppApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Application started");
        LOGGER.info("Application started");

        State state1 = new State("česká republika", "cz");

        City city1 = new City("liberec", "cz", state1);
        City city2 = new City("ústí", "cz", state1);

        service.createNewState(state1);

        service.createNewCity(city1.getName(), city1.getRegion());
        service.createNewCity(city2.getName(), city2.getRegion());
    }
}
