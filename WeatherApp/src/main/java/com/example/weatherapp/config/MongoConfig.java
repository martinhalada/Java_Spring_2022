package com.example.weatherapp.config;

import com.example.weatherapp.models.WeatherData;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
public class MongoConfig{

    @Value("${spring.data.mongodb.uri}")
    private String mongoURI;
    @Value("${spring.data.mongodb.database}")
    private String databaseName;
    @Value("${weather.expire}")
    private int expTime;

    @Bean
    public MongoClient mongoClient(){
        ConnectionString connectionString = new ConnectionString(mongoURI);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate template = new MongoTemplate(mongoClient(),databaseName);
        template.indexOps("forecast").dropIndex("date_1");
        template
                .indexOps(WeatherData.class)
                .ensureIndex(new Index().on("date", Sort.Direction.ASC).expire(expTime));
        return template;
    }
}
