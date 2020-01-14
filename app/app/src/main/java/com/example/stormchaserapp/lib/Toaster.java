package com.example.stormchaserapp.lib;

import android.content.Context;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class Toaster {
    private static Toaster instance;
    private Context context;
    private Toaster(Context context){
        this.context = context;
    }
    public static Toaster with(Context context){
        if (instance == null){
            instance = new Toaster(context);
        }
        return instance;
    }
    public void toast(String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
    public void toastShort(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
