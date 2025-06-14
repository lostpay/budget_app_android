package fcu.app.zhuanti;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fcu.app.zhuanti.Adapter.GroupedHistoryAdapter;
import fcu.app.zhuanti.model.HistoryItem;
import fcu.app.zhuanti.model.HistorySection;
import fcu.app.zhuanti.model.HistoryTransaction;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

public class historyFragment extends Fragment {
    private RecyclerView recyclerView;
    private GroupedHistoryAdapter adapter;
    private List<HistoryItem> groupedList;

    public historyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        PieChart pieChart = view.findViewById(R.id.pieChart);

        RecyclerView recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        groupedList = new ArrayList<>();
        loadGroupedData();

        GroupedHistoryAdapter adapter = new GroupedHistoryAdapter(groupedList, new GroupedHistoryAdapter.OnTransactionClickListener() {
            @Override
            public void onTransactionClick(HistoryTransaction transaction) {
                Intent intent = new Intent(getContext(), edit_expense.class);
                intent.putExtra("id", transaction.getId());
                intent.putExtra("note", transaction.getNote());
                intent.putExtra("category", transaction.getCategory());
                intent.putExtra("amount", transaction.getAmount());
                intent.putExtra("date", transaction.getDate());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        showExpensePieChart(pieChart);

        return view;
    }

    private void loadGroupedData() {
        ExpenseDBHelper dbHelper = new ExpenseDBHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ExpenseDBHelper.TABLE_NAME + " ORDER BY date DESC", null);

        Map<String, List<HistoryTransaction>> groupedMap = new LinkedHashMap<>();
        Map<String, Double> totalMap = new LinkedHashMap<>();

        while (cursor.moveToNext()) {
            String note = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_NOTE));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_CATEGORY));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_AMOUNT));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_DATE));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type")); // <-- GET TYPE

            if ("expense".equals(type)) {
                amount = -amount; // <-- CONVERT TO NEGATIVE
            }

            int iconRes = getIconForCategory(category);
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            HistoryTransaction transaction = new HistoryTransaction(id, note, category, amount, date, iconRes);

            if (!groupedMap.containsKey(date)) {
                groupedMap.put(date, new ArrayList<>());
                totalMap.put(date, 0.0);
            }

            groupedMap.get(date).add(transaction);
            totalMap.put(date, totalMap.get(date) + amount);
        }
        cursor.close();

        for (String date : groupedMap.keySet()) {
            groupedList.add(new HistorySection(date, totalMap.get(date)));
            groupedList.addAll(groupedMap.get(date));
        }
    }

    private void showExpensePieChart(PieChart pieChart) {
        ExpenseDBHelper dbHelper = new ExpenseDBHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + ExpenseDBHelper.COLUMN_CATEGORY + ", SUM(" + ExpenseDBHelper.COLUMN_AMOUNT + ") as total " +
                "FROM " + ExpenseDBHelper.TABLE_NAME +
                " WHERE type='expense' " +          // <--- Only expenses
                "GROUP BY " + ExpenseDBHelper.COLUMN_CATEGORY, null);


        ArrayList<PieEntry> entries = new ArrayList<>();

        while (cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_CATEGORY));
            float total = (float) cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            entries.add(new PieEntry(total, category));
        }
        cursor.close();

        PieDataSet dataSet = new PieDataSet(entries, "Expenses by Category");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(android.graphics.Color.TRANSPARENT);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.animateY(900);
        pieChart.invalidate();
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
