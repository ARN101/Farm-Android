package com.example.farmwise;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;

public class VaccinationTrackerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_tracker);

        Spinner spinnerAnimalId = findViewById(R.id.spinnerAnimalId);
        String[] animalIds = { "A1", "A2", "A3", "A4", "A5" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, animalIds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnimalId.setAdapter(adapter);

        EditText editDateGiven = findViewById(R.id.editDateGiven);
        EditText editNextDueDate = findViewById(R.id.editNextDueDate);

        editDateGiven.setOnClickListener(v -> showDatePicker(editDateGiven));
        editNextDueDate.setOnClickListener(v -> showDatePicker(editNextDueDate));

        RecyclerView recyclerView = findViewById(R.id.recyclerVaccination);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, y, m, d) -> {
            String date = String.format("%02d/%02d/%d", d, m + 1, y);
            editText.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }
}
