package com.example.weatherapi.service;

import com.example.weatherapi.model.AgricultureAdvice;
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
            WeatherResponse weatherResponse = convertToWeatherResponse(openWeatherResponse);
            
            // Generate agriculture advice based on weather conditions
            AgricultureAdvice agricultureAdvice = generateAgricultureAdvice(weatherResponse, openWeatherResponse);
            weatherResponse.setAgricultureAdvice(agricultureAdvice);
            
            return weatherResponse;

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
        
        // Set coordinates
        if (openWeatherResponse.getCoord() != null) {
            response.setLatitude(openWeatherResponse.getCoord().getLat());
            response.setLongitude(openWeatherResponse.getCoord().getLon());
        }
        
        return response;
    }

    /**
     * Generates agriculture advice based on weather conditions.
     * 
     * @param weatherResponse Processed weather response
     * @param openWeatherResponse Raw OpenWeather API response
     * @return AgricultureAdvice with farming recommendations
     */
    private AgricultureAdvice generateAgricultureAdvice(WeatherResponse weatherResponse, OpenWeatherResponse openWeatherResponse) {
        AgricultureAdvice advice = new AgricultureAdvice();
        
        // Irrigation advice based on rain and humidity
        String irrigationAdvice = generateIrrigationAdvice(openWeatherResponse, weatherResponse);
        advice.setIrrigationAdvice(irrigationAdvice);
        
        // Temperature advice
        String temperatureAdvice = generateTemperatureAdvice(weatherResponse);
        advice.setTemperatureAdvice(temperatureAdvice);
        
        // Humidity advice
        String humidityAdvice = generateHumidityAdvice(weatherResponse);
        advice.setHumidityAdvice(humidityAdvice);
        
        // General advice
        String generalAdvice = generateGeneralAdvice(openWeatherResponse, weatherResponse);
        advice.setGeneralAdvice(generalAdvice);
        
        return advice;
    }

    private String generateIrrigationAdvice(OpenWeatherResponse openWeatherResponse, WeatherResponse weatherResponse) {
        // Check if rain is expected
        if (openWeatherResponse.getWeather() != null && openWeatherResponse.getWeather().length > 0) {
            String weatherMain = openWeatherResponse.getWeather()[0].getMain().toLowerCase();
            String weatherDescription = openWeatherResponse.getWeather()[0].getDescription().toLowerCase();
            
            if (weatherMain.contains("rain") || weatherDescription.contains("rain") || 
                weatherMain.contains("drizzle") || weatherDescription.contains("drizzle")) {
                return "🌧️ Rain expected - No need to irrigate today";
            }
        }
        
        // Check humidity levels
        double humidity = weatherResponse.getHumidity();
        if (humidity > 80) {
            return "💧 High humidity - Reduce irrigation to prevent waterlogging";
        } else if (humidity < 30) {
            return "🏜️ Very low humidity - Increase irrigation frequency";
        } else {
            return "💦 Moderate conditions - Regular irrigation recommended";
        }
    }

    private String generateTemperatureAdvice(WeatherResponse weatherResponse) {
        double temperature = weatherResponse.getTemperature();
        
        if (temperature > 35) {
            return "🌡️ Very high temperature - Water crops early morning or evening to prevent evaporation loss";
        } else if (temperature > 30) {
            return "☀️ High temperature - Consider shading for sensitive crops and increase water supply";
        } else if (temperature < 10) {
            return "❄️ Low temperature - Reduce irrigation and protect sensitive crops from frost";
        } else if (temperature < 5) {
            return "🥶 Very low temperature - Frost warning - Cover crops and avoid irrigation";
        } else {
            return "🌤️ Optimal temperature - Normal watering schedule";
        }
    }

    private String generateHumidityAdvice(WeatherResponse weatherResponse) {
        double humidity = weatherResponse.getHumidity();
        
        if (humidity > 85) {
            return "🦠 High humidity - Check crops for disease risk, ensure proper air circulation";
        } else if (humidity > 70) {
            return "🍃 Elevated humidity - Monitor for fungal diseases, avoid overhead irrigation";
        } else if (humidity < 20) {
            return "🌵 Very low humidity - Risk of plant stress, consider mulching to retain moisture";
        } else {
            return "🌿 Normal humidity - Favorable conditions for most crops";
        }
    }

    private String generateGeneralAdvice(OpenWeatherResponse openWeatherResponse, WeatherResponse weatherResponse) {
        StringBuilder generalAdvice = new StringBuilder();
        
        // Wind conditions
        double windSpeed = weatherResponse.getWindSpeed();
        if (windSpeed > 15) {
            generalAdvice.append("💨 Strong winds - Secure loose items, consider wind protection for crops. ");
        } else if (windSpeed > 8) {
            generalAdvice.append("🍃 Moderate winds - Good for pollination, monitor for wind damage. ");
        }
        
        // Weather conditions
        if (openWeatherResponse.getWeather() != null && openWeatherResponse.getWeather().length > 0) {
            String weatherMain = openWeatherResponse.getWeather()[0].getMain().toLowerCase();
            
            if (weatherMain.contains("clear")) {
                generalAdvice.append("☀️ Clear skies - Optimal conditions for field work and spraying. ");
            } else if (weatherMain.contains("clouds")) {
                generalAdvice.append("☁️ Cloudy conditions - Reduced evaporation, good for transplanting. ");
            } else if (weatherMain.contains("snow")) {
                generalAdvice.append("❄️ Snow expected - Protect winter crops, delay field operations. ");
            }
        }
        
        // Temperature considerations
        double temp = weatherResponse.getTemperature();
        if (temp >= 15 && temp <= 30) {
            generalAdvice.append("🌱 Optimal growing conditions - Excellent for most crop activities.");
        }
        
        return generalAdvice.length() > 0 ? generalAdvice.toString() : "📊 Monitor conditions and adjust farming practices accordingly.";
    }
}
