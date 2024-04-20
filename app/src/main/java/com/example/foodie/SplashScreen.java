package com.example.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView splashImage = findViewById(R.id.splash_icon);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000); // milliseconds
        splashImage.startAnimation(fadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreen.this, Signin.class);
                startActivity(intent);
                finish();

            }
        }, 2000); // Wait for 2 seconds before starting the animation
    }
}
