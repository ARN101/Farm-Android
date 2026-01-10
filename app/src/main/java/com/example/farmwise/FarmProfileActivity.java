package com.example.farmwise;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.farmwise.models.FarmProfile;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;

public class FarmProfileActivity extends AppCompatActivity {

    private EditText editFarmName, editCity, editCountry;
    private Button btnSaveProfile;
    private FirebaseFirestore db;
    private static final String PROFILE_DOC_ID = "main_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_profile);

        db = FirebaseFirestore.getInstance();

        editFarmName = findViewById(R.id.editFarmName);
        editCity = findViewById(R.id.editCity);
        editCountry = findViewById(R.id.editCountry);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        loadProfile();

        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void loadProfile() {
        db.collection("farms").document(PROFILE_DOC_ID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        FarmProfile profile = documentSnapshot.toObject(FarmProfile.class);
                        if (profile != null) {
                            editFarmName.setText(profile.getFarmName());
                            editCity.setText(profile.getCity());
                            editCountry.setText(profile.getCountry());
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show());
    }

    private void saveProfile() {
        String name = editFarmName.getText().toString().trim();
        String city = editCity.getText().toString().trim();
        String country = editCountry.getText().toString().trim();

        if (name.isEmpty()) {
            editFarmName.setError("Required");
            return;
        }

        FarmProfile profile = new FarmProfile(name, city, country, Timestamp.now(), Timestamp.now());

        db.collection("farms").document(PROFILE_DOC_ID).set(profile)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error saving profile", Toast.LENGTH_SHORT).show());
    }
}
