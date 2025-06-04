package fcu.app.zhuanti;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import fcu.app.zhuanti.Adapter.RecentTransactionAdapter;
import fcu.app.zhuanti.model.HistoryTransaction;

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
        List<HistoryTransaction> recentList = loadRecentTransactions(); // method returns List<HistoryTransaction>

        RecyclerView recyclerView = view.findViewById(R.id.recyclerRecent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecentTransactionAdapter(recentList));


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
            startActivity(new Intent(getActivity(), add_income.class));
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

    //Load recent transactions

    private List<HistoryTransaction> loadRecentTransactions() {
        ExpenseDBHelper dbHelper = new ExpenseDBHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ExpenseDBHelper.TABLE_NAME + " ORDER BY date DESC LIMIT 7", null);

        List<HistoryTransaction> recentList = new ArrayList<>();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_ID));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_NOTE));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_CATEGORY));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_AMOUNT));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_DATE));
            int icon = getIconForCategory(category); // make sure this method exists
            recentList.add(new HistoryTransaction(id, note, category, amount, date, icon));
        }

        cursor.close();
        return recentList;
    }


    private int getIconForCategory(String category) {
        if (category == null) return R.drawable.games;
        switch (category) {
            case "飲食":
                return R.drawable.ic_food;
            case "交通":
                return R.drawable.ic_transport;
            case "娛樂":
                return R.drawable.ic_entertainment;
            case "醫療":
                return R.drawable.ic_health;
            case "購物":
                return R.drawable.ic_shopping;
            default:
                return R.drawable.games;
        }
    }

}
