package fcu.app.zhuanti;

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
import java.util.List;

import fcu.app.zhuanti.Adapter.historyAdapter;
import fcu.app.zhuanti.model.history; // ✅ 請加這行


public class historyFragment extends Fragment {
    private RecyclerView recyclerView;
    private historyAdapter adapter;
    private List<history> historyList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public historyFragment() {}

    public static historyFragment newInstance(String param1, String param2) {
        historyFragment fragment = new historyFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 初始化資料庫
        ExpenseDBHelper dbHelper = new ExpenseDBHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 讀取資料庫中所有支出資料
        Cursor cursor = db.rawQuery("SELECT * FROM " + ExpenseDBHelper.TABLE_NAME + " ORDER BY date DESC", null);
        historyList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String note = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_NOTE));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_CATEGORY));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_AMOUNT));

            int iconRes = getIconForCategory(category);
            historyList.add(new history(note, category, amount, iconRes));
        }
        cursor.close();

        adapter = new historyAdapter(historyList);
        recyclerView.setAdapter(adapter);

        return view;
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
