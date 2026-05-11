package com.example.weatherapi.controller;

import com.example.weatherapi.model.WeatherResponse;
import com.example.weatherapi.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling weather-related requests.
 * 
 * The @RestController annotation combines @Controller and @ResponseBody,
 * which means all methods in this class will return JSON responses by default.
 * This controller handles HTTP requests to our weather API endpoints.
 */
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    /**
     * Constructor for WeatherController with dependency injection.
     * 
     * @param weatherService Service for handling weather operations
     */
    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * GET endpoint to retrieve weather information for a specific city.
     * 
     * @param city The name of the city (extracted from the URL path)
     * @return ResponseEntity containing WeatherResponse or error message
     * 
     * Example: GET /api/weather/NewYork -> JSON response with weather data
     */
    @GetMapping("/{city}")
    public ResponseEntity<?> getWeather(@PathVariable String city) {
        try {
            // Call the weather service to fetch data
            WeatherResponse weatherResponse = weatherService.getWeatherByCity(city);
            
            // Return successful response with weather data
            return ResponseEntity.ok(weatherResponse);
            
        } catch (RuntimeException e) {
            // Handle errors and return appropriate HTTP status codes
            if (e.getMessage().contains("City not found")) {
                // Return 404 Not Found for invalid cities
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("City not found: " + city));
            } else if (e.getMessage().contains("API key is not configured")) {
                // Return 500 Internal Server Error for missing API key
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("OpenWeather API key is not configured"));
            } else {
                // Return 500 Internal Server Error for other errors
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("Failed to fetch weather data: " + e.getMessage()));
            }
        }
    }

    /**
     * Error response class for returning error messages in JSON format.
     */
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
