package com.example.androidgameproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class Tutorial extends AppCompatActivity {
    Point point;
    GameSurfaceTutorial gameSurfaceTutorial;

    @Override
    protected void onResume() {
        super.onResume();
        fullScreencall();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreencall();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        gameSurfaceTutorial=new GameSurfaceTutorial(this,point.x,point.y);



        FrameLayout game =new FrameLayout(this);
        LinearLayout gameWidgets = new LinearLayout (this);
        gameWidgets.setGravity(Gravity.TOP | Gravity.CENTER);

        Drawable buttonDrawable =getResources().getDrawable(R.drawable.start_game_tv);
        buttonDrawable.mutate();
        Drawable buttonDrawa =getResources().getDrawable(R.drawable.start_game_tv);
        buttonDrawa.mutate();
        Button backBtn = new Button(this);
        Button startGamebtn=new Button(this);

        backBtn.setBackground(buttonDrawable);
        startGamebtn.setBackground(buttonDrawa);

        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(30,50,30,0);


        backBtn.setPadding(25,10,25,10);
        backBtn.setLayoutParams(params);
        backBtn.setText(R.string.back_to_menu);
        backBtn.setTextColor(Color.WHITE);
        backBtn.setTextSize(20);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Typeface typeface = getResources().getFont(R.font.hippopotamus);
        }
        //or to support all versions use
        Typeface typeface = ResourcesCompat.getFont(this, R.font.hippopotamus);
        backBtn.setTypeface(typeface);
        startGamebtn.setTypeface(typeface);

        startGamebtn.setLayoutParams(params);
        startGamebtn.setPadding(25,10,25,10);

        startGamebtn.setTextSize(20);
        startGamebtn.setTextColor(Color.WHITE);
        startGamebtn.setText("Start Playing");



        gameWidgets.requestLayout();
        gameWidgets.addView(startGamebtn);
        gameWidgets.addView(backBtn);

        game.addView(gameSurfaceTutorial);
        game.addView(gameWidgets);
        setContentView(game);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Tutorial.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        startGamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Tutorial.this,GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

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
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Tutorial.this,MainActivity.class);
        finish();
        startActivity(intent);

    }
}
