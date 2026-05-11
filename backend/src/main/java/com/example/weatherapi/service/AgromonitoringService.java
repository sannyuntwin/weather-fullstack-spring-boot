package com.example.weatherapi.service;

import com.example.weatherapi.model.Field;
import com.example.weatherapi.model.NDVIHistory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AgromonitoringService {

    private final WebClient webClient;
    private final String apiKey;
    private final String baseUrl;

    public AgromonitoringService(WebClient webClient,
                                @Value("${openweather.api.key}") String apiKey,
                                @Value("${agromonitoring.api.base.url}") String baseUrl) {
        this.webClient = webClient;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    /**
     * Registers a new polygon for a field if it doesn't exist.
     */
    public String createPolygon(Field field) {
        double lat = field.getLatitude();
        double lon = field.getLongitude();
        double delta = 0.005; // ~500m square

        // Create a simple square GeoJSON polygon around the center point
        Map<String, Object> geoJson = Map.of(
            "type", "Feature",
            "properties", Map.of(),
            "geometry", Map.of(
                "type", "Polygon",
                "coordinates", List.of(List.of(
                    List.of(lon - delta, lat - delta),
                    List.of(lon + delta, lat - delta),
                    List.of(lon + delta, lat + delta),
                    List.of(lon - delta, lat + delta),
                    List.of(lon - delta, lat - delta)
                ))
            )
        );

        Map<String, Object> request = Map.of(
            "name", field.getName(),
            "geo_json", geoJson
        );

        try {
            Map response = webClient.post()
                .uri(baseUrl + "/polygons?appid=" + apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

            return (String) response.get("id");
        } catch (Exception e) {
            System.err.println("Failed to create polygon: " + e.getMessage());
            return null;
        }
    }

    /**
     * Fetches historical NDVI data for a registered polygon.
     */
    public List<NDVIHistory> getNDVIHistory(Long fieldId, String polygonId) {
        long end = Instant.now().getEpochSecond();
        long start = end - (365L * 24 * 60 * 60); // 1 year ago

        try {
            List<Map> response = webClient.get()
                .uri(baseUrl + "/ndvi/history?polyid={polyId}&start={start}&end={end}&appid={apiKey}", 
                     polygonId, start, end, apiKey)
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();

            List<NDVIHistory> history = new ArrayList<>();
            for (Map entry : response) {
                long dt = ((Number) entry.get("dt")).longValue();
                Map data = (Map) entry.get("data");
                double ndvi = ((Number) data.get("mean")).doubleValue();

                history.add(new NDVIHistory(
                    fieldId, 
                    Instant.ofEpochSecond(dt).atZone(ZoneId.systemDefault()).toLocalDate(),
                    ndvi,
                    0.0 // Precipitation isn't in this specific endpoint
                ));
            }
            return history;
        } catch (Exception e) {
            System.err.println("Failed to fetch satellite NDVI: " + e.getMessage());
            return new ArrayList<>(); // Fallback to empty/mock if API fails
        }
    }
}
