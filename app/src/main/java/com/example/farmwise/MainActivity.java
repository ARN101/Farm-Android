package com.example.farmwise;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.TextView;
import com.example.farmwise.models.WeatherData;
import com.example.farmwise.utils.WeatherService;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView textTemp, textHumidity, textRain, textCondition;
    private View cardWeather;
    private FirebaseFirestore db;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        textTemp = findViewById(R.id.textTemp);
        textHumidity = findViewById(R.id.textHumidity);
        textRain = findViewById(R.id.textRain);
        textCondition = findViewById(R.id.textCondition);
        cardWeather = findViewById(R.id.cardWeather);

        loadWeather();

        findViewById(R.id.btnLivestock).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, LivestockActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnVaccination).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this,
                    VaccinationTrackerActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnFinance).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, FarmFinanceActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnProfile).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, FarmProfileActivity.class);
            startActivity(intent);
        });
    }

    private void loadWeather() {
        db.collection("farms").document("main_profile").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String city = documentSnapshot.getString("city");
                        if (city != null && !city.isEmpty()) {
                            fetchWeatherData(city);
                        } else {
                        }
                    } else {
                    }
                })
                .addOnFailureListener(e -> {
                });
    }

    private void fetchWeatherData(String city) {
        executorService.execute(() -> {
            WeatherData data = WeatherService.getWeather(city);
            if (data != null) {
                runOnUiThread(() -> updateWeatherUI(data));
            }
        });
    }

    private void updateWeatherUI(WeatherData data) {
        cardWeather.setVisibility(View.VISIBLE);
        textTemp.setText(data.getTemperature() + "°C");
        textHumidity.setText("Humidity: " + data.getHumidity() + "%");
        textCondition.setText(data.getDescription());
        textRain.setText(data.isRainExpected() ? "Rain Expected" : "No Rain");
    }
}