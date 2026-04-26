package edu.bpi.hiitmasterapp;

/**
 * Represents a single exercise within a workout.
 * Mirrors the exercises array in WorkoutDetail.jsx / ActiveWorkout.jsx.
 */
public class Exercise {

    private int id;
    private String name;
    private int sets;
    private String reps;       // e.g. "8–10" (detail view display)
    private int repsValue;     // numeric reps used in active workout adjusters
    private int weightKg;      // starting weight in kg
    private String rest;       // e.g. "90s"
    private String muscleGroup;
    private boolean completed;

    public Exercise(int id, String name, int sets, String reps,
                    int repsValue, int weightKg, String rest, String muscleGroup) {
        this.id = id;
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.repsValue = repsValue;
        this.weightKg = weightKg;
        this.rest = rest;
        this.muscleGroup = muscleGroup;
        this.completed = false;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getName() { return name; }
    public int getSets() { return sets; }
    public String getReps() { return reps; }
    public int getRepsValue() { return repsValue; }
    public int getWeightKg() { return weightKg; }
    public String getRest() { return rest; }
    public String getMuscleGroup() { return muscleGroup; }
    public boolean isCompleted() { return completed; }

    // --- Setters ---
    public void setRepsValue(int repsValue) { this.repsValue = repsValue; }
    public void setWeightKg(int weightKg) { this.weightKg = weightKg; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    /** Returns "N sets · reps reps · Rest Xs" summary string. */
    public String getMetaSummary() {
        return sets + " sets · " + reps + " reps · Rest " + rest;
    }
}
