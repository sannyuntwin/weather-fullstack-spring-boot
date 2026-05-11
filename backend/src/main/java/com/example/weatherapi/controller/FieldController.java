package com.example.weatherapi.controller;

import com.example.weatherapi.model.Field;
import com.example.weatherapi.model.NDVIHistory;
import com.example.weatherapi.repository.FieldRepository;
import com.example.weatherapi.repository.NDVIHistoryRepository;
import com.example.weatherapi.service.AgromonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
@CrossOrigin(origins = "http://localhost:5173")
public class FieldController {

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private NDVIHistoryRepository ndviRepository;

    @Autowired
    private AgromonitoringService agroService;

    @GetMapping
    public List<Field> getAllFields() {
        return fieldRepository.findAll();
    }

    @GetMapping("/{id}/analytics")
    public List<NDVIHistory> getFieldAnalytics(@PathVariable Long id) {
        Field field = fieldRepository.findById(id).orElse(null);
        if (field == null) return null;

        // If no polygon ID, register it with the satellite service now
        if (field.getPolygonId() == null) {
            String polyId = agroService.createPolygon(field);
            if (polyId != null) {
                field.setPolygonId(polyId);
                fieldRepository.save(field);
            }
        }

        // Try to get real satellite data from Agromonitoring
        if (field.getPolygonId() != null) {
            List<NDVIHistory> realData = agroService.getNDVIHistory(id, field.getPolygonId());
            if (!realData.isEmpty()) return realData;
        }

        // Fallback to seeded database data if satellite API fails or no images are available
        return ndviRepository.findByFieldIdOrderByDateAsc(id);
    }
}
