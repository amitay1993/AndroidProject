package com.example.androidgameproject;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class Tutorial extends AppCompatActivity {
    Point point;
    GameSurfaceTutorial gameSurfaceTutorial;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        Button backBtn = new Button(this);
        Button startGamebtn=new Button(this);

        backBtn.setBackground(buttonDrawable);
        startGamebtn.setBackground(buttonDrawable);

        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(30,50,30,0);


        backBtn.setPadding(5,5,5,5);
        backBtn.setLayoutParams(params);
        backBtn.setText(R.string.back_to_menu);
        backBtn.setTextColor(Color.WHITE);
        backBtn.setTextSize(15);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Typeface typeface = getResources().getFont(R.font.gendarwo);
        }
        //or to support all versions use
        Typeface typeface = ResourcesCompat.getFont(this, R.font.gendarwo);
        backBtn.setTypeface(typeface);
        startGamebtn.setTypeface(typeface);


        startGamebtn.setPadding(5,5,5,5);
        startGamebtn.setTextSize(15);
        startGamebtn.setLayoutParams(params);
        startGamebtn.setTextColor(Color.WHITE);
        startGamebtn.setText("Start Playing");



        gameWidgets.requestLayout();
        gameWidgets.addView(backBtn);
        gameWidgets.addView(startGamebtn);
        game.addView(gameSurfaceTutorial);
        game.addView(gameWidgets);
        setContentView(game);

    }
}
