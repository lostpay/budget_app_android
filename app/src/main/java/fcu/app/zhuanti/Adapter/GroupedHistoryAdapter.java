package fcu.app.zhuanti.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import fcu.app.zhuanti.R;
import fcu.app.zhuanti.model.HistoryItem;
import fcu.app.zhuanti.model.HistorySection;
import fcu.app.zhuanti.model.HistoryTransaction;

public class GroupedHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_SECTION = 0;
    private static final int TYPE_ITEM = 1;

    private final List<HistoryItem> itemList;

    public GroupedHistoryAdapter(List<HistoryItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof HistorySection) {
            return TYPE_SECTION;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SECTION) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.section_header, parent, false);
            return new SectionViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryItem item = itemList.get(position);

        if (holder instanceof SectionViewHolder) {
            HistorySection section = (HistorySection) item;
            ((SectionViewHolder) holder).tvDate.setText(section.getDate());
            ((SectionViewHolder) holder).tvTotal.setText(formatAmount(section.getTotalAmount()));
        } else if (holder instanceof ItemViewHolder) {
            HistoryTransaction transaction = (HistoryTransaction) item;
            ((ItemViewHolder) holder).tvNote.setText(transaction.getNote());
            ((ItemViewHolder) holder).tvCategory.setText(transaction.getCategory());
            ((ItemViewHolder) holder).tvAmount.setText(formatAmount(transaction.getAmount()));
            ((ItemViewHolder) holder).tvDate.setText(transaction.getDate());
            ((ItemViewHolder) holder).ivIcon.setImageResource(transaction.getIconRes());
        }
    }

    private String formatAmount(double amount) {
        DecimalFormat df = new DecimalFormat("+#,##0;-#,##0");
        return df.format(amount);
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTotal;

        SectionViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_section_date);
            tvTotal = itemView.findViewById(R.id.tv_section_total);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvNote, tvCategory, tvAmount, tvDate;
        ImageView ivIcon;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvNote = itemView.findViewById(R.id.tv_name);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivIcon = itemView.findViewById(R.id.iv_logo);
        }
    }
}
