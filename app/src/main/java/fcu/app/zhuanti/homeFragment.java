package fcu.app.zhuanti;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class homeFragment extends Fragment {

    private FloatingActionButton add;
    private ExtendedFloatingActionButton add_expense, add_income;

    private Chip chip_day, chip_month, chip_year;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public homeFragment() {}

    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Floating Action Buttons
        add = view.findViewById(R.id.fab_add);
        add_expense = view.findViewById(R.id.fab_add_expense);
        add_income = view.findViewById(R.id.fab_add_income);

        // Chips
        chip_day = view.findViewById(R.id.chip_day);
        chip_month = view.findViewById(R.id.chip_month);
        chip_year = view.findViewById(R.id.chip_year);

        // Floating Button 展開收合
        final boolean[] isExpanded = {false};
        add.setOnClickListener(v -> {
            if (!isExpanded[0]) {
                add_income.setVisibility(View.VISIBLE);
                add_expense.setVisibility(View.VISIBLE);
                isExpanded[0] = true;
            } else {
                add_income.setVisibility(View.GONE);
                add_expense.setVisibility(View.GONE);
                isExpanded[0] = false;
            }
        });

        // FAB 點擊事件
        add_expense.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), add_expense.class));
        });

        add_income.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Add Income clicked", Toast.LENGTH_SHORT).show();
        });

        // Chip 點擊互斥高亮
        chip_day.setOnClickListener(v -> {
            resetChipColors();
            chip_day.setChecked(true);
            chip_day.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#800080")));
        });

        chip_month.setOnClickListener(v -> {
            resetChipColors();
            chip_month.setChecked(true);
            chip_month.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#800080")));
        });

        chip_year.setOnClickListener(v -> {
            resetChipColors();
            chip_year.setChecked(true);
            chip_year.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#800080")));
        });

        return view;
    }

    private void resetChipColors() {
        ColorStateList defaultColor = ColorStateList.valueOf(Color.LTGRAY);
        chip_day.setChipBackgroundColor(defaultColor);
        chip_month.setChipBackgroundColor(defaultColor);
        chip_year.setChipBackgroundColor(defaultColor);
        chip_day.setChecked(false);
        chip_month.setChecked(false);
        chip_year.setChecked(false);
    }
}
