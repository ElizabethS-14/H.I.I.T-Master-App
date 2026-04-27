package edu.bpi.hiitmasterapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.bpi.hiitmasterapp.R;

public class UserDetails extends AppCompatActivity {

    private static final String PREFS_NAME = "hittmaster_user_details";
    private static final String KEY_WEIGHT  = "weight_kg";
    private static final String KEY_HEIGHT  = "height_cm";
    private static final String KEY_AGE     = "age";
    private static final String KEY_CHEST   = "chest_cm";
    private static final String KEY_WAIST   = "waist_cm";
    private static final String KEY_HIPS    = "hips_cm";
    private static final String KEY_BICEPS  = "biceps_cm";
    private static final String KEY_THIGHS  = "thighs_cm";

    private float weight;   // kg
    private float height;   // cm
    private int   age;
    private float chest;    // cm
    private float waist;    // cm
    private float hips;     // cm
    private float biceps;   // cm
    private float thighs;   // cm

    public UserDetails() {
        // Required empty public constructor for Activity
    }

    public UserDetails(float weight, float height, int age, float chest, float waist, float hips, float biceps, float thighs) {
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.chest = chest;
        this.waist = waist;
        this.hips = hips;
        this.biceps = biceps;
        this.thighs = thighs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        UserDetails userDetails = UserDetails.load(this);
        weight = userDetails.getWeight();
        height = userDetails.getHeight();
        age = userDetails.getAge();

        Button btnContinue = findViewById(R.id.btn_save_details);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDetails.save(UserDetails.this);
                Intent intent = new Intent(UserDetails.this, DashboardActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();


            }
        });

    }
    public void save(Context context) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit();
        editor.putFloat(KEY_WEIGHT, weight);
        editor.putFloat(KEY_HEIGHT, height);
        editor.putInt(KEY_AGE, age);
        editor.putFloat(KEY_CHEST, chest);
        editor.putFloat(KEY_WAIST, waist);
        editor.putFloat(KEY_HIPS, hips);
        editor.putFloat(KEY_BICEPS, biceps);
        editor.putFloat(KEY_THIGHS, thighs);
        editor.apply();
    }
    public static UserDetails load(Context context) {
        SharedPreferences prefs = context
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return new UserDetails(
                prefs.getFloat(KEY_WEIGHT, 0f),
                prefs.getFloat(KEY_HEIGHT, 0f),
                prefs.getInt(KEY_AGE, 0),
                prefs.getFloat(KEY_CHEST, 0f),
                prefs.getFloat(KEY_WAIST, 0f),
                prefs.getFloat(KEY_HIPS, 0f),
                prefs.getFloat(KEY_BICEPS, 0f),
                prefs.getFloat(KEY_THIGHS, 0f)
        );
    }
    public boolean isComplete() {
        return weight > 0 && height > 0 && age > 0;
    }
    public float getWeight() {
        return weight; }
    public void setWeight(float weight) {
        this.weight = weight; }
    public float getHeight() {
        return height; }
    public void setHeight(float height) {
        this.height = height; }
    public int getAge() {
        return age; }
    public void setAge(int age) {
        this.age = age; }
    public float getChest() {
        return chest; }
    public void setChest(float chest) {
        this.chest = chest; }
    public float getWaist() {
        return waist; }
    public void setWaist(float waist) {
        this.waist = waist; }
    public float getHips() {
        return hips; }
    public void setHips(float hips) {
        this.hips = hips; }
    public float getBiceps() {
        return biceps; }
    public void setBiceps(float biceps) {
        this.biceps = biceps; }
    public float getThighs() {
        return thighs; }
    public void setThighs(float thighs) {
        this.thighs = thighs; }
    public float getBmi() {
        if (height <= 0 || weight <= 0) return 0f;
        float heightM = height / 100f;
        return Math.round((weight / (heightM * heightM)) * 10f) / 10f;
    }
}
