package com.example.restorandegerlendirmeuygulamasi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "RestoranApp.db";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, email TEXT, phone TEXT, password TEXT, userType TEXT)");

        db.execSQL("CREATE TABLE ratings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "restaurantName TEXT, " +
                "rating REAL)");

        db.execSQL("CREATE TABLE favorites (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "restaurantName TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS ratings");
        db.execSQL("DROP TABLE IF EXISTS favorites");
        onCreate(db);
    }

    public boolean insertUser(String name, String email, String phone, String password, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("password", password);
        values.put("userType", userType);

        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[]{email, password});
        return cursor.getCount() > 0;
    }

    public void saveRating(String restaurantName, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ratings", "restaurantName=?", new String[]{restaurantName});

        ContentValues values = new ContentValues();
        values.put("restaurantName", restaurantName);
        values.put("rating", rating);

        db.insert("ratings", null, values);
    }

    public float getRating(String restaurantName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT rating FROM ratings WHERE restaurantName=?", new String[]{restaurantName});
        float rating = 0.0f;

        if (cursor.moveToFirst()) {
            rating = cursor.getFloat(0);
        }

        cursor.close();
        return rating;
    }

    public void addFavorite(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("restaurantName", name);
        db.insert("favorites", null, values);
    }

    public void removeFavorite(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("favorites", "restaurantName=?", new String[]{name});
    }

    public boolean isFavorite(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM favorites WHERE restaurantName=?", new String[]{name});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public List<String> getAllFavorites() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT restaurantName FROM favorites", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public float getAverageRating(String restaurantName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(rating) FROM ratings WHERE restaurantName=?", new String[]{restaurantName});
        float avg = 0f;
        if (cursor.moveToFirst()) {
            avg = cursor.getFloat(0);
        }
        cursor.close();
        return avg;
    }

}
