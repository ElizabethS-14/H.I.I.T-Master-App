package edu.bpi.hiitmasterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class Home extends AppCompatActivity {

    ImageView imageView3;
    Button getStarted;
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.layout_home);


        getStarted = (Button) findViewById(R.id.btn_get_started);
        signIn = (Button) findViewById(R.id.btn_sign_in);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, GetStarted.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }
}