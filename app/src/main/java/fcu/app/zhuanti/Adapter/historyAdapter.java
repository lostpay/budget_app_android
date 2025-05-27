    package fcu.app.zhuanti.Adapter;

    import fcu.app.zhuanti.model.history;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.List;

    import fcu.app.zhuanti.R;

    public class historyAdapter extends RecyclerView.Adapter<historyAdapter.ViewHolder>{
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
            holder.name.setText(history.getNote()); // ✅ 這是 history.java 中的正確方法
            holder.category.setText(history.getCategory());
            holder.amount.setText("$ " + history.getAmount());
            holder.imgcategory.setImageResource(history.getIconRes());
        }


        @Override
        public int getItemCount() {
            return historyList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, category, amount;
            ImageView imgcategory;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.tv_name);
                category = itemView.findViewById(R.id.tv_category);
                amount = itemView.findViewById(R.id.tv_amount);
                imgcategory = itemView.findViewById(R.id.iv_logo);
            }
        }
    }
