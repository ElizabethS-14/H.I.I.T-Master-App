package edu.bpi.hiitmasterapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Stores and retrieves the three user-selected goals (mirrors localStorage in Goals.jsx /
 * Dashboard.jsx). Uses SharedPreferences as the Android equivalent of localStorage.
 */
public class UserGoals {

    private static final String PREFS_NAME      = "fittrack_goals";
    private static final String KEY_FITNESS     = "Fitness Goal";
    private static final String KEY_COMMITMENT  = "Weekly Commitment";
    private static final String KEY_LEVEL       = "Experience Level";

    private String fitnessGoal;
    private String weeklyCommitment;
    private String experienceLevel;

    public UserGoals(String fitnessGoal, String weeklyCommitment, String experienceLevel) {
        this.fitnessGoal      = fitnessGoal;
        this.weeklyCommitment = weeklyCommitment;
        this.experienceLevel  = experienceLevel;
    }

    // --- Getters ---
    public String getFitnessGoal()      { return fitnessGoal; }
    public String getWeeklyCommitment() { return weeklyCommitment; }
    public String getExperienceLevel()  { return experienceLevel; }

    /** Returns true when all three goals have been set. */
    public boolean isComplete() {
        return fitnessGoal      != null && !fitnessGoal.isEmpty()
            && weeklyCommitment != null && !weeklyCommitment.isEmpty()
            && experienceLevel  != null && !experienceLevel.isEmpty();
    }

    // ------------------------------------------------------------------
    // SharedPreferences persistence (replaces localStorage in the React app)
    // ------------------------------------------------------------------

    /** Persist goals to SharedPreferences. */
    public void save(Context context) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit();
        editor.putString(KEY_FITNESS,    fitnessGoal);
        editor.putString(KEY_COMMITMENT, weeklyCommitment);
        editor.putString(KEY_LEVEL,      experienceLevel);
        editor.apply();
    }

    /**
     * Load goals from SharedPreferences.
     * Returns a UserGoals object; individual fields may be empty if not yet set.
     */
    public static UserGoals load(Context context) {
        //Clear Test
        UserGoals.clear(context);
        SharedPreferences prefs = context
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return new UserGoals(
                prefs.getString(KEY_FITNESS,    ""),
                prefs.getString(KEY_COMMITMENT, ""),
                prefs.getString(KEY_LEVEL,      "")
        );
    }

    /** Clear all saved goals (useful for testing / reset). */
    public static void clear(Context context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
               .edit().clear().apply();
    }
}
