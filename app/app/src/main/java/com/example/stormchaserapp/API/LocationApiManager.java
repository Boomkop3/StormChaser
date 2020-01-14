package com.example.stormchaserapp.API;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.stormchaserapp.lib.Toaster;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationApiManager {
    private static LocationApiManager instance;
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    public static LocationApiManager with(Context context){
        if (instance == null){
            instance = new LocationApiManager(context);
        }
        return instance;
    }
    private LocationApiManager(Context context){
        this.context = context;
    }
    public void getFusedLocation(OnSuccessListener onSuccessListener){
        if (checkLocationPermission()) {
            if (fusedLocationClient == null){
                fusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(
                                context
                        );
            }
            fusedLocationClient
                    .getLastLocation()
                    .addOnSuccessListener(onSuccessListener);
        }
        else {
            throw new IllegalStateException("Permission to get user location denied");
        }
    }
    public void getLocationPermission(Activity activity){
        if (!checkLocationPermission()){
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    // Google's documentation is kinda bad
                    5 // f this
            );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void getBackgroundLocPermission(Activity activity) {
        if (!checkBackgroundLocPermission()) {
            ActivityCompat.requestPermissions(activity, new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            }, 6);
        }
    }
    public boolean checkLocationPermission(){
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkBackgroundLocPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }


    public void startListeningUserLocation(LocationListener locationListener) {
        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationCriteria.setAltitudeRequired(false);
        locationCriteria.setBearingRequired(false);
        locationCriteria.setSpeedRequired(false);
        locationCriteria.setCostAllowed(true);
        LocationManager locationManager =
                (LocationManager) context.getSystemService(
                        context.LOCATION_SERVICE
                );
        if (checkLocationPermission()) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    1,
                    locationListener,
                    null
            );
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        else {
            throw new IllegalStateException("Permission to get user location denied");
        }
    }
}
