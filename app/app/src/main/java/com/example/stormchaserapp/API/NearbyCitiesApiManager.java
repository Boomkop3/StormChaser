package com.example.stormchaserapp.API;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stormchaserapp.Models.LocalWeather;
import com.example.stormchaserapp.lib.Toaster;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class NearbyCitiesApiManager {
    private static final int cityLimit = 12;
    private static NearbyCitiesApiManager instance;
    private Context context;
    private RequestQueue queue;
    private static final String url =
            "http://getnearbycities.geobytes.com/GetNearbyCities?callback=?&radius=100";
    private static final String latkey = "&latitude=";
    private static final String lonkey = "&longitude=";
    private NearbyCitiesApiManager(Context context){
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }
    public static NearbyCitiesApiManager with(Context context){
        if (instance == null){
            instance = new NearbyCitiesApiManager(context);
        }
        return instance;
    }

    private String getUrlFor(LatLng latLng){
        return url
            + latkey
            + safeDecimalPrint(latLng.latitude, 7)
            + lonkey
            + safeDecimalPrint(latLng.longitude, 7);
    }
    private String safeDecimalPrint(double d, int decimals){
        return "" + ((int)d) + "." + ((int)((d-(int)d)*Math.pow(10, decimals)));
    }

    public void getNearbyCities(LatLng location, final OnNewNearbyCity callback) throws UnsupportedEncodingException {
        String url = getUrlFor(location);
        queue.add(
                new StringRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                response = response.substring(
                                        2,
                                        response.length() - 2
                                );
                                try {
                                    JSONArray jsonResponse = new JSONArray(response);
                                    int length = jsonResponse.length();
                                    if (length > cityLimit){
                                        length = cityLimit;
                                    }
                                    for (int i = 0; i < length; i++) {
                                        handleCityRequest(
                                                jsonResponse.getJSONArray(i),
                                                callback
                                        );
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toaster.with(context).toastShort(
                                        "Could not get nearby weather locations"
                                );
                                error.printStackTrace();
                            }
                        }
                )
        );
        queue.start();
    }
    private void handleCityRequest(final JSONArray cityDescription, final OnNewNearbyCity callback) throws JSONException {
        String locationCode = cityDescription.getString(1) + "," + cityDescription.getString(6);
        WeatherApiManager.with(context).getWeatherFor(
                locationCode,
                new OnWeatherCallback() {
                    @Override
                    public void callback(LocalWeather weather) {
                        callback.newCity(weather);
                    }
                });
    }
}