package fcu.app.zhuanti;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;

public class mainscreen extends AppCompatActivity {
    Chip day,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mainscreen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        day=findViewById(R.id.chip_day);
        month=findViewById(R.id.chip_month);
        year=findViewById(R.id.chip_year);

        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day.setChecked(true);
                day.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#800080")));

            }
        });
    }
}