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

public class FarmFinanceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_finance);

        Spinner spinnerType = findViewById(R.id.spinnerType);
        String[] types = { "Income", "Expense" };
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        Spinner spinnerAnimalId = findViewById(R.id.spinnerAnimalId);
        String[] animalIds = { "None", "A1", "A2", "A3", "A4", "A5" };
        ArrayAdapter<String> animalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, animalIds);
        animalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnimalId.setAdapter(animalAdapter);

        EditText editDate = findViewById(R.id.editDate);
        editDate.setOnClickListener(v -> showDatePicker(editDate));

        RecyclerView recyclerView = findViewById(R.id.recyclerFinance);
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
