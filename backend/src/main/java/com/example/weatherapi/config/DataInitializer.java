package com.example.weatherapi.config;

import com.example.weatherapi.model.Field;
import com.example.weatherapi.model.NDVIHistory;
import com.example.weatherapi.repository.FieldRepository;
import com.example.weatherapi.repository.NDVIHistoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(FieldRepository fieldRepository, NDVIHistoryRepository ndviRepository) {
        return args -> {
            // Seed a real field (example: Campo Grande area)
            Field field1 = new Field("Strategic Plot A1", 332.5, -26.6190, -55.1699);
            fieldRepository.save(field1);

            // Seed NDVI History for the last 12 months
            LocalDate startDate = LocalDate.now().minusMonths(12);
            for (int i = 0; i < 12; i++) {
                LocalDate date = startDate.plusMonths(i);
                double ndvi = 0.4 + Math.random() * 0.4; // NDVI between 0.4 and 0.8
                double precip = 10 + Math.random() * 100; // Precip between 10mm and 110mm
                ndviRepository.save(new NDVIHistory(field1.getId(), date, ndvi, precip));
            }

            System.out.println("✅ Database seeded with Strategic Field data");
        };
    }
}
