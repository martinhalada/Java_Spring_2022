package com.example.weatherapp;

import com.example.weatherapp.models.StateRepository;
import com.example.weatherapp.models.WeatherData;
import com.example.weatherapp.models.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;

@SpringBootApplication
@ComponentScan({"com.example.weatherapp.config", "com.example.weatherapp.controllers","com.example.weatherapp.models", "com.example.weatherapp.rest"})
@EntityScan("com.example.weatherapp.models")
@EnableMongoRepositories
public class WeatherAppApplication implements CommandLineRunner {
    @Autowired
    private WeatherDataRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(WeatherAppApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception{
        System.out.println("Aplikace spuštěna");
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
