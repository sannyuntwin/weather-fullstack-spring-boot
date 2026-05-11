package com.example.weatherapi.model;

public class AgricultureAdvice {
    private String irrigationAdvice;
    private String temperatureAdvice;
    private String humidityAdvice;
    private String generalAdvice;

    public AgricultureAdvice() {}

    public AgricultureAdvice(String irrigationAdvice, String temperatureAdvice, String humidityAdvice, String generalAdvice) {
        this.irrigationAdvice = irrigationAdvice;
        this.temperatureAdvice = temperatureAdvice;
        this.humidityAdvice = humidityAdvice;
        this.generalAdvice = generalAdvice;
    }

    public String getIrrigationAdvice() {
        return irrigationAdvice;
    }

    public void setIrrigationAdvice(String irrigationAdvice) {
        this.irrigationAdvice = irrigationAdvice;
    }

    public String getTemperatureAdvice() {
        return temperatureAdvice;
    }

    public void setTemperatureAdvice(String temperatureAdvice) {
        this.temperatureAdvice = temperatureAdvice;
    }

    public String getHumidityAdvice() {
        return humidityAdvice;
    }

    public void setHumidityAdvice(String humidityAdvice) {
        this.humidityAdvice = humidityAdvice;
    }

    public String getGeneralAdvice() {
        return generalAdvice;
    }

    public void setGeneralAdvice(String generalAdvice) {
        this.generalAdvice = generalAdvice;
    }
}
