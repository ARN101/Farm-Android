package com.example.farmwise;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LivestockActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestock);

        Spinner spinnerHealth = findViewById(R.id.spinnerHealth);
        String[] healthOptions = { "Healthy", "Sick", "Injured" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, healthOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHealth.setAdapter(adapter);

        RecyclerView recyclerView = findViewById(R.id.recyclerLivestock);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
