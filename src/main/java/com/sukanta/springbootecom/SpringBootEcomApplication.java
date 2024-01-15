package com.sukanta.springbootecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableMongoAuditing
@EnableMongoRepositories("com.sukanta.springbootecom.repository")
public class SpringBootEcomApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootEcomApplication.class, args);
    }

}
