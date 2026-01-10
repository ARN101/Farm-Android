package com.example.farmwise;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.farmwise.adapters.LivestockAdapter;
import com.example.farmwise.models.Livestock;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class LivestockActivity extends AppCompatActivity {

    private EditText editType, editName, editBreed, editAge;
    private Spinner spinnerHealth;
    private RecyclerView recyclerView;
    private LivestockAdapter adapter;
    private List<Livestock> livestockList;
    private FirebaseFirestore db;
    private Livestock selectedLivestock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestock);

        db = FirebaseFirestore.getInstance();

        editType = findViewById(R.id.editType);
        editName = findViewById(R.id.editName);
        editBreed = findViewById(R.id.editBreed);
        editAge = findViewById(R.id.editAge);
        spinnerHealth = findViewById(R.id.spinnerHealth);
        recyclerView = findViewById(R.id.recyclerLivestock);
        Button btnAdd = findViewById(R.id.btnAddAnimal);
        Button btnDelete = findViewById(R.id.btnDelete);

        String[] healthOptions = { "Healthy", "Sick", "Injured" };
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                healthOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHealth.setAdapter(spinnerAdapter);

        livestockList = new ArrayList<>();
        adapter = new LivestockAdapter(livestockList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(livestock -> {
            selectedLivestock = livestock;
            editType.setText(livestock.getType());
            editName.setText(livestock.getName());
            editBreed.setText(livestock.getBreed());
            editAge.setText(String.valueOf(livestock.getAge()));

            for (int i = 0; i < healthOptions.length; i++) {
                if (healthOptions[i].equals(livestock.getHealth())) {
                    spinnerHealth.setSelection(i);
                    break;
                }
            }
            Toast.makeText(this, "Selected: " + livestock.getName(), Toast.LENGTH_SHORT).show();
        });

        btnAdd.setOnClickListener(v -> addLivestock());
        btnDelete.setOnClickListener(v -> deleteLivestock());

        loadLivestock();
    }

    private void loadLivestock() {
        db.collection("livestock")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    livestockList.clear();
                    if (value != null) {
                        livestockList.addAll(value.toObjects(Livestock.class));
                        int i = 0;
                        for (com.google.firebase.firestore.DocumentSnapshot doc : value.getDocuments()) {
                            livestockList.get(i).setId(doc.getId());
                            i++;
                        }
                    }
                    adapter.updateList(livestockList);
                });
    }

    private void addLivestock() {
        String type = editType.getText().toString().trim();
        String name = editName.getText().toString().trim();
        String breed = editBreed.getText().toString().trim();
        String ageStr = editAge.getText().toString().trim();
        String health = spinnerHealth.getSelectedItem().toString();

        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr)) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        Livestock livestock = new Livestock(type, name, breed, age, health, Timestamp.now());

        db.collection("livestock").add(livestock)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Animal Added", Toast.LENGTH_SHORT).show();
                    clearInputs();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error adding animal", Toast.LENGTH_SHORT).show());
    }

    private void deleteLivestock() {
        if (selectedLivestock == null) {
            Toast.makeText(this, "Select an animal to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("livestock").document(selectedLivestock.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Animal Deleted", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    selectedLivestock = null;
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error deleting animal", Toast.LENGTH_SHORT).show());
    }

    private void clearInputs() {
        editType.setText("");
        editName.setText("");
        editBreed.setText("");
        editAge.setText("");
        spinnerHealth.setSelection(0);
    }
}
