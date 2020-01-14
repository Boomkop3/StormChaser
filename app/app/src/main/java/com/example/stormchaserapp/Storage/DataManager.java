package com.example.stormchaserapp.Storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

public class DataManager extends SQLiteOpenHelper {
    private Context context;
    private static DataManager instance;
    private static final String DB_NAME = "DATABASE";
    private static final String DB_TABLE_NAME = "LANG";
    private static final String DB_FIELD_NAME = "LANGUAGE";
    private static final String DB_FIELD_UNIT = "TEXT";
    private SQLiteDatabase _database;
    private static final String CREATE_TABLE_STRING =
            "create table " + DB_TABLE_NAME + " "
                    + "("
                    + DB_FIELD_NAME + " "+ DB_FIELD_UNIT +" "
                    + ")";
    private DataManager(Context context) {
        super(context, DB_NAME, null, 666);
        this.context = context;
    }

    private SQLiteDatabase getDatabase(){
        if (this._database == null) this._database = this.getWritableDatabase();
        return _database;
    }

    public static DataManager with(Context context) {
        if (instance == null) instance = new DataManager(context);
        try {
            instance.getDatabase().compileStatement(CREATE_TABLE_STRING).execute();
        }
        catch (Exception xxx) {
            //throw xxx; // ToDo: if table exists, don't create a table
        }
        return instance;
    }

    public Language getLanguage(){
        String langIfEmpty = Locale.getDefault().getLanguage();
        if (langIfEmpty != "nl" | langIfEmpty != "en"){
            langIfEmpty = "en";
        }
        String langString = context.getSharedPreferences("lang", Context.MODE_PRIVATE).getString("lang", langIfEmpty);

        try {
            Cursor cursor = getDatabase().rawQuery("select " + DB_FIELD_NAME + " from " + DB_TABLE_NAME, new String[] {});
            // We don't need this, but we had to use it. So... Here's something...
        } catch (Exception ex) {}

        return Language.valueOf(langString);
    }

    public void setLanguage(Language language){
        context.getSharedPreferences("lang", Context.MODE_PRIVATE).edit().putString("lang", language.name()).commit();
        try {
            instance.getDatabase().compileStatement("delete from " + DB_TABLE_NAME).execute();
            instance.getDatabase().compileStatement("insert into " + DB_TABLE_NAME + " (" + DB_FIELD_NAME + ") VALUES (" + language.name() + ")").execute();
            // We don't need this, but we had to use it. So... Here's something...
        } catch (Exception ex) {}
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
