package com.example.stormchaserapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.stormchaserapp.Storage.DataManager;
import com.example.stormchaserapp.Storage.Language;

import java.util.Locale;

public class SettingsFragment extends AppCompatActivity {

    private RadioButton NL;
    private RadioButton EN;


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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_fragment);

        Button backButton = findViewById(R.id.buttonMap);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMap();
            }
        });

        NL = findViewById(R.id.radioButtonNL);
        EN = findViewById(R.id.radioButtonEN);

        if (DataManager.with(getApplicationContext()).getLanguage() == Language.nl){
            NL.toggle();
        } else {
            EN.toggle();
        }

        NL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataManager.with(getApplicationContext()).setLanguage(Language.nl);
                setLocale(Language.nl.name());
                recreate();
            }
        });
        EN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataManager.with(getApplicationContext()).setLanguage(Language.en);
                setLocale(Language.en.name());
                recreate();
            }
        });
    }

    public void launchMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
