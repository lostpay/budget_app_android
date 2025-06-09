package fcu.app.zhuanti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

public class goalDetail extends AppCompatActivity {
    private EditText editName, editCurrent, editTarget;
    private Button btnUpdate, btnDelete, btnBack;
    private long goalId;
    private GoalsDBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_goal_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linearLayout2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editName = findViewById(R.id.editGoalName);
        editCurrent = findViewById(R.id.editCurrentAmount);
        editTarget = findViewById(R.id.editTargetAmount);
        btnUpdate = findViewById(R.id.btnUpdateGoal);
        btnDelete = findViewById(R.id.btnDeleteGoal);
        ImageButton btnBack = findViewById(R.id.btn_goback);
        dbHelper = new GoalsDBHelper(this);

        // Get goal ID from intent
        goalId = getIntent().getLongExtra("goal_id", -1);

        // Load and display the goal info
        loadGoal();
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(goalDetail.this, MainActivity.class);
            intent.putExtra("selected_tab", R.id.menu_home);
            startActivity(intent);
            finish();
        });
        btnUpdate.setOnClickListener(v -> updateGoal());
        btnDelete.setOnClickListener(v -> confirmDeleteGoal());
    }
    private void loadGoal() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + GoalsDBHelper.TABLE_NAME + " WHERE " +
                GoalsDBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(goalId)});

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(GoalsDBHelper.COLUMN_NAME));
            double current = cursor.getDouble(cursor.getColumnIndexOrThrow(GoalsDBHelper.COLUMN_CURRENT_AMOUNT));
            double target = cursor.getDouble(cursor.getColumnIndexOrThrow(GoalsDBHelper.COLUMN_TARGET_AMOUNT));

            editName.setText(name);
            editCurrent.setText(String.valueOf(current));
            editTarget.setText(String.valueOf(target));
        }
        cursor.close();
        db.close();
    }

    private void updateGoal() {
        String name = editName.getText().toString().trim();
        String currentStr = editCurrent.getText().toString().trim();
        String targetStr = editTarget.getText().toString().trim();

        if (name.isEmpty() || currentStr.isEmpty() || targetStr.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        double current, target;
        try {
            current = Double.parseDouble(currentStr);
            target = Double.parseDouble(targetStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE " + GoalsDBHelper.TABLE_NAME +
                        " SET " + GoalsDBHelper.COLUMN_NAME + "=?, " +
                        GoalsDBHelper.COLUMN_CURRENT_AMOUNT + "=?, " +
                        GoalsDBHelper.COLUMN_TARGET_AMOUNT + "=? WHERE " +
                        GoalsDBHelper.COLUMN_ID + "=?",
                new Object[]{name, current, target, goalId});
        db.close();

        Toast.makeText(this, "Goal updated.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmDeleteGoal() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Goal")
                .setMessage("Are you sure you want to delete this goal?")
                .setPositiveButton("Yes", (dialog, which) -> deleteGoal())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteGoal() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(GoalsDBHelper.TABLE_NAME, GoalsDBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(goalId)});
        db.close();

        Toast.makeText(this, "Goal deleted.", Toast.LENGTH_SHORT).show();
        finish();
    }
}