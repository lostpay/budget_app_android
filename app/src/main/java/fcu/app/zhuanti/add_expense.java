
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

    public class add_expense extends AppCompatActivity {

        private EditText etAmount, etNote, etDate, etNewCategory;
        private Spinner spinnerCategory;
        //private ImageButton btnBack;
        private ExpenseDBHelper dbHelper;
        private ArrayAdapter<String> categoryAdapter;
        private List<String> categoryList = new ArrayList<>();
        private TextView tvAddNewExpense;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_add_expense);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            etAmount = findViewById(R.id.et_nominal);
            etDate = findViewById(R.id.et_date);
            etNote = findViewById(R.id.editTextTextMultiLine);
            etNewCategory = findViewById(R.id.et_new_category);
            spinnerCategory = findViewById(R.id.spinner_category);
            ImageButton btnBack = findViewById(R.id.btn_back);
            Button btnAdd = findViewById(R.id.btn_edit);
            Button btnAddCategory = findViewById(R.id.btn_add_category);
            tvAddNewExpense = findViewById(R.id.tv_add_expense);

            dbHelper = new ExpenseDBHelper(this);
            loadCategories();

            categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(categoryAdapter);

            etDate.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(add_expense.this,
                        (view, year1, month1, dayOfMonth) -> {
                            String dateFormatted = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                            etDate.setText(dateFormatted);
                        }, year, month, day);
                datePickerDialog.show();
            });

            btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(add_expense.this, MainActivity.class);
                intent.putExtra("selected_tab", R.id.menu_home);
                startActivity(intent);
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
                String category = spinnerCategory.getSelectedItem().toString();

                if (amountStr.isEmpty() || dateStr.isEmpty()) {
                    Toast.makeText(add_expense.this, "Please fill in amount and date", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double amount = Double.parseDouble(amountStr);
                    amount = Math.abs(amount);


                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(ExpenseDBHelper.COLUMN_AMOUNT, amount);
                    values.put(ExpenseDBHelper.COLUMN_DATE, dateStr);
                    values.put(ExpenseDBHelper.COLUMN_NOTE, noteStr);
                    values.put(ExpenseDBHelper.COLUMN_TYPE, "expense");
                    values.put(ExpenseDBHelper.COLUMN_CATEGORY, category);

                    long newRowId = db.insert(ExpenseDBHelper.TABLE_NAME, null, values);

                    if (newRowId != -1) {
                        Toast.makeText(add_expense.this, "支出已新增", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(add_expense.this, MainActivity.class);
                        intent.putExtra("selected_tab", R.id.menu_home);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(add_expense.this, "新增失敗", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(add_expense.this, "金額格式錯誤", Toast.LENGTH_SHORT).show();
                }

            });


        }

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
