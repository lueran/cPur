package com.cpur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        Intent startIntent;
        if (auth.getCurrentUser() != null) {
            startIntent = new Intent(this, CreateStoryActivity.class);
        } else {
            startIntent = new Intent(this, LoginActivity.class);
        }

        startActivity(startIntent);
    }
}