package com.example.restorandegerlendirmeuygulamasi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    ImageView restaurantImage;
    TextView restaurantName;
    RatingBar ratingBar;
    WebView webView;
    TextView textAverageRating;
    TextView textLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // XML öğeleri tanımlanıyor
        restaurantImage = findViewById(R.id.restaurantImage);
        restaurantName = findViewById(R.id.restaurantName);
        ratingBar = findViewById(R.id.ratingBar);
        webView = findViewById(R.id.webView);
        textAverageRating = findViewById(R.id.textAverageRating);
        textLocation = findViewById(R.id.textLocation);

        // Intent ile gelen bilgiler alınıyor
        String name = getIntent().getStringExtra("name");
        int imageId = getIntent().getIntExtra("image", R.drawable.logo);
        String webUrl = getIntent().getStringExtra("url");
        String location = getIntent().getStringExtra("location");

        // Görseller ve ad bilgisi
        restaurantName.setText(name);
        restaurantImage.setImageResource(imageId);

        // WebView ayarları
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                // Eğer sayfa açılmazsa Google'da arama yap
                String fallbackUrl = "https://www.google.com/search?q=" + Uri.encode(name + " Paris restaurant");
                view.loadUrl(fallbackUrl);
                Toast.makeText(DetailActivity.this, "Web sitesi açılamadı, Google'da aratılıyor.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Eski Android sürümleri için uyum
                String fallbackUrl = "https://www.google.com/search?q=" + Uri.encode(name + " Paris restaurant");
                view.loadUrl(fallbackUrl);
                Toast.makeText(DetailActivity.this, "Web sitesi açılamadı, Google'da aratılıyor.", Toast.LENGTH_SHORT).show();
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(webUrl);

        // Veritabanı bağlantısı
        DatabaseHelper db = new DatabaseHelper(this);

        // Ortalama puan
        float avgRating = db.getAverageRating(name);
        textAverageRating.setText("Ortalama Puan: " + String.format("%.1f", avgRating) + " ★");

        // Kaydedilen puanı göster
        float savedRating = db.getRating(name);
        ratingBar.setRating(savedRating);

        // Yeni puan verildiğinde kaydet ve ortalamayı güncelle
        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            db.saveRating(name, rating);
            float newAvg = db.getAverageRating(name);
            textAverageRating.setText("Ortalama Puan: " + String.format("%.1f", newAvg) + " ★");
        });

        // Konuma tıklanınca Google Maps'e git
        textLocation.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Google Maps yüklü değil!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
