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
import edu.bpi.hiitmasterapp.Exercise;

import java.util.List;

/**
 * Adapter for the exercise list in WorkoutDetailActivity.
 * Binds Exercise data to item_exercise.xml rows.
 * Completed exercises are styled with strikethrough text and a blue indicator.
 */
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

    private List<Exercise> exercises;

    public ExerciseAdapter(List<Exercise> exercises) {
        this.exercises = new java.util.ArrayList<>(exercises);
    }

    /** Replaces the displayed list (used by muscle-group filter chips). */
    public void updateList(List<Exercise> newList) {
        this.exercises = new java.util.ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise ex  = exercises.get(position);
        Context  ctx = holder.itemView.getContext();

        holder.tvMuscle.setText(ex.getMuscleGroup());
        holder.tvMeta.setText(ex.getMetaSummary());

        if (ex.isCompleted()) {
            // Blue indicator with checkmark
            holder.cvIndicator.setCardBackgroundColor(
                    ctx.getColor(R.color.fit_blue) & 0x33FFFFFF | 0x1A000000);
            holder.tvIndicator.setText("✓");
            holder.tvIndicator.setTextColor(ctx.getColor(R.color.fit_blue));

            // Strike-through + dim the name
            holder.tvName.setText(ex.getName());
            holder.tvName.setTextColor(ctx.getColor(R.color.white_40));
            holder.tvName.setPaintFlags(
                    holder.tvName.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.cvIndicator.setCardBackgroundColor(ctx.getColor(android.R.color.transparent));
            holder.tvIndicator.setText(String.valueOf(ex.getId()));
            holder.tvIndicator.setTextColor(ctx.getColor(R.color.white_40));

            holder.tvName.setText(ex.getName());
            holder.tvName.setTextColor(ctx.getColor(R.color.white));
            holder.tvName.setPaintFlags(
                    holder.tvName.getPaintFlags() & ~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    /** Mark a single exercise as done and refresh that row. */
    public void markCompleted(int position) {
        if (position >= 0 && position < exercises.size()) {
            exercises.get(position).setCompleted(true);
            notifyItemChanged(position);
        }
    }

    /** Returns how many exercises are completed (for the progress bar). */
    public int getCompletedCount() {
        int count = 0;
        for (Exercise e : exercises) if (e.isCompleted()) count++;
        return count;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvIndicator;
        TextView tvIndicator, tvName, tvMeta, tvMuscle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvIndicator = itemView.findViewById(R.id.cv_indicator);
            tvIndicator = itemView.findViewById(R.id.tv_indicator);
            tvName      = itemView.findViewById(R.id.tv_exercise_name);
            tvMeta      = itemView.findViewById(R.id.tv_exercise_meta);
            tvMuscle    = itemView.findViewById(R.id.tv_muscle);
        }
    }
}
