package com.example.stormchaserapp.Models;

import com.google.android.gms.maps.model.LatLng;

public class LocalWeather {
    private LatLng location;
    private String cityName;
    private boolean raining;
    private String weatherHeader;
    private String weatherDescription;

    public LocalWeather(
            LatLng location,
            String cityName,
            boolean raining,
            String weatherHeader,
            String weatherDescription
        ) {
        this.location = location;
        this.cityName = cityName;
        this.raining = raining;
        this.weatherHeader = weatherHeader;
        this.weatherDescription = weatherDescription;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getCityName() {
        return cityName;
    }

    public boolean isRaining() {
        return raining;
    }

    public String getWeatherHeader() {
        return weatherHeader;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }
}
