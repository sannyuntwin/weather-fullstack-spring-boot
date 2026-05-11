package com.example.weatherapi.controller;

import com.example.weatherapi.model.Field;
import com.example.weatherapi.model.NDVIHistory;
import com.example.weatherapi.repository.FieldRepository;
import com.example.weatherapi.repository.NDVIHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
public class FieldController {

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private NDVIHistoryRepository ndviRepository;

    @GetMapping
    public List<Field> getAllFields() {
        return fieldRepository.findAll();
    }

    @GetMapping("/{id}/analytics")
    public List<NDVIHistory> getFieldAnalytics(@PathVariable Long id) {
        return ndviRepository.findByFieldIdOrderByDateAsc(id);
    }
}
