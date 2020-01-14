package com.example.stormchaserapp.Storage;

import android.content.Context;

import java.util.Locale;

public class DataManager {
    private Context context;
    private static DataManager instance;
    private DataManager(Context context) {
        this.context = context;
    }

    public static DataManager with(Context context) {
        if (instance == null) instance = new DataManager(context);
        return instance;
    }

    public Language getLanguage(){
        String langIfEmpty = Locale.getDefault().getLanguage();
        if (langIfEmpty != "nl" | langIfEmpty != "en"){
            langIfEmpty = "en";
        }
        String langString = context.getSharedPreferences("lang", Context.MODE_PRIVATE).getString("lang", langIfEmpty);
        return Language.valueOf(langString);
    }

    public void setLanguage(Language language){
        context.getSharedPreferences("lang", Context.MODE_PRIVATE).edit().putString("lang", language.name()).commit();
    }
}
