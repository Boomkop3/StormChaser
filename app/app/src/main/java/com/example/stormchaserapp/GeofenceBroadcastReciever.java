package com.example.stormchaserapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.stormchaserapp.lib.Toaster;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("recieved");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Toaster.with(context).toastShort("Error Geofencing");
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Toaster.with(context).toastShort(context.getResources().getString(R.string.toastRadSuc));
        }
    }
}
