package edu.bpi.hiitmasterapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.bpi.hiitmasterapp.R;
import edu.bpi.hiitmasterapp.UserGoals;
import edu.bpi.hiitmasterapp.DataRepository;

import java.util.Arrays;
import java.util.List;

/**
 * GoalsActivity — mirrors Goals.jsx
 * Lets the user pick 3 goals via in-card dropdowns.
 * Saves to SharedPreferences (≡ localStorage) and navigates to Dashboard.
 */
public class GoalsActivity extends AppCompatActivity {

    // -----------------------------------------------------------------------
    // Option data — mirrors goalOptions in Goals.jsx (sourced from DataRepository)
    // -----------------------------------------------------------------------
    private static List<String> FITNESS_OPTIONS;
    private static List<String> COMMITMENT_OPTIONS;
    private static List<String> LEVEL_OPTIONS;

    // Currently selected values
    private String selectedFitness    = "";
    private String selectedCommitment = "";
    private String selectedLevel      = "";

    // UI references for each card
    private GoalCardController cardFitness;
    private GoalCardController cardCommitment;
    private GoalCardController cardLevel;

    // Bottom bar
    private Button  btnContinue;
    private TextView tvHint;
    private View    dot1, dot2, dot3;

    // -----------------------------------------------------------------------
    // Lifecycle
    // -----------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        // Load option lists from the central DataRepository
        FITNESS_OPTIONS    = Arrays.asList(DataRepository.getFitnessGoalOptions());
        COMMITMENT_OPTIONS = Arrays.asList(DataRepository.getCommitmentOptions());
        LEVEL_OPTIONS      = Arrays.asList(DataRepository.getExperienceLevelOptions());

        bindViews();
        setupGoalCards();
        updateContinueState();
    }

    // -----------------------------------------------------------------------
    // View binding
    // -----------------------------------------------------------------------

    private void bindViews() {
        btnContinue = findViewById(R.id.btn_continue);
        tvHint      = findViewById(R.id.tv_hint);
        dot1        = findViewById(R.id.dot1);
        dot2        = findViewById(R.id.dot2);
        dot3        = findViewById(R.id.dot3);

        btnContinue.setOnClickListener(v -> saveAndContinue());
    }

    // -----------------------------------------------------------------------
    // Goal card setup
    // -----------------------------------------------------------------------

    private void setupGoalCards() {
        cardFitness = new GoalCardController(
                (View) findViewById(R.id.card_fitness_goal),
                "GOAL 1", "Fitness Goal",
                FITNESS_OPTIONS,
                value -> { selectedFitness = value; updateContinueState(); }
        );

        cardCommitment = new GoalCardController(
                (View) findViewById(R.id.card_commitment),
                "GOAL 2", "Weekly Commitment",
                COMMITMENT_OPTIONS,
                value -> { selectedCommitment = value; updateContinueState(); }
        );

        cardLevel = new GoalCardController(
                (View) findViewById(R.id.card_level),
                "GOAL 3", "Experience Level",
                LEVEL_OPTIONS,
                value -> { selectedLevel = value; updateContinueState(); }
        );
    }

    // -----------------------------------------------------------------------
    // State management — mirrors allSelected / progress dots in Goals.jsx
    // -----------------------------------------------------------------------

    private void updateContinueState() {
        boolean allSelected = !selectedFitness.isEmpty()
                && !selectedCommitment.isEmpty()
                && !selectedLevel.isEmpty();

        btnContinue.setEnabled(allSelected);
        btnContinue.setAlpha(allSelected ? 1.0f : 0.4f);
        tvHint.setVisibility(allSelected ? View.GONE : View.VISIBLE);

        // Animate progress dots (completed = blue, pending = dim)
        setDotActive(dot1, !selectedFitness.isEmpty());
        setDotActive(dot2, !selectedCommitment.isEmpty());
        setDotActive(dot3, !selectedLevel.isEmpty());
    }

    private void setDotActive(View dot, boolean active) {
        if (dot == null) return;
        dot.setBackgroundResource(active ? R.drawable.circle_blue : R.drawable.circle_dim);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dot.getLayoutParams();
        params.width = active ? dpToPx(24) : dpToPx(8);
        dot.setLayoutParams(params);
    }

    // -----------------------------------------------------------------------
    // Navigation
    // -----------------------------------------------------------------------

    private void saveAndContinue() {
        if (selectedFitness.isEmpty() || selectedCommitment.isEmpty() || selectedLevel.isEmpty()) return;

        UserGoals goals = new UserGoals(selectedFitness, selectedCommitment, selectedLevel);
        goals.save(this);

        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // -----------------------------------------------------------------------
    // Inner class: manages one goal card (dropdown logic)
    // -----------------------------------------------------------------------

    interface OnOptionSelected {
        void onSelected(String value);
    }

    /**
     * GoalCardController manages the expand/collapse dropdown for a single goal card.
     * Mirrors the GoalCard component in Goals.jsx.
     */
    static class GoalCardController {

        private final View             rootCard;
        private final TextView         tvGoalNumber;
        private final TextView         tvGoalTitle;
        private final View             vCheck;
        private final LinearLayout     llDropdownTrigger;
        private final TextView         tvSelectedOption;
        private final TextView         tvChevron;
        private final LinearLayout     llOptionsContainer;
        private final List<String>     options;
        private final OnOptionSelected listener;

        private boolean isExpanded = false;
        private String  selected   = "";

        GoalCardController(View rootCard, String goalNumber, String goalTitle,
                           List<String> options, OnOptionSelected listener) {
            this.rootCard = rootCard;
            this.options  = options;
            this.listener = listener;

            // Bind child views
            tvGoalNumber       = rootCard.findViewById(R.id.tv_goal_number);
            tvGoalTitle        = rootCard.findViewById(R.id.tv_goal_title);
            vCheck             = rootCard.findViewById(R.id.v_check);
            llDropdownTrigger  = rootCard.findViewById(R.id.ll_dropdown_trigger);
            tvSelectedOption   = rootCard.findViewById(R.id.tv_selected_option);
            tvChevron          = rootCard.findViewById(R.id.tv_chevron);
            llOptionsContainer = rootCard.findViewById(R.id.ll_options_container);

            // Set labels
            if (tvGoalNumber != null) tvGoalNumber.setText(goalNumber);
            if (tvGoalTitle  != null) tvGoalTitle.setText(goalTitle);

            // Populate option rows
            buildOptionRows();

            // Toggle dropdown on trigger click
            if (llDropdownTrigger != null) {
                llDropdownTrigger.setOnClickListener(v -> toggleDropdown());
            }
        }

        private void buildOptionRows() {
            if (llOptionsContainer == null) return;
            llOptionsContainer.removeAllViews();

            for (String option : options) {
                TextView row = new TextView(rootCard.getContext());
                row.setText(option);
                row.setTextColor(Color.parseColor("#99FFFFFF"));
                row.setTextSize(13f);
                row.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12));
                row.setClickable(true);
                row.setFocusable(true);
                row.setOnClickListener(v -> selectOption(option, row));
                llOptionsContainer.addView(row);
            }
        }

        private void selectOption(String option, TextView clickedRow) {
            this.selected = option;

            // Update trigger text
            if (tvSelectedOption != null) {
                tvSelectedOption.setText(option);
                tvSelectedOption.setTextColor(Color.WHITE);
            }

            // Highlight selected row, dim others
            if (llOptionsContainer != null) {
                for (int i = 0; i < llOptionsContainer.getChildCount(); i++) {
                    View child = llOptionsContainer.getChildAt(i);
                    if (child instanceof TextView) {
                        TextView tv = (TextView) child;
                        if (tv == clickedRow) {
                            tv.setTextColor(Color.parseColor("#FF2979FF")); // fit_blue
                        } else {
                            tv.setTextColor(Color.parseColor("#99FFFFFF"));
                        }
                    }
                }
            }

            // Show checkmark
            if (vCheck != null) vCheck.setVisibility(View.VISIBLE);

            collapseDropdown();
            listener.onSelected(option);
        }

        private void toggleDropdown() {
            if (isExpanded) collapseDropdown();
            else expandDropdown();
        }

        private void expandDropdown() {
            if (llOptionsContainer == null) return;
            llOptionsContainer.setVisibility(View.VISIBLE);
            if (tvChevron != null) tvChevron.setText("▴");
            isExpanded = true;
        }

        private void collapseDropdown() {
            if (llOptionsContainer == null) return;
            llOptionsContainer.setVisibility(View.GONE);
            if (tvChevron != null) tvChevron.setText("▾");
            isExpanded = false;
        }

        private int dpToPx(int dp) {
            float density = rootCard.getContext().getResources().getDisplayMetrics().density;
            return Math.round(dp * density);
        }
    }
}
