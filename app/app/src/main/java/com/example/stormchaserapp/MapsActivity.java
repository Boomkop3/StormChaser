package com.example.stormchaserapp;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.example.stormchaserapp.API.LocationApiManager;
import com.example.stormchaserapp.API.NearbyCitiesApiManager;
import com.example.stormchaserapp.API.OnNewNearbyCity;
import com.example.stormchaserapp.Models.LocalWeather;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class MapsActivity
        extends     FragmentActivity
        implements  OnMapReadyCallback,
                    LocationListener,
                    OnSuccessListener<Location> {
    private GoogleMap map;
    private LocationApiManager locationApiManager;
    private Context context;
    private boolean gotWeatherInfo;

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        Resources resources = getBaseContext().getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(lang);
        getBaseContext().getApplicationContext().createConfigurationContext(configuration);

        Configuration cfg = new Configuration();
        if (!TextUtils.isEmpty(lang)) {
            cfg.locale = new Locale(lang);
        }
        else {
            cfg.locale = Locale.getDefault();
        }
        this.getResources().updateConfiguration(cfg, null);
    }

    public void startSettings() {
        Intent intent = new Intent(this, SettingsFragment.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.gotWeatherInfo = false;
        this.context = getApplicationContext();
        this.locationApiManager = LocationApiManager.with(context);
        setContentView(R.layout.activity_maps);
        ImageButton settings = findViewById(R.id.buttonSettings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettings();
            }
        });
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Start the map using lastKnownLocation from fusedLocation
        // Then switch to gps as soon as it's available
        if (locationApiManager.checkLocationPermission()){
            locationApiManager.getFusedLocation(this);
            locationApiManager.startListeningUserLocation(this);
        }
        else {
            locationApiManager.getLocationPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == requestCode) {
            if (locationApiManager.checkLocationPermission()) {
                locationApiManager.startListeningUserLocation(this);
            } else {
                // Otherwise, just ask again
                locationApiManager.getLocationPermission(this);
            }
        }
    }

    private void handleLocationUpdate(Location location){
        LatLng locationPoint = new LatLng(
                location.getLatitude(),
                location.getLongitude()
        );
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(locationPoint);
        circleOptions.clickable(false);
        circleOptions.radius(1000);
        circleOptions.fillColor(0);
        map.addCircle(circleOptions);
        map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        locationPoint,
                        10
                )
        );
        if (!this.gotWeatherInfo){
            this.gotWeatherInfo = true;
            try {
                NearbyCitiesApiManager
                        .with(this)
                        .getNearbyCities(
                                locationPoint,
                                new OnNewNearbyCity() {
                                    @Override
                                    public void newCity(LocalWeather city) {

                                        // ToDo: print city on map if rainy





                                    }
                                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    // fusedLocation
    @Override
    public void onSuccess(Location location) {
        if (location != null){
            handleLocationUpdate(location);
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(context.getResources().getString(
                        R.string.location_required_dialog_title))
                .setMessage(context.getResources().getString(
                        R.string.location_required_dialog_message))
                .create()
                .show();
    }

    @Override
    public void onLocationChanged(Location location) {
        handleLocationUpdate(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // wtvr
    }

    @Override
    public void onProviderEnabled(String s) {
        // wtvr
    }

    @Override
    public void onProviderDisabled(String s) {
        // wtvr
    }
}

interface onCoordinateResponse {
    void onResponse(LatLng location);
}
