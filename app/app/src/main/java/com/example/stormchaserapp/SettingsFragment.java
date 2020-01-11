package com.example.stormchaserapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

public class SettingsFragment extends AppCompatActivity {

    private RadioButton NL;
    private RadioButton EN;

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
        NL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        EN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void launchMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
