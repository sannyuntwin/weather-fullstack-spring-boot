package com.example.weatherapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Main application class for the Weather API.
 * 
 * This is the entry point of our Spring Boot application.
 * The @SpringBootApplication annotation combines three annotations:
 * 1. @Configuration - marks this class as a configuration class
 * 2. @EnableAutoConfiguration - enables Spring Boot's auto-configuration
 * 3. @ComponentScan - enables component scanning for other beans
 */
@SpringBootApplication
public class WeatherApplication {

    /**
     * Main method that starts the Spring Boot application.
     * 
     * @param args Command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        SpringApplication.run(WeatherApplication.class, args);
    }

    /**
     * Bean configuration for WebClient.
     * WebClient is used to make HTTP calls to the OpenWeather API.
     * 
     * @return Configured WebClient instance
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
