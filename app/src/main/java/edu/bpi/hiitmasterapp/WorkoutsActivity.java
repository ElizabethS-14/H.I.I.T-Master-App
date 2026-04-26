package edu.bpi.hiitmasterapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.bpi.hiitmasterapp.R;
import edu.bpi.hiitmasterapp.WorkoutAdapter;
import edu.bpi.hiitmasterapp.Workout;
import edu.bpi.hiitmasterapp.DataRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * WorkoutsActivity — mirrors Workouts.jsx
 * Displays a scrollable list of workout plans. Tapping any card opens WorkoutDetailActivity.
 */
public class WorkoutsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        setupWorkoutList();
        setupBottomNav();
    }

    // -----------------------------------------------------------------------
    // Workout list — mirrors workoutPlans array in Workouts.jsx
    // -----------------------------------------------------------------------

    private void setupWorkoutList() {
        List<Workout> plans = DataRepository.getWorkoutPlans();

        RecyclerView rv = findViewById(R.id.rv_workouts);
        if (rv == null) return;

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new WorkoutAdapter(plans, workout ->
            startActivity(new Intent(this, WorkoutDetailActivity.class))
        ));
    }

    // -----------------------------------------------------------------------
    // Bottom Navigation
    // -----------------------------------------------------------------------

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        if (nav == null) return;

        nav.setSelectedItemId(R.id.nav_workouts);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_workouts) return true;
            if (id == R.id.nav_home)    { openActivity(DashboardActivity.class); return true; }
            if (id == R.id.nav_goals)   { openActivity(GoalsActivity.class);     return true; }
            if (id == R.id.nav_profile) { openActivity(ProfileActivity.class);   return true; }
            return false;
        });
    }

    private void openActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        overridePendingTransition(0, 0);
    }
}
