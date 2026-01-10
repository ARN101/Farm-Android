package com.example.farmwise;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.farmwise.adapters.FinanceAdapter;
import com.example.farmwise.models.Finance;
import com.example.farmwise.models.Livestock;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FarmFinanceActivity extends AppCompatActivity {

    private TextView textTotalIncome, textTotalExpense, textNetProfit;
    private Spinner spinnerType, spinnerAnimalId;
    private EditText editCategory, editAmount, editDate, editNotes;
    private Button btnAddRecord, btnDeleteRecord;
    private RecyclerView recyclerView;
    private FinanceAdapter adapter;
    private List<Finance> financeList;
    private List<Livestock> livestockList;
    private ArrayAdapter<Livestock> animalAdapter;
    private FirebaseFirestore db;
    private Finance selectedFinance;
    private Calendar dateCal;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_finance);

        db = FirebaseFirestore.getInstance();

        textTotalIncome = findViewById(R.id.textTotalIncome);
        textTotalExpense = findViewById(R.id.textTotalExpense);
        textNetProfit = findViewById(R.id.textNetProfit);

        spinnerType = findViewById(R.id.spinnerType);
        spinnerAnimalId = findViewById(R.id.spinnerAnimalId);
        editCategory = findViewById(R.id.editCategory);
        editAmount = findViewById(R.id.editAmount);
        editDate = findViewById(R.id.editDate);
        editNotes = findViewById(R.id.editNotes);
        btnAddRecord = findViewById(R.id.btnAddRecord);
        btnDeleteRecord = findViewById(R.id.btnDeleteRecord);
        recyclerView = findViewById(R.id.recyclerFinance);

        dateCal = Calendar.getInstance();

        String[] types = { "Income", "Expense" };
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        livestockList = new ArrayList<>();

        animalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, livestockList);
        animalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnimalId.setAdapter(animalAdapter);
        loadLivestockForSpinner();

        financeList = new ArrayList<>();
        adapter = new FinanceAdapter(financeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(finance -> {
            selectedFinance = finance;
            editCategory.setText(finance.getCategory());
            editAmount.setText(String.valueOf(finance.getAmount()));
            editNotes.setText(finance.getNotes());
            if (finance.getDate() != null)
                editDate.setText(dateFormat.format(finance.getDate().toDate()));

            String navType = finance.getType();
            if (navType.equals("Income"))
                spinnerType.setSelection(0);
            else
                spinnerType.setSelection(1);

            Toast.makeText(this, "Selected Record", Toast.LENGTH_SHORT).show();
        });

        editDate.setOnClickListener(v -> showDatePicker());
        btnAddRecord.setOnClickListener(v -> addFinance());
        btnDeleteRecord.setOnClickListener(v -> deleteFinance());

        loadFinances();
    }

    private void loadLivestockForSpinner() {
        db.collection("livestock")
                .orderBy("name", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    livestockList.clear();
                    Livestock none = new Livestock();
                    none.setName("None");
                    none.setId("");
                    livestockList.add(none);

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Livestock> loadedList = queryDocumentSnapshots.toObjects(Livestock.class);
                        int i = 0;
                        for (com.google.firebase.firestore.DocumentSnapshot doc : queryDocumentSnapshots
                                .getDocuments()) {
                            loadedList.get(i).setId(doc.getId());
                            i++;
                        }
                        livestockList.addAll(loadedList);
                    }
                    animalAdapter.notifyDataSetChanged();
                });
    }

    private void loadFinances() {
        db.collection("finances")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null)
                        return;

                    financeList.clear();
                    if (value != null) {
                        financeList.addAll(value.toObjects(Finance.class));
                        int i = 0;
                        for (com.google.firebase.firestore.DocumentSnapshot doc : value.getDocuments()) {
                            financeList.get(i).setId(doc.getId());
                            i++;
                        }
                    }
                    adapter.updateList(financeList);
                    calculateTotals();
                });
    }

    private void calculateTotals() {
        double income = 0;
        double expense = 0;

        for (Finance f : financeList) {
            if ("Income".equalsIgnoreCase(f.getType())) {
                income += f.getAmount();
            } else {
                expense += f.getAmount();
            }
        }

        textTotalIncome.setText(String.format(Locale.getDefault(), "%.2f", income));
        textTotalExpense.setText(String.format(Locale.getDefault(), "%.2f", expense));
        textNetProfit.setText(String.format(Locale.getDefault(), "%.2f", income - expense));
    }

    private void addFinance() {
        String type = spinnerType.getSelectedItem().toString();
        String category = editCategory.getText().toString().trim();
        String amountStr = editAmount.getText().toString().trim();
        String dateStr = editDate.getText().toString();
        String notes = editNotes.getText().toString().trim();

        Livestock selectedAnimal = (Livestock) spinnerAnimalId.getSelectedItem();
        String animalIdStr = (selectedAnimal != null && !selectedAnimal.getId().isEmpty())
                ? selectedAnimal.toString()
                : "None";

        if (TextUtils.isEmpty(category) || TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(dateStr)) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        Timestamp dateTs = new Timestamp(dateCal.getTime());

        Finance finance = new Finance(type, category, amount, dateTs, animalIdStr, notes, Timestamp.now());

        db.collection("finances").add(finance)
                .addOnSuccessListener(ref -> {
                    Toast.makeText(this, "Record Added", Toast.LENGTH_SHORT).show();
                    clearInputs();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error adding record", Toast.LENGTH_SHORT).show());
    }

    private void deleteFinance() {
        if (selectedFinance == null) {
            Toast.makeText(this, "Select a record to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("finances").document(selectedFinance.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Record Deleted", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    selectedFinance = null;
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error deleting record", Toast.LENGTH_SHORT).show());
    }

    private void showDatePicker() {
        int year = dateCal.get(Calendar.YEAR);
        int month = dateCal.get(Calendar.MONTH);
        int day = dateCal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, y, m, d) -> {
            dateCal.set(y, m, d);
            editDate.setText(dateFormat.format(dateCal.getTime()));
        }, year, month, day);
        datePickerDialog.show();
    }

    private void clearInputs() {
        editCategory.setText("");
        editAmount.setText("");
        editDate.setText("");
        editNotes.setText("");
        spinnerType.setSelection(0);
        if (!livestockList.isEmpty())
            spinnerAnimalId.setSelection(0);
    }
}
