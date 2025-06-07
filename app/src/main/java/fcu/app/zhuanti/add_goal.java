package fcu.app.zhuanti;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class add_goal extends AppCompatActivity {
    private EditText editName, editCurrent, editTarget;
    private Button btnSave;
    private GoalsDBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_goal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editName = findViewById(R.id.editGoalName);
        editCurrent = findViewById(R.id.editCurrentAmount);
        editTarget = findViewById(R.id.editTargetAmount);
        btnSave = findViewById(R.id.btnSaveGoal);
        dbHelper = new GoalsDBHelper(this);

        btnSave.setOnClickListener(v -> saveGoal());
    }
    private void saveGoal() {
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

        dbHelper.getWritableDatabase().execSQL(
                "INSERT INTO " + GoalsDBHelper.TABLE_NAME + " (name, currentAmount, targetAmount) VALUES (?, ?, ?)",
                new Object[]{name, current, target}
        );

        Toast.makeText(this, "Goal saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}