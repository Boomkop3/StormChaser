package com.example.stormchaserapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.stormchaserapp.Storage.DataManager;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale(DataManager.with(this).getLanguage().name());
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.buttonStart);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMap();
            }
        });
    }

    public void launchMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
