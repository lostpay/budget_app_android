package fcu.app.zhuanti;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class add_expense extends AppCompatActivity {

    private EditText etAmount, etNote, etDate;
    private ExpenseDBHelper dbHelper;

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

        // 連接 UI 元件
        etAmount = findViewById(R.id.et_nominal);
        etDate = findViewById(R.id.et_date);
        etNote = findViewById(R.id.editTextTextMultiLine);
        Button btnAdd = findViewById(R.id.btn_add);

        // 建立資料庫 helper
        dbHelper = new ExpenseDBHelper(this);

        // 點擊儲存按鈕
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountStr = etAmount.getText().toString().trim();
                String dateStr = etDate.getText().toString().trim();
                String noteStr = etNote.getText().toString().trim();

                // 基本驗證
                if (amountStr.isEmpty() || dateStr.isEmpty()) {
                    Toast.makeText(add_expense.this, "Please fill in amount and date", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double amount = Double.parseDouble(amountStr);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(ExpenseDBHelper.COLUMN_AMOUNT, amount);
                    values.put(ExpenseDBHelper.COLUMN_DATE, dateStr);
                    values.put(ExpenseDBHelper.COLUMN_NOTE, noteStr);
                    long newRowId = db.insert(ExpenseDBHelper.TABLE_NAME, null, values);

                    if (newRowId != -1) {
                        Toast.makeText(add_expense.this, "Expense added", Toast.LENGTH_SHORT).show();
                        etAmount.setText("");
                        etDate.setText("");
                        etNote.setText("");
                    } else {
                        Toast.makeText(add_expense.this, "Error adding expense", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(add_expense.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
