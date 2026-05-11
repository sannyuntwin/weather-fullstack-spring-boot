package com.example.weatherapi.controller;

import com.example.weatherapi.model.WeatherResponse;
import com.example.weatherapi.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "http://localhost:5173")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Specific coordinate endpoint must come BEFORE the generic city path variable
     */
    @GetMapping("/coords")
    public ResponseEntity<?> getWeatherByCoords(@RequestParam double lat, @RequestParam double lon) {
        try {
            WeatherResponse weatherResponse = weatherService.getWeatherByCoordinates(lat, lon);
            return ResponseEntity.ok(weatherResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to fetch weather for coordinates: " + e.getMessage()));
        }
    }

    @GetMapping("/{city}")
    public ResponseEntity<?> getWeather(@PathVariable String city) {
        try {
            WeatherResponse weatherResponse = weatherService.getWeatherByCity(city);
            return ResponseEntity.ok(weatherResponse);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("City not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("City not found: " + city));
            } else if (e.getMessage().contains("API key is not configured")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("OpenWeather API key is not configured"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("Failed to fetch weather data: " + e.getMessage()));
            }
        }
    }

    public static class ErrorResponse {
        private String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
