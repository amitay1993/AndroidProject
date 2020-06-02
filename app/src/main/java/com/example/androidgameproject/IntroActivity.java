package com.example.androidgameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_intro);

           Intent intent=new Intent(IntroActivity.this,GameSurfaceView.class);
           startActivity(intent);
   }
}
