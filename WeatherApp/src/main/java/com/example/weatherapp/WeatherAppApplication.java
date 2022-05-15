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
    @Autowired
    private WeatherDataRepositoryImpl repository;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherAppApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WeatherAppApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception{
        System.out.println("Aplikace spuštěna");
        LOGGER.info("Application started");
        /**
        //repository.deleteAll();

        repository.save(new WeatherData("Liberec","CZ", 1650113100, 7,1030,60,100,2,60,5));
        repository.save(new WeatherData("Praha","CZ", 1650113106, 8,1034,62,104,3,67,4));

        System.out.println("findAll záznamy");
        for (WeatherData data : repository.findAll()){
            System.out.println(data);
        }
        System.out.println();

        System.out.println("find Liberec");
        for(WeatherData data : repository.findByLocationName("Liberec")){
            System.out.println(data);
        }
        System.out.println();
        **/

    }

}
