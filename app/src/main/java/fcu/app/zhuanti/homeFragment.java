package fcu.app.zhuanti;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
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

        add = view.findViewById(R.id.fab_add);
        add_expense = view.findViewById(R.id.fab_add_expense);
        add_income = view.findViewById(R.id.fab_add_income);

        chip_day = view.findViewById(R.id.chip_day);
        chip_month = view.findViewById(R.id.chip_month);
        chip_year = view.findViewById(R.id.chip_year);

        chip_day.setOnClickListener(v -> {
            chip_day.setChecked(true);
            chip_day.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#800080")));
        });

        chip_month.setOnClickListener(v -> {
            chip_month.setChecked(true);
            chip_month.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#800080")));
        });

        chip_year.setOnClickListener(v -> {
            chip_year.setChecked(true);
            chip_year.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#800080")));
        });

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

        add_expense.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), add_expense.class));
        });

        add_income.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Add Income clicked", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
