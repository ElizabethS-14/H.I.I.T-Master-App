package edu.bpi.hiitmasterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import edu.bpi.hiitmasterapp.R;
import edu.bpi.hiitmasterapp.Workout;

import java.util.List;

/**
 * Adapter for the workout plan cards on WorkoutsActivity.
 * Binds Workout data to item_workout_card.xml.
 */
public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {

    public interface OnWorkoutClickListener {
        void onWorkoutClick(Workout workout);
    }

    private final List<Workout> workouts;
    private final OnWorkoutClickListener listener;

    public WorkoutAdapter(List<Workout> workouts, OnWorkoutClickListener listener) {
        this.workouts = workouts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Workout item = workouts.get(position);
        Context ctx  = holder.itemView.getContext();

        holder.tvName.setText(item.getName());
        holder.tvTag.setText(item.getMuscleTag());
        holder.tvDuration.setText("⏱ " + item.getDuration());
        holder.tvCalories.setText("🔥 " + item.getCalories() + " kcal");

        // Tint the icon card with each workout's accent colour
        holder.cvIcon.setCardBackgroundColor(ctx.getColor(item.getColorResId()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onWorkoutClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvIcon;
        TextView tvName, tvTag, tvDuration, tvCalories;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvIcon     = itemView.findViewById(R.id.cv_workout_icon);
            tvName     = itemView.findViewById(R.id.tv_workout_name);
            tvTag      = itemView.findViewById(R.id.tv_workout_tag);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvCalories = itemView.findViewById(R.id.tv_calories);
        }
    }
}
