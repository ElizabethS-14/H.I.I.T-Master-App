package edu.bpi.hiitmasterapp;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDetails {

    private static final String PREFS_NAME = "fittrack_user_details";
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

    public UserDetails(float weight, float height, int age,
                       float chest, float waist, float hips,
                       float biceps, float thighs) {
        this.weight  = weight;
        this.height  = height;
        this.age     = age;
        this.chest   = chest;
        this.waist   = waist;
        this.hips    = hips;
        this.biceps  = biceps;
        this.thighs  = thighs;
    }

    // -----------------------------------------------------------------------
    // SharedPreferences persistence
    // -----------------------------------------------------------------------

    public void save(Context context) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit();
        editor.putFloat(KEY_WEIGHT, weight);
        editor.putFloat(KEY_HEIGHT, height);
        editor.putInt  (KEY_AGE,    age);
        editor.putFloat(KEY_CHEST,  chest);
        editor.putFloat(KEY_WAIST,  waist);
        editor.putFloat(KEY_HIPS,   hips);
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
                prefs.getInt  (KEY_AGE,    0),
                prefs.getFloat(KEY_CHEST,  0f),
                prefs.getFloat(KEY_WAIST,  0f),
                prefs.getFloat(KEY_HIPS,   0f),
                prefs.getFloat(KEY_BICEPS, 0f),
                prefs.getFloat(KEY_THIGHS, 0f)
        );
    }

    public boolean isComplete() {
        return weight > 0 && height > 0 && age > 0;
    }

    // -----------------------------------------------------------------------
    // Getters & Setters
    // -----------------------------------------------------------------------

    public float getWeight()  { return weight; }
    public float getHeight()  { return height; }
    public int   getAge()     { return age; }
    public float getChest()   { return chest; }
    public float getWaist()   { return waist; }
    public float getHips()    { return hips; }
    public float getBiceps()  { return biceps; }
    public float getThighs()  { return thighs; }

    public void setWeight(float weight)   { this.weight  = weight; }
    public void setHeight(float height)   { this.height  = height; }
    public void setAge(int age)           { this.age     = age; }
    public void setChest(float chest)     { this.chest   = chest; }
    public void setWaist(float waist)     { this.waist   = waist; }
    public void setHips(float hips)       { this.hips    = hips; }
    public void setBiceps(float biceps)   { this.biceps  = biceps; }
    public void setThighs(float thighs)   { this.thighs  = thighs; }

    /** Returns BMI rounded to 1 decimal place. Returns 0 if data is missing. */
    public float getBmi() {
        if (height <= 0 || weight <= 0) return 0f;
        float heightM = height / 100f;
        return Math.round((weight / (heightM * heightM)) * 10f) / 10f;
    }
}
