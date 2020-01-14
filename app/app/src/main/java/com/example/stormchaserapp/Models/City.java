package com.example.stormchaserapp.Models;

import com.google.android.gms.maps.model.LatLng;

public class City {
    private LatLng location;
    private boolean raining;
    private String name;
    private String weatherHeader;
    private String weatherDescription;
    public City(
            LatLng location,
            boolean raining,
            String name,
            String weatherHeader,
            String weatherDescription
        ) {
        this.location = location;
        this.raining = raining;
        this.name = name;
        this.weatherHeader = weatherHeader;
        this.weatherDescription = weatherDescription;
    }

    public LatLng getLocation() {
        return location;
    }

    public boolean isRaining() {
        return raining;
    }

    public String getName() {
        return name;
    }

    public String getWeatherHeader() {
        return weatherHeader;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }
}
