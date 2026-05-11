package com.example.weatherapi.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ndvi_history")
public class NDVIHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long fieldId;
    private LocalDate date;
    private Double ndviValue;
    private Double precipitation;

    public NDVIHistory() {}

    public NDVIHistory(Long fieldId, LocalDate date, Double ndviValue, Double precipitation) {
        this.fieldId = fieldId;
        this.date = date;
        this.ndviValue = ndviValue;
        this.precipitation = precipitation;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFieldId() { return fieldId; }
    public void setFieldId(Long fieldId) { this.fieldId = fieldId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Double getNdviValue() { return ndviValue; }
    public void setNdviValue(Double ndviValue) { this.ndviValue = ndviValue; }
    public Double getPrecipitation() { return precipitation; }
    public void setPrecipitation(Double precipitation) { this.precipitation = precipitation; }
}
