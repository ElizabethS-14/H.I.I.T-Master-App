package edu.bpi.hiitmasterapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.bpi.hiitmasterapp.R;
import edu.bpi.hiitmasterapp.RecentWorkoutAdapter;
import edu.bpi.hiitmasterapp.RecentWorkout;
import edu.bpi.hiitmasterapp.UserGoals;
import edu.bpi.hiitmasterapp.DataRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * DashboardActivity — mirrors Dashboard.jsx
 * Shows weekly goal, stats, AI coach message, recent workouts and bottom nav.
 */
public class DashboardActivity extends AppCompatActivity {


    private static final String[] AI_LINES = DataRepository.getDashboardAiLines();

    private int aiLineIndex = 0;
    private final Handler aiHandler = new Handler();
    private Runnable aiRunnable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setupGoalsSection();
        setupRecentWorkouts();
        setupTodayWorkoutCard();
        setupBottomNav();
        startAICoachCycle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh goals in case user edited them
        setupGoalsSection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (aiRunnable != null) aiHandler.removeCallbacks(aiRunnable);
    }



    private void setupGoalsSection() {
        UserGoals goals = UserGoals.load(this);

        setGoalChip(R.id.chip_fitness_goal,
                "Fitness Goal",
                goals.getFitnessGoal().isEmpty() ? "Build muscle" : goals.getFitnessGoal(),
                R.color.fit_red);

        setGoalChip(R.id.chip_commitment,
                "Commitment",
                goals.getWeeklyCommitment().isEmpty() ? "4 days / week" : goals.getWeeklyCommitment(),
                R.color.fit_blue);

        setGoalChip(R.id.chip_level,
                "Level",
                goals.getExperienceLevel().isEmpty() ? "Intermediate" : goals.getExperienceLevel(),
                R.color.white_60);

        // Edit goals tap
        View btnEdit = findViewById(R.id.btn_edit_goals);
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v ->
                startActivity(new Intent(this, GoalsActivity.class)));
        }
    }

    private void setGoalChip(int chipId, String label, String value, int colorRes) {
        View chip = findViewById(chipId);
        if (chip == null) return;

        TextView tvLabel = chip.findViewById(R.id.tv_label);
        TextView tvValue = chip.findViewById(R.id.tv_value);

        if (tvLabel != null) tvLabel.setText(label);
        if (tvValue != null) {
            tvValue.setText(value);
            tvValue.setTextColor(getResources().getColor(colorRes, getTheme()));
        }
    }



    private void setupRecentWorkouts() {
        List<RecentWorkout> workouts = DataRepository.getRecentWorkouts();

        RecyclerView rv = findViewById(R.id.rv_recent_workouts);
        if (rv == null) return;

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecentWorkoutAdapter(workouts));
        rv.setNestedScrollingEnabled(false);
    }



    private void setupTodayWorkoutCard() {
        View card = findViewById(R.id.card_today_workout);
        if (card != null) {
            card.setOnClickListener(v ->
                startActivity(new Intent(this, WorkoutDetailActivity.class)));
        }
    }


    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        if (nav == null) return;

        nav.setSelectedItemId(R.id.nav_home);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home)     return true; // already here
            if (id == R.id.nav_workouts) { openActivity(WorkoutsActivity.class);  return true; }
            if (id == R.id.nav_goals)    { openActivity(GoalsActivity.class);     return true; }
            if (id == R.id.nav_profile)  { openActivity(ProfileActivity.class);   return true; }
            return false;
        });
    }

    private void openActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        overridePendingTransition(0, 0);
    }



    private void startAICoachCycle() {
        TextView tvAI = findViewById(R.id.tv_ai_message);
        if (tvAI == null) return;

        aiRunnable = new Runnable() {
            @Override
            public void run() {
                tvAI.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                    tvAI.setText(AI_LINES[aiLineIndex % AI_LINES.length]);
                    aiLineIndex++;
                    tvAI.animate().alpha(1f).setDuration(300).start();
                }).start();
                aiHandler.postDelayed(this, 3500);
            }
        };
        aiHandler.postDelayed(aiRunnable, 3500);
    }
}
