package com.cpur;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Intent startIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startIntent = new Intent(this, MainActivity.class);
        } else {
            startIntent = new Intent(this, LoginActivity.class);
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(startIntent);
                finish();
            }

        }, 1500);

    }
}
