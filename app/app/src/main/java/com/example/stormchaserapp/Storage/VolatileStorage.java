package com.example.stormchaserapp.Storage;

import com.example.stormchaserapp.Models.LocalWeather;

import java.util.ArrayList;

public class VolatileStorage {
    private ArrayList<LocalWeather> loadedCities;
    private boolean gotWeatherInfo;
    private static VolatileStorage instance;
    private VolatileStorage(){
        loadedCities = new ArrayList<>();
        gotWeatherInfo = false;
    }
    public static VolatileStorage getInstance(){
        if (instance == null) {
            instance = new VolatileStorage();
        }
        return instance;
    }

    public ArrayList<LocalWeather> getLoadedCities() {
        return loadedCities;
    }

    public boolean isGotWeatherInfo() {
        return gotWeatherInfo;
    }

    public void setGotWeatherInfo(boolean gotWeatherInfo) {
        this.gotWeatherInfo = gotWeatherInfo;
    }
}
