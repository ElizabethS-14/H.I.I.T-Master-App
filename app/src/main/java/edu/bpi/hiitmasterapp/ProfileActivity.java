package edu.bpi.hiitmasterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.bpi.hiitmasterapp.R;
import edu.bpi.hiitmasterapp.UserGoals;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * ProfileActivity — mirrors Profile.jsx
 * Shows user avatar, stats row, active goals summary, and settings menu items.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupGoalsSummary();
        setupMenuRows();
        setupBottomNav();
    }

    // -----------------------------------------------------------------------
    // Goals Summary — mirrors the goals object from localStorage in Profile.jsx
    // -----------------------------------------------------------------------

    private void setupGoalsSummary() {
        UserGoals goals = UserGoals.load(this);
        LinearLayout llGoals = findViewById(R.id.ll_goals_list);
        if (llGoals == null) return;
        llGoals.removeAllViews();

        if (!goals.isComplete()) return; // nothing to show

        String[][] entries = {
            {"Fitness Goal",       goals.getFitnessGoal()},
            {"Weekly Commitment",  goals.getWeeklyCommitment()},
            {"Experience Level",   goals.getExperienceLevel()},
        };

        for (String[] entry : entries) {
            View row = buildGoalRow(entry[0], entry[1]);
            llGoals.addView(row);
        }
    }

    /** Inflates a horizontal key-value row for the goals summary card. */
    private View buildGoalRow(String key, String value) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        rowParams.bottomMargin = dpToPx(10);
        row.setLayoutParams(rowParams);

        // Key
        TextView tvKey = new TextView(this);
        tvKey.setText(key);
        tvKey.setTextColor(getResources().getColor(R.color.white_40, getTheme()));
        tvKey.setTextSize(12f);
        LinearLayout.LayoutParams keyParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        tvKey.setLayoutParams(keyParams);

        // Value
        TextView tvVal = new TextView(this);
        tvVal.setText(value);
        tvVal.setTextColor(getResources().getColor(R.color.white, getTheme()));
        tvVal.setTextSize(12f);
        tvVal.setTypeface(null, android.graphics.Typeface.BOLD);

        row.addView(tvKey);
        row.addView(tvVal);
        return row;
    }

    // -----------------------------------------------------------------------
    // Menu rows — mirrors menuItems in Profile.jsx
    // -----------------------------------------------------------------------

    private void setupMenuRows() {
        Object[][] menuData = {
            {R.id.menu_notifications, "🔔", "Notifications",  "Reminders & alerts"},
            {R.id.menu_preferences,  "⚙️", "Preferences",    "Units, language"},
            {R.id.menu_privacy,      "🔒", "Privacy",         "Data & security"},
        };

        for (Object[] data : menuData) {
            int    id    = (int)    data[0];
            String icon  = (String) data[1];
            String label = (String) data[2];
            String sub   = (String) data[3];

            View row = findViewById(id);
            if (row == null) continue;

            TextView tvIcon  = row.findViewById(R.id.tv_menu_icon);
            TextView tvLabel = row.findViewById(R.id.tv_menu_label);
            TextView tvSub   = row.findViewById(R.id.tv_menu_sub);

            if (tvIcon  != null) tvIcon.setText(icon);
            if (tvLabel != null) tvLabel.setText(label);
            if (tvSub   != null) tvSub.setText(sub);
        }
    }

    // -----------------------------------------------------------------------
    // Bottom Navigation
    // -----------------------------------------------------------------------

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        if (nav == null) return;

        nav.setSelectedItemId(R.id.nav_profile);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile)  return true;
            if (id == R.id.nav_home)     { openActivity(DashboardActivity.class); return true; }
            if (id == R.id.nav_workouts) { openActivity(WorkoutsActivity.class);  return true; }
            if (id == R.id.nav_goals)    { openActivity(GoalsActivity.class);     return true; }
            return false;
        });
    }

    private void openActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        overridePendingTransition(0, 0);
    }

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
