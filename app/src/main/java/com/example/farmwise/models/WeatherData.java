package com.example.farmwise.models;

public class WeatherData {
    private int temperature;
    private String description;
    private int humidity;
    private boolean rainExpected;

    public WeatherData(int temperature, String description, int humidity, boolean rainExpected) {
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
        this.rainExpected = rainExpected;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public int getHumidity() {
        return humidity;
    }

    public boolean isRainExpected() {
        return rainExpected;
    }
}
