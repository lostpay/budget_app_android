package fcu.app.zhuanti;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fcu.app.zhuanti.Adapter.RecentTransactionAdapter;
import fcu.app.zhuanti.model.HistoryTransaction;

public class homeFragment extends Fragment {

    private FloatingActionButton add;
    private ExtendedFloatingActionButton add_expense, add_income;
    private Chip chip_day, chip_month, chip_year;

    public homeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerRecent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecentTransactionAdapter(loadRecentTransactions()));

        // UI 元件
        add = view.findViewById(R.id.fab_add);
        add_expense = view.findViewById(R.id.fab_add_expense);
        add_income = view.findViewById(R.id.fab_add_income);
        chip_day = view.findViewById(R.id.chip_day);
        chip_month = view.findViewById(R.id.chip_month);
        chip_year = view.findViewById(R.id.chip_year);

        // FAB 展開收合邏輯
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

        // 點擊跳轉新增收入/支出
        add_expense.setOnClickListener(v -> startActivity(new Intent(getActivity(), add_expense.class)));
        add_income.setOnClickListener(v -> startActivity(new Intent(getActivity(), add_income.class)));

        // 預設 Day 模式
        chip_day.setChecked(true);
        chip_day.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#800080")));
        updateBalance(view, "day");

        // Chip 點擊切換
        chip_day.setOnClickListener(v -> {
            resetChipColors();
            chip_day.setChecked(true);
            chip_day.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#800080")));
            updateBalance(view, "day");
        });

        chip_month.setOnClickListener(v -> {
            resetChipColors();
            chip_month.setChecked(true);
            chip_month.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#800080")));
            updateBalance(view, "month");
        });

        chip_year.setOnClickListener(v -> {
            resetChipColors();
            chip_year.setChecked(true);
            chip_year.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#800080")));
            updateBalance(view, "year");
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
            int icon = getIconForCategory(category);
            recentList.add(new HistoryTransaction(id, note, category, amount, date, icon));
        }
        cursor.close();
        return recentList;
    }

    private int getIconForCategory(String category) {
        if (category == null) return R.drawable.games;
        switch (category) {
            case "飲食": return R.drawable.ic_food;
            case "交通": return R.drawable.ic_transport;
            case "娛樂": return R.drawable.ic_entertainment;
            case "醫療": return R.drawable.ic_health;
            case "購物": return R.drawable.ic_shopping;
            case "薪水":
            case "獎金":
            case "股息":
            case "其他":
                return R.drawable.games;
            default:
                return R.drawable.games;
        }
    }

    private void updateBalance(View view, String mode) {
        TextView tvBalance = view.findViewById(R.id.tv_balance);
        TextView tvIncome = view.findViewById(R.id.tv_income);
        TextView tvExpense = view.findViewById(R.id.tv_expense);

        ExpenseDBHelper dbHelper = new ExpenseDBHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Calendar calendar = Calendar.getInstance();
        String whereClause = "";

        if (mode.equals("day")) {
            whereClause = String.format("date = '%04d-%02d-%02d'",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
        } else if (mode.equals("month")) {
            whereClause = String.format("date LIKE '%04d-%02d%%'",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1);
        } else if (mode.equals("year")) {
            whereClause = String.format("date LIKE '%04d%%'", calendar.get(Calendar.YEAR));
        }

        String query = String.format(
                "SELECT type, SUM(amount) as total FROM %s WHERE %s GROUP BY type",
                ExpenseDBHelper.TABLE_NAME, whereClause
        );

        Cursor cursor = db.rawQuery(query, null);

        double income = 0, expense = 0;
        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            if ("income".equals(type)) {
                income = total;
            } else if ("expense".equals(type)) {
                expense = total;
            }
        }
        cursor.close();

        double balance = income - expense;
        tvBalance.setText(String.format("$%.2f", balance));
        tvIncome.setText(String.format("+$%.2f", income));
        tvExpense.setText(String.format("-$%.2f", expense));
    }
}
