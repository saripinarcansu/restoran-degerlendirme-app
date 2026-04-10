package com.example.restorandegerlendirmeuygulamasi;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    ListView favoriteListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoriteListView = findViewById(R.id.favoriteListView);

        DatabaseHelper db = new DatabaseHelper(this);
        List<String> favoriteList = db.getAllFavorites(); // ← getAllFavorites() doğru çağrılıyor

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                favoriteList
        );

        favoriteListView.setAdapter(adapter);
    }
}
