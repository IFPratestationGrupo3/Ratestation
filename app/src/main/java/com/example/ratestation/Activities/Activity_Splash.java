package com.example.ratestation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratestation.R;

public class Activity_Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ImageView logoImageView = findViewById(R.id.ratelogo);


        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.logo_fadein);
        logoImageView.startAnimation(fadeIn);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(Activity_Splash.this, Activity_Login.class);
            startActivity(intent);
            finish();
        }, 2000); // 2000 ms = 2 segundos
    }
}
