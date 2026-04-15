package edu.bpi.hiitmasterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class Dashboard extends AppCompatActivity {

    CardView workoutoftheDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.layout_dashboard);

        workoutoftheDay = (CardView) findViewById(R.id.to_workoutoftheday_page);


        workoutoftheDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Workouts.class);
                startActivity(intent);
            }
        });
    }
}