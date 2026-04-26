package edu.bpi.hiitmasterapp;

import android.content.Context;
import android.util.TypedValue;
import edu.bpi.hiitmasterapp.R;
import java.util.Locale;

/**
 * AppUtils — shared utility methods used across activities and adapters.
 */
public final class AppUtils {

    private AppUtils() { /* no instances */ }

    /**
     * Converts dp to pixels.
     */
    public static int dpToPx(Context context, float dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        ));
    }

    /**
     * Formats a seconds count as MM:SS — mirrors the fmt() function in ActiveWorkout.jsx.
     */
    public static String formatElapsedTime(int totalSeconds) {
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", mins, secs);
    }

    /**
     * Returns a friendly calorie string (e.g. "380 kcal").
     */
    public static String formatCalories(int calories) {
        return calories + " kcal";
    }

    /**
     * Clamps an integer value between min and max (inclusive).
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
