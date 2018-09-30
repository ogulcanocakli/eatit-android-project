package com.example.ogulcan.eatit.Giris;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.ogulcan.eatit.R;

public class SplashScreen extends AppCompatActivity {

    private static int gosterim_suresi = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        }, gosterim_suresi);
    }
}
