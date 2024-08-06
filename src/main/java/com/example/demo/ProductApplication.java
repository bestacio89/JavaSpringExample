package com.example.demo;

import Utilities.DatabaseInitializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaRepositories("Persistence.Repository")
@EntityScan("Persistence.Entity")
@EnableWebMvc
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            // Call your database initialization method
            DatabaseInitializer.createDatabase();
        };
    }
}
