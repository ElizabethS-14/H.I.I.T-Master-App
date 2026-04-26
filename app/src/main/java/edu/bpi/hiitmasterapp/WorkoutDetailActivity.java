package edu.bpi.hiitmasterapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.bpi.hiitmasterapp.R;
import edu.bpi.hiitmasterapp.ExerciseAdapter;
import edu.bpi.hiitmasterapp.Exercise;
import edu.bpi.hiitmasterapp.DataRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * WorkoutDetailActivity — mirrors WorkoutDetail.jsx
 * Shows workout metadata, muscle-group filter chips, and the ordered exercise list.
 * The "Start Workout" button launches ActiveWorkoutActivity.
 */
public class WorkoutDetailActivity extends AppCompatActivity {

    // Muscle group filter chips — mirrors muscleGroups in WorkoutDetail.jsx
    private String[] MUSCLE_GROUPS;
    private String activeFilter = "All";

    private ExerciseAdapter exerciseAdapter;
    private List<Exercise>  allExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        MUSCLE_GROUPS = DataRepository.getMuscleGroups();
        allExercises = DataRepository.getPushDayExercises();

        setupBackButton();
        setupFilterChips();
        setupExerciseList();
        setupProgressBar();
        setupStartButton();
    }

    // -----------------------------------------------------------------------
    // Back button
    // -----------------------------------------------------------------------

    private void setupBackButton() {
        TextView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
    }

    // -----------------------------------------------------------------------
    // Filter chips — mirrors muscleGroups horizontal scroll in WorkoutDetail.jsx
    // -----------------------------------------------------------------------

    private void setupFilterChips() {
        LinearLayout container = findViewById(R.id.ll_filter_chips);
        if (container == null) return;

        for (String group : MUSCLE_GROUPS) {
            TextView chip = new TextView(this);
            chip.setText(group);
            chip.setTextSize(12f);
            chip.setTextColor(Color.parseColor("#80FFFFFF"));
            chip.setBackground(getResources().getDrawable(R.drawable.bg_pill, getTheme()));
            chip.setPadding(dpToPx(16), dpToPx(7), dpToPx(16), dpToPx(7));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMarginEnd(dpToPx(8));
            chip.setLayoutParams(params);

            chip.setOnClickListener(v -> applyFilter(group, container));
            container.addView(chip);
        }

        // Activate "All" chip by default
        activateChip((TextView) container.getChildAt(0));
    }

    private void applyFilter(String group, LinearLayout container) {
        activeFilter = group;
        // Update chip visuals
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof TextView) {
                TextView tv = (TextView) child;
                boolean active = tv.getText().toString().equals(group);
                if (active) activateChip(tv);
                else deactivateChip(tv);
            }
        }
        // Filter exercise list
        filterExercises(group);
    }

    private void activateChip(TextView chip) {
        chip.setBackground(getResources().getDrawable(R.drawable.bg_pill_red, getTheme()));
        chip.setTextColor(Color.WHITE);
    }

    private void deactivateChip(TextView chip) {
        chip.setBackground(getResources().getDrawable(R.drawable.bg_pill, getTheme()));
        chip.setTextColor(Color.parseColor("#80FFFFFF"));
    }

    // -----------------------------------------------------------------------
    // Exercise list
    // -----------------------------------------------------------------------

    private void setupExerciseList() {
        RecyclerView rv = findViewById(R.id.rv_exercises);
        if (rv == null) return;

        exerciseAdapter = new ExerciseAdapter(allExercises);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(exerciseAdapter);
        rv.setNestedScrollingEnabled(false);
    }

    private void filterExercises(String group) {
        if (exerciseAdapter == null) return;
        if (group.equals("All")) {
            exerciseAdapter.updateList(allExercises);
        } else {
            List<Exercise> filtered = new ArrayList<>();
            for (Exercise e : allExercises) {
                if (e.getMuscleGroup().equals(group)) filtered.add(e);
            }
            exerciseAdapter.updateList(filtered);
        }
    }

    // -----------------------------------------------------------------------
    // Progress bar — mirrors (doneCount / total) in WorkoutDetail.jsx
    // -----------------------------------------------------------------------

    private void setupProgressBar() {
        int done = 0;
        for (Exercise e : allExercises) if (e.isCompleted()) done++;

        int total   = allExercises.size();
        int percent = total > 0 ? (done * 100 / total) : 0;

        ProgressBar pb = findViewById(R.id.pb_workout);
        if (pb != null) pb.setProgress(percent);

        TextView tvCount = findViewById(R.id.tv_progress_count);
        if (tvCount != null) tvCount.setText(done + "/" + total);
    }

    // -----------------------------------------------------------------------
    // Start button
    // -----------------------------------------------------------------------

    private void setupStartButton() {
        Button btn = findViewById(R.id.btn_start_workout);
        if (btn != null) {
            btn.setOnClickListener(v ->
                startActivity(new Intent(this, ActiveWorkoutActivity.class)));
        }
    }

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
