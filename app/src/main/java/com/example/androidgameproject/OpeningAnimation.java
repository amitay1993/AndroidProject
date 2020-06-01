package com.example.androidgameproject;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OpeningAnimation extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening_animation);

        ImageView opening_anim = findViewById(R.id.opening_anim_iv);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.scale_start_anim);
        opening_anim.startAnimation(animation1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(OpeningAnimation.this, MainActivity.class);
                startActivity(intent);
            }
        }, 5000);


    }
}