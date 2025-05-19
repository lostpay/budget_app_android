package fcu.app.zhuanti;

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
import fcu.app.zhuanti.model.history;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link historyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class historyFragment extends Fragment {
    private RecyclerView recyclerView;
    private historyAdapter adapter;
    private List<history> historyList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public historyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment history.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Initialize RecyclerView and data
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Dummy data for testing
        historyList = new ArrayList<>();
        historyList.add(new history("Sushi King", "gaming", 1000, R.drawable.games));
        historyList.add(new history("Pizza Mania", "electricity", 200, R.drawable.games));
        historyList.add(new history("Burger House", "food", 30, R.drawable.games));
        historyList.add(new history("Sushi King", "entertainment", 40, R.drawable.games));
        historyList.add(new history("Pizza Mania", "food", 50, R.drawable.games));
        historyList.add(new history("Burger House", "entertainment", 20, R.drawable.games));
        historyList.add(new history("Sushi King", "transport", 100, R.drawable.games));
        historyList.add(new history("Pizza Mania", "gaming", 500, R.drawable.games));
        historyList.add(new history("Burger House", "hospital", 44, R.drawable.games));

        adapter = new historyAdapter(historyList);
        recyclerView.setAdapter(adapter);

        return view;
    }


}