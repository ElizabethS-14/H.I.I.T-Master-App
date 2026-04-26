package edu.bpi.hiitmasterapp;

import edu.bpi.hiitmasterapp.R;
import edu.bpi.hiitmasterapp.Exercise;
import edu.bpi.hiitmasterapp.RecentWorkout;
import edu.bpi.hiitmasterapp.Workout;

import java.util.ArrayList;
import java.util.List;

/**
 * Central data source that mirrors all the hard-coded arrays spread across
 * the React pages (Dashboard.jsx, Workouts.jsx, WorkoutDetail.jsx, ActiveWorkout.jsx).
 *
 * In a production app these would come from a Room database or a remote API.
 */
public class DataRepository {

    // ------------------------------------------------------------------
    // Workout Plans  (Workouts.jsx → workoutPlans)
    // ------------------------------------------------------------------
    public static List<Workout> getWorkoutPlans() {
        List<Workout> list = new ArrayList<>();
        list.add(new Workout(1, "Push Day",    "Chest · Shoulders · Triceps", "45 min", 380,  8, R.color.fit_red));
        list.add(new Workout(2, "Pull Day",    "Back · Biceps · Rear Delts",  "50 min", 420,  9, R.color.fit_blue));
        list.add(new Workout(3, "Leg Day",     "Quads · Hamstrings · Glutes", "55 min", 510,  7, R.color.purple_accent));
        list.add(new Workout(4, "HIIT Cardio", "Full Body Conditioning",       "30 min", 450,  6, R.color.orange_accent));
        list.add(new Workout(5, "Core & Abs",  "Core · Stability",            "25 min", 220,  8, R.color.green_accent));
        list.add(new Workout(6, "Upper Body",  "Chest · Back · Shoulders",    "48 min", 360, 10, R.color.fit_red));
        return list;
    }

    // ------------------------------------------------------------------
    // Push Day exercises  (WorkoutDetail.jsx / ActiveWorkout.jsx)
    // ------------------------------------------------------------------
    public static List<Exercise> getPushDayExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise(1, "Bench Press",            4, "8–10",  10, 80,  "90s", "Chest"));
        list.add(new Exercise(2, "Incline Dumbbell Press", 3, "10–12", 12, 30,  "60s", "Chest"));
        list.add(new Exercise(3, "Overhead Press",         4, "8",      8, 55,  "90s", "Shoulders"));
        list.add(new Exercise(4, "Lateral Raises",         3, "15",    15, 12,  "45s", "Shoulders"));
        list.add(new Exercise(5, "Tricep Pushdown",        3, "12–15", 12, 30,  "45s", "Triceps"));
        list.add(new Exercise(6, "Skull Crushers",         3, "10",    10, 25,  "60s", "Triceps"));
        list.add(new Exercise(7, "Cable Flyes",            3, "12–15", 12, 20,  "45s", "Chest"));
        list.add(new Exercise(8, "Face Pulls",             3, "15",    15, 18,  "45s", "Rear Delts"));
        return list;
    }

    // ------------------------------------------------------------------
    // Active-workout subset  (ActiveWorkout.jsx → exercises)
    // ------------------------------------------------------------------
    public static List<Exercise> getActiveWorkoutExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise(1, "Bench Press",     4, "8–10", 10, 80, "90s", "Chest"));
        list.add(new Exercise(2, "Overhead Press",  4, "8",     8, 55, "90s", "Shoulders"));
        list.add(new Exercise(3, "Tricep Pushdown", 3, "12–15",12, 30, "45s", "Triceps"));
        return list;
    }

    // ------------------------------------------------------------------
    // Recent workouts  (Dashboard.jsx → recentWorkouts)
    // ------------------------------------------------------------------
    public static List<RecentWorkout> getRecentWorkouts() {
        List<RecentWorkout> list = new ArrayList<>();
        list.add(new RecentWorkout("Upper Body Blast", "42 min", 380, "Today"));
        list.add(new RecentWorkout("Leg Day Power",    "55 min", 510, "Yesterday"));
        list.add(new RecentWorkout("HIIT Cardio",      "30 min", 420, "Mon"));
        return list;
    }

    // ------------------------------------------------------------------
    // Goal options  (Goals.jsx → goalOptions)
    // ------------------------------------------------------------------
    public static String[] getFitnessGoalOptions() {
        return new String[]{
            "Lose weight",
            "Build muscle",
            "Improve endurance",
            "Increase flexibility",
            "Stay active & healthy",
            "Train for a sport"
        };
    }

    public static String[] getCommitmentOptions() {
        return new String[]{
            "1–2 days / week",
            "3 days / week",
            "4 days / week",
            "5 days / week",
            "6–7 days / week"
        };
    }

    public static String[] getExperienceLevelOptions() {
        return new String[]{
            "Complete beginner",
            "Some experience",
            "Intermediate",
            "Advanced",
            "Athlete"
        };
    }

    // ------------------------------------------------------------------
    // Muscle-group filter chips  (WorkoutDetail.jsx → muscleGroups)
    // ------------------------------------------------------------------
    public static String[] getMuscleGroups() {
        return new String[]{ "All", "Chest", "Shoulders", "Triceps", "Rear Delts" };
    }

    // ------------------------------------------------------------------
    // AI coach motivational lines  (AISpeaker / AIChatFAB)
    // ------------------------------------------------------------------
    public static String[] getDashboardAiLines() {
        return new String[]{
            "Great job staying consistent! 💪",
            "You're 2 workouts away from your weekly goal 🔥",
            "Push Day is waiting — let's crush it! ⚡",
            "Recovery is just as important as training 🧘",
            "Every rep counts. Stay focused today 🎯"
        };
    }
}
