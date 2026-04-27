package edu.bpi.hiitmasterapp;

/**
 * Represents a completed workout entry shown in the Dashboard recent activity list.
 * Mirrors the recentWorkouts array in Dashboard.jsx.
 */
public class RecentWorkout {

    private final String name;
    private final String duration;
    private final int calories;
    private final String dateLabel; // e.g. "Today", "Yesterday", "Mon"

    public RecentWorkout(String name, String duration, int calories, String dateLabel) {
        this.name = name;
        this.duration = duration;
        this.calories = calories;
        this.dateLabel = dateLabel;
    }

    public String getName() { return name; }
    public String getDuration() { return duration; }
    public int getCalories() { return calories; }
    public String getDateLabel() { return dateLabel; }

    /** Returns the combined meta string shown beneath the workout name. */
    public String getMetaString() {
        return dateLabel + " · " + duration;
    }
}
