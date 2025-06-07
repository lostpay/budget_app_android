package fcu.app.zhuanti.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import fcu.app.zhuanti.goalDetail;
import fcu.app.zhuanti.model.Goals;
import fcu.app.zhuanti.R;
public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

    private Context context;
    private List<Goals> goalList;

    public GoalAdapter(Context context, List<Goals> goalList) {
        this.context = context;
        this.goalList = goalList;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goals goal = goalList.get(position);
        holder.goalName.setText(goal.getName());
        holder.goalProgressText.setText(String.format("$%.0f / $%.0f", goal.getCurrentAmount(), goal.getTargetAmount()));

        int progress = goal.getTargetAmount() == 0 ? 0 : (int)((goal.getCurrentAmount() / goal.getTargetAmount()) * 100);
        holder.goalProgressBar.setProgress(progress);

        // Handle click: go to detail page
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, goalDetail.class);
            intent.putExtra("goal_id", goal.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView goalName, goalProgressText;
        ProgressBar goalProgressBar;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            goalName = itemView.findViewById(R.id.goalName);
            goalProgressText = itemView.findViewById(R.id.goalProgressText);
            goalProgressBar = itemView.findViewById(R.id.goalProgressBar);
        }
    }
}