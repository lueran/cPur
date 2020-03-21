package com.cpur;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends BaseActivity {
    private Intent startIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirebaseAuth auth = FirebaseAuth.getInstance();

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

        }, 1000);

    }
}
