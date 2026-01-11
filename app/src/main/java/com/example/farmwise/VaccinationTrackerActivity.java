package com.example.farmwise;

import android.app.DatePickerDialog;
import android.graphics.Color;
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
import com.example.farmwise.adapters.VaccinationAdapter;
import com.example.farmwise.models.Livestock;
import com.example.farmwise.models.Vaccination;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VaccinationTrackerActivity extends AppCompatActivity {

    private Spinner spinnerAnimalId;
    private EditText editVaccineName, editDateGiven, editNextDueDate, editNotes;
    private Button btnAddRecord, btnDeleteRecord;
    private RecyclerView recyclerView;
    private VaccinationAdapter adapter;
    private List<Vaccination> vaccinationList;
    private List<Livestock> livestockList;
    private ArrayAdapter<Livestock> animalAdapter;
    private FirebaseFirestore db;
    private Vaccination selectedVaccination;
    private Calendar dateGivenCal, nextDueCal;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_tracker);

        db = FirebaseFirestore.getInstance();

        spinnerAnimalId = findViewById(R.id.spinnerAnimalId);
        editVaccineName = findViewById(R.id.editVaccineName);
        editDateGiven = findViewById(R.id.editDateGiven);
        editNextDueDate = findViewById(R.id.editNextDueDate);
        editNotes = findViewById(R.id.editNotes);
        btnAddRecord = findViewById(R.id.btnAddRecord);
        btnDeleteRecord = findViewById(R.id.btnDeleteRecord);
        recyclerView = findViewById(R.id.recyclerVaccination);

        dateGivenCal = Calendar.getInstance();
        nextDueCal = Calendar.getInstance();

        livestockList = new ArrayList<>();
        animalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, livestockList);
        animalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnimalId.setAdapter(animalAdapter);
        loadLivestockForSpinner();

        vaccinationList = new ArrayList<>();
        adapter = new VaccinationAdapter(vaccinationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((vaccination, position) -> {
            selectedVaccination = vaccination;
            adapter.setSelectedPosition(position);
            Toast.makeText(this, "Selected for deletion", Toast.LENGTH_SHORT).show();
        });

        editDateGiven.setOnClickListener(v -> showDatePicker(editDateGiven, dateGivenCal));
        editNextDueDate.setOnClickListener(v -> showDatePicker(editNextDueDate, nextDueCal));

        btnAddRecord.setOnClickListener(v -> addVaccination());
        btnDeleteRecord.setOnClickListener(v -> deleteVaccination());

        loadVaccinations();
    }

    private void loadLivestockForSpinner() {
        db.collection("livestock")
                .orderBy("name", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    livestockList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Livestock> loadedList = queryDocumentSnapshots.toObjects(Livestock.class);
                        int i = 0;
                        for (com.google.firebase.firestore.DocumentSnapshot doc : queryDocumentSnapshots
                                .getDocuments()) {
                            loadedList.get(i).setId(doc.getId());
                            i++;
                        }
                        livestockList.addAll(loadedList);
                        animalAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadVaccinations() {
        db.collection("vaccinations")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null)
                        return;

                    vaccinationList.clear();
                    if (value != null) {
                        vaccinationList.addAll(value.toObjects(Vaccination.class));
                        int i = 0;
                        for (com.google.firebase.firestore.DocumentSnapshot doc : value.getDocuments()) {
                            vaccinationList.get(i).setId(doc.getId());
                            i++;
                        }
                    }
                    adapter.updateList(vaccinationList);
                });
    }

    private void addVaccination() {
        if (livestockList.isEmpty()) {
            Toast.makeText(this, "No livestock available. Add animals first.", Toast.LENGTH_SHORT).show();
            return;
        }

        Livestock selectedAnimal = (Livestock) spinnerAnimalId.getSelectedItem();
        String vaccine = editVaccineName.getText().toString().trim();
        String notes = editNotes.getText().toString().trim();
        String dateGivenStr = editDateGiven.getText().toString();
        String nextDueStr = editNextDueDate.getText().toString();

        if (selectedAnimal == null || TextUtils.isEmpty(vaccine) || TextUtils.isEmpty(dateGivenStr)) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Timestamp tsGiven = new Timestamp(dateGivenCal.getTime());
        Timestamp tsDue = !TextUtils.isEmpty(nextDueStr) ? new Timestamp(nextDueCal.getTime()) : null;

        String status = "Completed";
        if (tsDue != null) {
            status = calculateStatus(tsDue.toDate());
        }

        Vaccination record = new Vaccination(selectedAnimal.toString(), vaccine,
                tsGiven, tsDue, notes, status, Timestamp.now());

        db.collection("vaccinations").add(record)
                .addOnSuccessListener(ref -> {
                    Toast.makeText(this, "Record Added", Toast.LENGTH_SHORT).show();
                    clearInputs();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error adding record", Toast.LENGTH_SHORT).show());
    }

    private void deleteVaccination() {
        if (selectedVaccination == null) {
            Toast.makeText(this, "Select a record to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("vaccinations").document(selectedVaccination.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Record Deleted", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    selectedVaccination = null;
                    adapter.setSelectedPosition(RecyclerView.NO_POSITION);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error deleting record", Toast.LENGTH_SHORT).show());
    }

    private void showDatePicker(EditText editText, Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, y, m, d) -> {
            calendar.set(y, m, d);
            editText.setText(dateFormat.format(calendar.getTime()));
        }, year, month, day);
        datePickerDialog.show();
    }

    private void clearInputs() {
        editVaccineName.setText("");
        editDateGiven.setText("");
        editNextDueDate.setText("");
        editNotes.setText("");
        if (!livestockList.isEmpty())
            spinnerAnimalId.setSelection(0);
    }

    private String calculateStatus(Date nextDueDate) {
        Calendar today = Calendar.getInstance();
        // Reset time to midnight for accurate day comparison
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar due = Calendar.getInstance();
        due.setTime(nextDueDate);
        due.set(Calendar.HOUR_OF_DAY, 0);
        due.set(Calendar.MINUTE, 0);
        due.set(Calendar.SECOND, 0);
        due.set(Calendar.MILLISECOND, 0);

        if (due.before(today)) {
            return "Overdue";
        }

        long diffMillis = due.getTimeInMillis() - today.getTimeInMillis();
        long days = diffMillis / (24 * 60 * 60 * 1000);

        if (days <= 7) {
            return "Due Soon";
        } else {
            return "Scheduled";
        }
    }
}
