package com.example.weatherapi.service;

import com.example.weatherapi.model.OpenWeatherResponse;
import com.example.weatherapi.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Service class for handling weather data operations.
 * This service calls the OpenWeather API and processes the response.
 */
@Service
public class WeatherService {

    private final WebClient webClient;
    private final String apiKey;
    private final String baseUrl;

    /**
     * Constructor for WeatherService.
     * 
     * @param webClient WebClient instance for making HTTP requests
     * @param apiKey OpenWeather API key from application.properties
     * @param baseUrl OpenWeather API base URL from application.properties
     */
    public WeatherService(WebClient webClient, 
                         @Value("${openweather.api.key}") String apiKey,
                         @Value("${openweather.api.base.url}") String baseUrl) {
        this.webClient = webClient;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    /**
     * Fetches weather data for a given city from OpenWeather API.
     * 
     * @param city The name of the city to fetch weather for
     * @return WeatherResponse object with weather information
     * @throws RuntimeException if API key is not configured or API call fails
     */
    public WeatherResponse getWeatherByCity(String city) {
        // Check if API key is configured
        if ("YOUR_API_KEY_HERE".equals(apiKey)) {
            throw new RuntimeException("OpenWeather API key is not configured. " +
                    "Please set openweather.api.key in application.properties");
        }

        try {
            // Make API call to OpenWeather
            OpenWeatherResponse openWeatherResponse = webClient.get()
                    .uri(baseUrl + "?q={city}&appid={apiKey}&units=metric", city, apiKey)
                    .retrieve()
                    .bodyToMono(OpenWeatherResponse.class)
                    .block(); // Block to wait for response (synchronous call)

            // Convert OpenWeather response to our WeatherResponse format
            return convertToWeatherResponse(openWeatherResponse);

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                throw new RuntimeException("City not found: " + city);
            } else {
                throw new RuntimeException("Failed to fetch weather data: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching weather data: " + e.getMessage());
        }
    }

    /**
     * Converts OpenWeather API response to our WeatherResponse format.
     * 
     * @param openWeatherResponse Response from OpenWeather API
     * @return Converted WeatherResponse object
     */
    private WeatherResponse convertToWeatherResponse(OpenWeatherResponse openWeatherResponse) {
        WeatherResponse response = new WeatherResponse();
        
        // Set city and country
        response.setCity(openWeatherResponse.getName());
        response.setCountry(openWeatherResponse.getSys().getCountry());
        
        // Set temperature data (convert from Kelvin to Celsius is already done by units=metric)
        response.setTemperature(openWeatherResponse.getMain().getTemp());
        response.setFeelsLike(openWeatherResponse.getMain().getFeels_like());
        response.setHumidity(openWeatherResponse.getMain().getHumidity());
        
        // Set wind speed
        response.setWindSpeed(openWeatherResponse.getWind().getSpeed());
        
        // Set weather description and icon
        if (openWeatherResponse.getWeather() != null && openWeatherResponse.getWeather().length > 0) {
            response.setDescription(openWeatherResponse.getWeather()[0].getDescription());
            response.setIcon(openWeatherResponse.getWeather()[0].getIcon());
        }
        
        return response;
    }
}
