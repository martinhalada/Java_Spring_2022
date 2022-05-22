package com.example.weatherapp;

import com.example.weatherapp.models.WeatherDataRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.logging.Level;

@SpringBootApplication
@ComponentScan({"com.example.weatherapp.config", "com.example.weatherapp.controllers","com.example.weatherapp.models", "com.example.weatherapp.rest"})
@EntityScan("com.example.weatherapp.models")
@EnableMongoRepositories
public class WeatherAppApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherAppApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WeatherAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception{
        System.out.println("Aplikace spuštěna");
        LOGGER.info("Application started");
    }
}
