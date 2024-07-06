package com.example.demo;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// src/test/java/com/example/demo/config/TestJpaConfig.java
@Configuration
@EntityScan("Core.Entity") // Replace with your entity package
@EnableJpaRepositories("Persistence.Repository") // Replace with your repository package
public class TestJpaConfig {
    // Additional test-specific configurations if needed
}

