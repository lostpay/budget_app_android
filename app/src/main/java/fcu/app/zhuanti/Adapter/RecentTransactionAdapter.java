package fcu.app.zhuanti.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fcu.app.zhuanti.R;
import fcu.app.zhuanti.model.HistoryTransaction;
import fcu.app.zhuanti.model.history;

public class RecentTransactionAdapter extends RecyclerView.Adapter<RecentTransactionAdapter.ViewHolder> {
    private List<HistoryTransaction> recentList;

    public RecentTransactionAdapter(List<HistoryTransaction> list) {
        this.recentList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView note, category, amount, date;
        ImageView icon;

        public ViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.tv_name);
            category = view.findViewById(R.id.tv_category);
            amount = view.findViewById(R.id.tv_amount);
            date = view.findViewById(R.id.tv_date);
            icon = view.findViewById(R.id.iv_logo);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryTransaction item = recentList.get(position);
        holder.note.setText(item.getNote());
        holder.category.setText(item.getCategory());
        holder.amount.setText("$ " + item.getAmount());
        holder.date.setText(item.getDate());
        holder.icon.setImageResource(item.getIconRes());
    }

    @Override
    public int getItemCount() {
        return recentList.size();
    }
}