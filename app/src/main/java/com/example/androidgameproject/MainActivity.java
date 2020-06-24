package com.example.androidgameproject;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;
    boolean sound_bool;
    SharedPreferences soundSP;
    public static final String USER_SP_FILE ="user_file",SOUND_KEY="sound";
    Button sound_btn;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundSP=getSharedPreferences(USER_SP_FILE,MODE_PRIVATE);
        sound_bool=soundSP.getBoolean(SOUND_KEY,true);

        fullScreencall();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button scores_table = findViewById(R.id.scores_table_btn);
        scores_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ScoreListView.class);
                startActivity(intent);
            }
        });
        sound_btn = findViewById(R.id.sound_btn);

        mp = MediaPlayer.create(this,R.raw.opening_sound_liran);
        mp.setLooping(true);
        if(sound_bool)
            mp.start();
        else
            sound_btn.setBackground(getDrawable(R.drawable.ic_mute_icon));


        ImageView start_anim = findViewById(R.id.start_anim);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.scale_start_anim);
        start_anim.startAnimation(animation1);


        sound_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sound_bool) {
                            sound_btn.setBackground(getDrawable(R.drawable.ic_mute_icon));
                            soundSP.edit().putBoolean(SOUND_KEY,false).commit();
                            sound_bool = false;
                            mp.pause();
                        } else {
                            sound_btn.setBackground(getDrawable(R.drawable.ic_speaker_icon));
                            soundSP.edit().putBoolean(SOUND_KEY,true).commit();
                            sound_bool = true;
                            mp.start();
                        }
            }
        });

        Button tutorial_btn= findViewById(R.id.tutorial_btn);
        tutorial_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,Tutorial.class);
                startActivity(intent);
                finish();
            }
        });
        Button exitButton=findViewById(R.id.exit_btn);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sound_bool)
            mp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sound_bool)
            mp.start();
        fullScreencall();
    }

    public void listnerGame(View view) {
        Intent intent =new Intent(this,GameActivity.class);
        startActivity(intent);
        finish();
    }
    public void fullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
