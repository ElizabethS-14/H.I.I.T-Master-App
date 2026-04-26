package edu.bpi.hiitmasterapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import edu.bpi.hiitmasterapp.R;
import edu.bpi.hiitmasterapp.Exercise;
import edu.bpi.hiitmasterapp.DataRepository;

import java.util.List;
import java.util.Locale;

/**
 * ActiveWorkoutActivity — mirrors ActiveWorkout.jsx
 * Manages the live workout session: elapsed timer, per-exercise set tracking,
 * rep/weight adjusters, rest countdown, and exercise navigation.
 */
public class ActiveWorkoutActivity extends AppCompatActivity {

    // -----------------------------------------------------------------------
    // Exercises — mirrors the exercises array in ActiveWorkout.jsx
    // -----------------------------------------------------------------------
    private List<Exercise> exercises;

    // State
    private int     currentExerciseIndex = 0;
    private int     currentSetIndex      = 0;
    private boolean[][] completedSets;   // [exerciseIdx][setIdx]

    // Elapsed workout timer
    private int     elapsedSeconds = 0;
    private boolean timerRunning   = true;
    private final Handler timerHandler = new Handler();
    private Runnable timerRunnable;

    // Rest countdown
    private CountDownTimer restCountDown;
    private int            restSecondsLeft = 0;

    // -----------------------------------------------------------------------
    // UI references
    // -----------------------------------------------------------------------
    private TextView     tvTimer;
    private TextView     tvPauseIcon;
    private TextView     tvSetsBadge;
    private TextView     tvExerciseNumber;
    private TextView     tvExerciseName;
    private TextView     tvReps;
    private TextView     tvWeight;
    private TextView     tvSetsDone;
    private TextView     tvSetsRemaining;
    private ProgressBar  pbOverall;
    private LinearLayout llSetsTracker;
    private LinearLayout llRestTimer;
    private TextView     tvRestTimer;
    private LinearLayout llExerciseTabs;

    // -----------------------------------------------------------------------
    // Lifecycle
    // -----------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_workout);

        exercises = buildExerciseList();
        initCompletedSets();
        bindViews();
        setupButtons();
        buildExerciseTabs();
        updateUI();
        startElapsedTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopElapsedTimer();
        if (restCountDown != null) restCountDown.cancel();
    }

    // -----------------------------------------------------------------------
    // View binding
    // -----------------------------------------------------------------------

    private void bindViews() {
        tvTimer          = findViewById(R.id.tv_timer);
        tvPauseIcon      = findViewById(R.id.tv_pause_icon);
        tvSetsBadge      = findViewById(R.id.tv_set_badge);
        tvExerciseNumber = findViewById(R.id.tv_exercise_number);
        tvExerciseName   = findViewById(R.id.tv_exercise_name);
        tvReps           = findViewById(R.id.tv_reps);
        tvWeight         = findViewById(R.id.tv_weight);
        tvSetsDone       = findViewById(R.id.tv_sets_done);
        tvSetsRemaining  = findViewById(R.id.tv_sets_remaining);
        pbOverall        = findViewById(R.id.pb_overall);
        llSetsTracker    = findViewById(R.id.ll_sets_tracker);
        llRestTimer      = findViewById(R.id.ll_rest_timer);
        tvRestTimer      = findViewById(R.id.tv_rest_timer);
        llExerciseTabs   = findViewById(R.id.ll_exercise_tabs);
    }

    // -----------------------------------------------------------------------
    // Button wiring
    // -----------------------------------------------------------------------

    private void setupButtons() {
        // Close
        CardView btnClose = findViewById(R.id.btn_close);
        if (btnClose != null) btnClose.setOnClickListener(v -> finish());

        // Pause / Resume toggle
        CardView btnPause = findViewById(R.id.btn_pause);
        if (btnPause != null) {
            btnPause.setOnClickListener(v -> {
                timerRunning = !timerRunning;
                if (tvPauseIcon != null) tvPauseIcon.setText(timerRunning ? "⏸" : "▶");
            });
        }

        // Reps adjusters
        TextView btnRepsMinus = findViewById(R.id.btn_reps_minus);
        TextView btnRepsPlus  = findViewById(R.id.btn_reps_plus);
        if (btnRepsMinus != null) btnRepsMinus.setOnClickListener(v -> adjustReps(-1));
        if (btnRepsPlus  != null) btnRepsPlus.setOnClickListener(v -> adjustReps(1));

        // Weight adjusters
        TextView btnWeightMinus = findViewById(R.id.btn_weight_minus);
        TextView btnWeightPlus  = findViewById(R.id.btn_weight_plus);
        if (btnWeightMinus != null) btnWeightMinus.setOnClickListener(v -> adjustWeight(-1));
        if (btnWeightPlus  != null) btnWeightPlus.setOnClickListener(v -> adjustWeight(1));

        // Complete Set
        Button btnCompleteSet = findViewById(R.id.btn_complete_set);
        if (btnCompleteSet != null) btnCompleteSet.setOnClickListener(v -> completeCurrentSet());

        // Prev / Next exercise
        CardView btnPrev = findViewById(R.id.btn_prev_exercise);
        CardView btnNext = findViewById(R.id.btn_next_exercise);
        if (btnPrev != null) btnPrev.setOnClickListener(v -> navigateExercise(-1));
        if (btnNext != null) btnNext.setOnClickListener(v -> navigateExercise(1));

        // Skip rest
        Button btnSkipRest = findViewById(R.id.btn_skip_rest);
        if (btnSkipRest != null) btnSkipRest.setOnClickListener(v -> cancelRestTimer());
    }

    // -----------------------------------------------------------------------
    // Exercise tabs (horizontal pill buttons) — mirrors exercise navigator tabs
    // -----------------------------------------------------------------------

    private void buildExerciseTabs() {
        if (llExerciseTabs == null) return;
        llExerciseTabs.removeAllViews();

        for (int i = 0; i < exercises.size(); i++) {
            final int idx = i;
            TextView tab = new TextView(this);
            tab.setText(exercises.get(i).getName());
            tab.setTextSize(12f);
            tab.setTextColor(i == 0 ? Color.WHITE : Color.parseColor("#66FFFFFF"));
            tab.setBackground(getResources().getDrawable(
                    i == 0 ? R.drawable.bg_pill_blue : R.drawable.bg_pill, getTheme()));
            tab.setPadding(dpToPx(14), dpToPx(7), dpToPx(14), dpToPx(7));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(dpToPx(8));
            tab.setLayoutParams(params);

            tab.setOnClickListener(v -> goToExercise(idx));
            tab.setTag("tab_" + i);
            llExerciseTabs.addView(tab);
        }
    }

    private void refreshExerciseTabs() {
        if (llExerciseTabs == null) return;
        for (int i = 0; i < llExerciseTabs.getChildCount(); i++) {
            View child = llExerciseTabs.getChildAt(i);
            if (child instanceof TextView) {
                boolean active = (i == currentExerciseIndex);
                ((TextView) child).setTextColor(active ? Color.WHITE : Color.parseColor("#66FFFFFF"));
                child.setBackground(getResources().getDrawable(
                        active ? R.drawable.bg_pill_blue : R.drawable.bg_pill, getTheme()));
            }
        }
    }

    // -----------------------------------------------------------------------
    // Exercise navigation — mirrors goToEx() in ActiveWorkout.jsx
    // -----------------------------------------------------------------------

    private void goToExercise(int index) {
        if (index < 0 || index >= exercises.size()) return;
        currentExerciseIndex = index;
        currentSetIndex      = 0;
        updateUI();
        refreshExerciseTabs();
    }

    private void navigateExercise(int delta) {
        goToExercise(currentExerciseIndex + delta);
    }

    // -----------------------------------------------------------------------
    // Set completion — mirrors completeSet() in ActiveWorkout.jsx
    // -----------------------------------------------------------------------

    private void completeCurrentSet() {
        if (completedSets[currentExerciseIndex][currentSetIndex]) return;

        completedSets[currentExerciseIndex][currentSetIndex] = true;

        // Advance set or exercise
        Exercise current = exercises.get(currentExerciseIndex);
        if (currentSetIndex + 1 < current.getSets()) {
            currentSetIndex++;
        } else if (currentExerciseIndex + 1 < exercises.size()) {
            currentExerciseIndex++;
            currentSetIndex = 0;
            refreshExerciseTabs();
        }

        updateUI();
        startRestTimer(60);
    }

    // -----------------------------------------------------------------------
    // Reps / Weight adjusters
    // -----------------------------------------------------------------------

    private void adjustReps(int delta) {
        Exercise ex = exercises.get(currentExerciseIndex);
        int newVal = Math.max(1, ex.getRepsValue() + delta);
        ex.setRepsValue(newVal);
        if (tvReps != null) tvReps.setText(String.valueOf(newVal));
    }

    private void adjustWeight(int delta) {
        Exercise ex = exercises.get(currentExerciseIndex);
        int newVal = Math.max(0, ex.getWeightKg() + delta);
        ex.setWeightKg(newVal);
        if (tvWeight != null) tvWeight.setText(newVal + "kg");
    }

    // -----------------------------------------------------------------------
    // UI update — syncs all visible components to current state
    // -----------------------------------------------------------------------

    private void updateUI() {
        Exercise ex = exercises.get(currentExerciseIndex);

        // Labels
        if (tvExerciseNumber != null)
            tvExerciseNumber.setText("EXERCISE " + (currentExerciseIndex + 1) + "/" + exercises.size());
        if (tvSetsBadge != null)
            tvSetsBadge.setText("Set " + (currentSetIndex + 1) + "/" + ex.getSets());
        if (tvExerciseName != null) tvExerciseName.setText(ex.getName());
        if (tvReps   != null) tvReps.setText(String.valueOf(ex.getRepsValue()));
        if (tvWeight != null) tvWeight.setText(ex.getWeightKg() + "kg");

        // Overall progress
        int totalSets     = countTotalSets();
        int completedCount = countCompletedSets();
        int remaining     = totalSets - completedCount;

        if (tvSetsDone      != null) tvSetsDone.setText(completedCount + " sets done");
        if (tvSetsRemaining != null) tvSetsRemaining.setText(remaining + " remaining");
        if (pbOverall       != null)
            pbOverall.setProgress(totalSets > 0 ? (completedCount * 100 / totalSets) : 0);

        // Per-exercise set dots
        buildSetsDots(ex);
    }

    // -----------------------------------------------------------------------
    // Sets tracker dots — mirrors the coloured set progress bars in ActiveWorkout.jsx
    // -----------------------------------------------------------------------

    private void buildSetsDots(Exercise ex) {
        if (llSetsTracker == null) return;
        llSetsTracker.removeAllViews();

        for (int i = 0; i < ex.getSets(); i++) {
            View dot = new View(this);
            dot.setBackgroundColor(
                    completedSets[currentExerciseIndex][i] ? Color.parseColor("#FF2979FF") // fit_blue
                    : i == currentSetIndex                 ? Color.parseColor("#66FFFFFF")
                    :                                        Color.parseColor("#1AFFFFFF")
            );
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, dpToPx(8), 1f);
            p.setMarginEnd(dpToPx(4));
            dot.setLayoutParams(p);
            // Rounded corners
            dot.setBackground(createRoundedDrawable(
                    completedSets[currentExerciseIndex][i] ? "#FF2979FF"
                    : i == currentSetIndex                 ? "#66FFFFFF"
                    :                                        "#1AFFFFFF"
            ));
            llSetsTracker.addView(dot);
        }
    }

    private android.graphics.drawable.GradientDrawable createRoundedDrawable(String colorHex) {
        android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
        gd.setColor(Color.parseColor(colorHex));
        gd.setCornerRadius(dpToPx(8));
        return gd;
    }

    // -----------------------------------------------------------------------
    // Elapsed workout timer — mirrors useTimer() in ActiveWorkout.jsx
    // -----------------------------------------------------------------------

    private void startElapsedTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (timerRunning) {
                    elapsedSeconds++;
                    if (tvTimer != null) tvTimer.setText(formatTime(elapsedSeconds));
                }
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private void stopElapsedTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    private String formatTime(int totalSeconds) {
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", mins, secs);
    }

    // -----------------------------------------------------------------------
    // Rest countdown timer — mirrors restTimer state in ActiveWorkout.jsx
    // -----------------------------------------------------------------------

    private void startRestTimer(int seconds) {
        if (restCountDown != null) restCountDown.cancel();
        restSecondsLeft = seconds;
        showRestTimer(true);

        restCountDown = new CountDownTimer(seconds * 1000L, 1000) {
            @Override public void onTick(long millisUntilFinished) {
                restSecondsLeft = (int) (millisUntilFinished / 1000);
                if (tvRestTimer != null) tvRestTimer.setText(restSecondsLeft + "s");
            }
            @Override public void onFinish() {
                showRestTimer(false);
            }
        }.start();
    }

    private void cancelRestTimer() {
        if (restCountDown != null) restCountDown.cancel();
        showRestTimer(false);
    }

    private void showRestTimer(boolean visible) {
        if (llRestTimer != null)
            llRestTimer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private void initCompletedSets() {
        completedSets = new boolean[exercises.size()][];
        for (int i = 0; i < exercises.size(); i++) {
            completedSets[i] = new boolean[exercises.get(i).getSets()];
        }
    }

    private int countTotalSets() {
        int total = 0;
        for (Exercise e : exercises) total += e.getSets();
        return total;
    }

    private int countCompletedSets() {
        int count = 0;
        for (boolean[] exSets : completedSets)
            for (boolean done : exSets)
                if (done) count++;
        return count;
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    // -----------------------------------------------------------------------
    // Exercise data — mirrors exercises in ActiveWorkout.jsx
    // -----------------------------------------------------------------------

    private List<Exercise> buildExerciseList() {
        return DataRepository.getActiveWorkoutExercises();
    }
}
