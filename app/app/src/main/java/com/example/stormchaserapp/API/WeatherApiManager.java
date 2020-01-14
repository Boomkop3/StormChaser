package com.example.stormchaserapp.API;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stormchaserapp.Models.LocalWeather;
import com.example.stormchaserapp.lib.Toaster;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WeatherApiManager {
    private static WeatherApiManager instance;
    private Context context;
    private RequestQueue queue;
    private static final String url =
            "http://weather.boomkop.com/?location=";
    private WeatherApiManager(Context context){
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }
    public static WeatherApiManager with(Context context){
        if (instance == null){
            instance = new WeatherApiManager(context);
        }
        return instance;
    }

    private String getUrlFor(String cityCode){
        try {
            return url + URLEncoder.encode(cityCode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getWeatherFor(final String cityCode, final OnWeatherCallback callback) {
        String url = getUrlFor(cityCode);
        queue.add(
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject jsonCity = response.getJSONObject("city");
                                    JSONObject jsonCoord = jsonCity.getJSONObject("coord");
                                    JSONObject jsonWeather =
                                            response
                                            .getJSONArray("list")
                                            .getJSONObject(0);
                                    JSONObject jsonWeatherDesc =
                                            jsonWeather
                                            .getJSONArray("weather")
                                            .getJSONObject(0);
                                    LocalWeather weather = new LocalWeather(
                                            new LatLng(
                                                    jsonCoord.getDouble("lat"),
                                                    jsonCoord.getDouble("lon")
                                            ),
                                            jsonCity.getString("name"),
                                            jsonWeather.has("rain"),
                                            jsonWeatherDesc.getString("main"),
                                            jsonWeatherDesc.getString("description")
                                        );
                                    callback.callback(weather);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toaster.with(context).toastShort(
                                        "Could not get weather at '" + cityCode + "'"
                                );
                                error.printStackTrace();
                            }
                        }
                )
        );
    }
}

interface OnWeatherCallback{
    void callback(LocalWeather weather);
}