package edu.bpi.hiitmasterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class GoalPage extends AppCompatActivity {

    Spinner Goal1_Op;

    Spinner Goal2_Op;

    Spinner Goal3_Op;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_goal_page);

        imageView = (ImageView) findViewById(R.id.imageView);
        Goal1_Op = (Spinner)findViewById(R.id.Goal1_Op);
        Goal2_Op = (Spinner)findViewById(R.id.Goal2_Op);
        Goal3_Op = (Spinner)findViewById(R.id.Goal3_Op);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalPage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Drop down menu 1
        ArrayAdapter<CharSequence>adapter1=ArrayAdapter.createFromResource(this, R.array.Goal1, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Goal1_Op.setAdapter(adapter1);

        //Drop down menu 2
        ArrayAdapter<CharSequence>adapter2=ArrayAdapter.createFromResource(this, R.array.Goal2, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Goal2_Op.setAdapter(adapter2);

        //Drop down menu 3
        ArrayAdapter<CharSequence>adapter3=ArrayAdapter.createFromResource(this, R.array.Goal3, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Goal3_Op.setAdapter(adapter3);


    }
}