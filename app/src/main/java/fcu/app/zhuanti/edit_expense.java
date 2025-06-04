
package fcu.app.zhuanti;

import android.content.Intent;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class edit_expense extends AppCompatActivity {

    private EditText etAmount, etNote, etDate, etNewCategory;
    private Spinner spinnerCategory;
    //private ImageButton btnBack;
    private ExpenseDBHelper dbHelper;
    private ArrayAdapter<String> categoryAdapter;
    private List<String> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_expense);
        // ✅ 1. Initialize all views FIRST
        etAmount = findViewById(R.id.et_nominal);
        etDate = findViewById(R.id.et_date);
        etNote = findViewById(R.id.editTextTextMultiLine); // initialize before setText!
        etNewCategory = findViewById(R.id.et_new_category);
        spinnerCategory = findViewById(R.id.spinner_category);
        ImageButton btnBack = findViewById(R.id.btn_back);
        Button btnAdd = findViewById(R.id.btn_edit);
        Button btnAddCategory = findViewById(R.id.btn_add_category);
        Button btnDelete = findViewById(R.id.btn_del);

// ✅ 2. Load categories
        dbHelper = new ExpenseDBHelper(this);
        loadCategories(); // fills categoryList

// ✅ 3. Set up spinner adapter
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

// ✅ 4. read intent data and assign to views
        Intent intent = getIntent();
        long transactionId = intent.getLongExtra("id", -1);
        String note = intent.getStringExtra("note");
        String category = intent.getStringExtra("category");
        double amount = intent.getDoubleExtra("amount", 0);
        String date = intent.getStringExtra("date");

// assign values to views
        etNote.setText(note);
        etAmount.setText(String.valueOf(amount));
        etDate.setText(date);

// Set spinner to correct category
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).equals(category)) {
                spinnerCategory.setSelection(i);
                break;
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(edit_expense.this,
                    (view, year1, month1, dayOfMonth) -> {
                        String dateFormatted = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                        etDate.setText(dateFormatted);
                    }, year, month, day);
            datePickerDialog.show();
        });

        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(edit_expense.this, MainActivity.class);
            backIntent.putExtra("selected_tab", R.id.menu_home);
            startActivity(backIntent);
            finish();
        });

        btnAddCategory.setOnClickListener(v -> {
            String newCategory = etNewCategory.getText().toString().trim();
            if (!newCategory.isEmpty() && !categoryList.contains(newCategory)) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", newCategory);
                db.insert(ExpenseDBHelper.CATEGORY_TABLE, null, values);
                categoryList.add(newCategory);
                categoryAdapter.notifyDataSetChanged();
                etNewCategory.setText("");
                Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show();
            }
        });

        btnAdd.setOnClickListener(view -> {
            String amountStr = etAmount.getText().toString().trim();
            String dateStr = etDate.getText().toString().trim();
            String noteStr = etNote.getText().toString().trim();
            String selectedCategory = spinnerCategory.getSelectedItem().toString();


            if (amountStr.isEmpty() || dateStr.isEmpty()) {
                Toast.makeText(edit_expense.this, "Please fill in amount and date", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double parsedAmount = Double.parseDouble(amountStr);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ExpenseDBHelper.COLUMN_AMOUNT, parsedAmount);
                values.put(ExpenseDBHelper.COLUMN_DATE, dateStr);
                values.put(ExpenseDBHelper.COLUMN_NOTE, noteStr);
                values.put("type", "expense");
                values.put("category", selectedCategory);

                int rowsAffected = db.update(
                        ExpenseDBHelper.TABLE_NAME,
                        values,
                        "id = ?",
                        new String[]{String.valueOf(transactionId)}
                );


                if (rowsAffected > 0) {
                    Toast.makeText(edit_expense.this, "Expense updated", Toast.LENGTH_SHORT).show();
                    // back to home
                    Intent intentBack = new Intent(edit_expense.this, MainActivity.class);
                    intentBack.putExtra("selected_tab", R.id.menu_home);
                    startActivity(intentBack);
                    finish();
                } else {
                    Toast.makeText(edit_expense.this, "Error updating expense", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(edit_expense.this, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rowsDeleted = db.delete(ExpenseDBHelper.TABLE_NAME, "id = ?", new String[]{String.valueOf(transactionId)});
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Transaction deleted", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(edit_expense.this, MainActivity.class);
                back.putExtra("selected_tab", R.id.menu_home);
                startActivity(back);
                finish();
            } else {
                Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });


    }
//add_expense
    private void loadCategories() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ExpenseDBHelper.CATEGORY_TABLE, new String[]{"name"}, null, null, null, null, null);
        categoryList.clear();
        while (cursor.moveToNext()) {
            categoryList.add(cursor.getString(0));
        }
        cursor.close();
    }


}
