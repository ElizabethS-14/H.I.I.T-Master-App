package edu.bpi.hiitmasterapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.bpi.hiitmasterapp.R;
import edu.bpi.hiitmasterapp.RecentWorkout;

import java.util.List;

/**
 * Adapter for the "Recent Activity" RecyclerView on the Dashboard.
 * Binds RecentWorkout data to item_recent_workout.xml rows.
 */
public class RecentWorkoutAdapter extends RecyclerView.Adapter<RecentWorkoutAdapter.ViewHolder> {

    private final List<RecentWorkout> workouts;

    public RecentWorkoutAdapter(List<RecentWorkout> workouts) {
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentWorkout item = workouts.get(position);
        holder.tvName.setText(item.getName());
        holder.tvMeta.setText(item.getMetaString());
        holder.tvCalories.setText(String.valueOf(item.getCalories()));
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMeta, tvCalories;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName     = itemView.findViewById(R.id.tv_workout_name);
            tvMeta     = itemView.findViewById(R.id.tv_workout_meta);
            tvCalories = itemView.findViewById(R.id.tv_calories);
        }
    }
}
