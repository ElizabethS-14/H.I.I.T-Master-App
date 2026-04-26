package edu.bpi.hiitmasterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.bpi.hiitmasterapp.R;
import edu.bpi.hiitmasterapp.UserGoals;

/**
 * Entry screen — mirrors Welcome.jsx.
 *
 * Behaviour:
 *  • If goals are already saved → go straight to DashboardActivity.
 *  • "Get Started" / "Sign In" navigate to GoalsActivity (onboarding).
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Skip onboarding if goals already exist
        UserGoals saved = UserGoals.load(this);
        if (saved.isComplete()) {
            startDashboard();
            return;
        }

        setContentView(R.layout.activity_welcome);

        // Populate the three feature-list rows
        String[] features = {
            getString(R.string.feature_plans),
            getString(R.string.feature_tracking),
            getString(R.string.feature_analytics)
        };
        int[] featureIds = { R.id.feature_1, R.id.feature_2, R.id.feature_3 };

        for (int i = 0; i < featureIds.length; i++) {
            View row = findViewById(featureIds[i]);
            if (row != null) {
                TextView tv = row.findViewById(R.id.tv_feature_text);
                if (tv != null) tv.setText(features[i]);
            }
        }

        Button btnGetStarted = findViewById(R.id.btn_get_started);
        Button btnSignIn     = findViewById(R.id.btn_sign_in);

        btnGetStarted.setOnClickListener(v -> navigateToGoals());
        btnSignIn.setOnClickListener(v -> navigateToGoals());
    }

    private void navigateToGoals() {
        startActivity(new Intent(this, GoalsActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void startDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}
