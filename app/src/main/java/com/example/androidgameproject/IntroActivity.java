//package com.example.androidgameproject;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//public class IntroActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_intro);
//
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    sleep(4500);
//                    Intent intent=new Intent(IntroActivity.this,GameSurfaceView.class);
//                    startActivity(intent);
//                    finish();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//        };
//        thread.start();
//    }
//}
