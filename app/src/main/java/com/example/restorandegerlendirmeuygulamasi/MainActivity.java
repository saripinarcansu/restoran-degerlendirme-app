package com.example.restorandegerlendirmeuygulamasi;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ListView restaurantListView;

    String[] names = {
            "Le Jules Verne",
            "L'Ambroisie",
            "Septime",
            "Chez Janou",
            "Le Meurice",
            "Le Clarence",
            "Vaisseau",
            "Datil",
            "Le Doyenné",
            "Le Servan"
    };

    String[] descriptions = {
            "Eyfel Kulesi\u2019nde panoramik manzaral\u0131 \u015f\u0131k restoran.",
            "Place des Vosges\u2019te \u00fc\u00e7 Michelin y\u0131ld\u0131zl\u0131 klasik Frans\u0131z mutfa\u011f\u0131.",
            "Modern tarzda yarat\u0131c\u0131 men\u00fcs\u00fcyle \u00f6ne \u00e7\u0131kar.",
            "Samimi atmosferde geleneksel Frans\u0131z lezzetleri.",
            "L\u00fcks otel restoran\u0131, Alain Ducasse dokunu\u015fuyla.",
            "Paris'in alt\u0131n \u00fc\u00e7geninde, 19. y\u00fczy\u0131l \u015fatoda iki y\u0131ld\u0131zl\u0131 \u015fef.",
            "Minimalist dekor, yarat\u0131c\u0131 Frans\u0131z-Japon f\u00fczyonu.",
            "S\u00fcrd\u00fcr\u00fclebilir bitkisel men\u00fcyle Marais\u2019te fark yarat\u0131r.",
            "\u015eehir d\u0131\u015f\u0131nda \u015fatoda, \u00e7iftlikten masaya konsepti.",
            "Frans\u0131z-Asya f\u00fczyonu, gen\u00e7 \u015fef karde\u015flerin eseri."
    };

    int[] images = {
            R.drawable.jules,
            R.drawable.ambroisie,
            R.drawable.septime,
            R.drawable.janou,
            R.drawable.meurice,
            R.drawable.le_clarence,
            R.drawable.vaisseau,
            R.drawable.datil,
            R.drawable.le_doyenne,
            R.drawable.le_servan
    };

    String[] urls = {
            "https://www.restaurants-toureiffel.com",
            "https://www.ambroisie-paris.com",
            "https://www.septime-charonne.fr",
            "https://www.chezjanou.com",
            "https://www.dorchestercollection.com",
            "https://www.le-clarence.paris",
            "https://www.restaurant-vaisseau.com",
            "https://www.datil.fr",
            "https://www.ledoyenne.paris",
            "https://www.leservan.com"
    };

    String[] locations = {
            "Champ de Mars, 75007 Paris, France",
            "9 Place des Vosges, 75004 Paris, France",
            "80 Rue de Charonne, 75011 Paris, France",
            "2 Rue Roger Verlomme, 75003 Paris, France",
            "228 Rue de Rivoli, 75001 Paris, France",
            "31 Avenue Franklin Delano Roosevelt, 75008 Paris",
            "5 Rue de la Folie Méricourt, 75011 Paris",
            "8 Rue de la Corderie, 75003 Paris",
            "5 Rue Saint-Antoine, 91770 Saint-Vrain",
            "32 Rue Saint-Maur, 75011 Paris"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restaurantListView = findViewById(R.id.restaurantListView);
        SearchView searchView = findViewById(R.id.searchView);
        Button buttonFavorites = findViewById(R.id.buttonFavorites);

        RestaurantAdapter adapter = new RestaurantAdapter(this, names, descriptions, images);
        restaurantListView.setAdapter(adapter);
        registerForContextMenu(restaurantListView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        buttonFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        restaurantListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("name", names[i]);
            intent.putExtra("image", images[i]);
            intent.putExtra("url", urls[i]);
            intent.putExtra("location", locations[i]);
            startActivity(intent);
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String selectedName = names[info.position];
        DatabaseHelper db = new DatabaseHelper(this);

        menu.setHeaderTitle(selectedName);
        if (db.isFavorite(selectedName)) {
            menu.add(0, 1, 0, "Favorilerden Kaldır");
        } else {
            menu.add(0, 2, 0, "Favorilere Ekle");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String selectedName = names[info.position];
        DatabaseHelper db = new DatabaseHelper(this);

        switch (item.getItemId()) {
            case 1:
                db.removeFavorite(selectedName);
                Toast.makeText(this, selectedName + " favorilerden çıkarıldı", Toast.LENGTH_SHORT).show();
                return true;
            case 2:
                db.addFavorite(selectedName);
                Toast.makeText(this, selectedName + " favorilere eklendi", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
