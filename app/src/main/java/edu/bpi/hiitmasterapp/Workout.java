package edu.bpi.hiitmasterapp;

/**
 * Represents a workout plan (e.g. Push Day, Pull Day).
 * Mirrors the workoutPlans array in Workouts.jsx.
 */
public class Workout {

    private final int id;
    private final String name;
    private final String muscleTag;
    private final String duration;
    private final int calories;
    private final int exerciseCount;
    private final int colorResId; // background color resource for the icon card

    public Workout(int id, String name, String muscleTag,
                   String duration, int calories, int exerciseCount, int colorResId) {
        this.id = id;
        this.name = name;
        this.muscleTag = muscleTag;
        this.duration = duration;
        this.calories = calories;
        this.exerciseCount = exerciseCount;
        this.colorResId = colorResId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getMuscleTag() { return muscleTag; }
    public String getDuration() { return duration; }
    public int getCalories() { return calories; }
    public int getExerciseCount() { return exerciseCount; }
    public int getColorResId() { return colorResId; }
}
