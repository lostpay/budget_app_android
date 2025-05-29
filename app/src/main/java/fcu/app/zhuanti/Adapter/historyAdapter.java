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
import fcu.app.zhuanti.model.history;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.ViewHolder> {
    private List<history> historyList;

    public historyAdapter(List<history> list) {
        this.historyList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        history history = historyList.get(position);
        holder.note.setText(history.getNote());
        holder.category.setText(history.getCategory());
        holder.amount.setText("$ " + history.getAmount());
        holder.imgcategory.setImageResource(history.getIconRes());

        // 格式化日期為 yyyy/MM/dd
        String rawDate = history.getDate();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        try {
            Date parsedDate = inputFormat.parse(rawDate);
            String formattedDate = outputFormat.format(parsedDate);
            holder.date.setText(formattedDate);
        } catch (ParseException e) {
            holder.date.setText(rawDate);
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView note, category, amount, date;
        ImageView imgcategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.tv_name);
            category = itemView.findViewById(R.id.tv_category);
            amount = itemView.findViewById(R.id.tv_amount);
            date = itemView.findViewById(R.id.tv_date);
            imgcategory = itemView.findViewById(R.id.iv_logo);
        }
    }
}
