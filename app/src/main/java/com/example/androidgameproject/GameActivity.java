package com.example.androidgameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity  implements GameListener, View.OnClickListener {
    Point point;
    GameSurfaceView gameSurfaceView;
    List<User> users=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        gameSurfaceView=new GameSurfaceView(this,point.x,point.y);







        FrameLayout game =new FrameLayout(this);
        LinearLayout gameWidgets = new LinearLayout (this);
        gameWidgets.setGravity(Gravity.BOTTOM | Gravity.END);

        ImageButton pausebtn = new ImageButton(this);
        pausebtn.setImageDrawable(getResources().getDrawable(R.drawable.pausebtn));
        pausebtn.setBackground(null);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(100,0,100,50);
       // pausebtn.setScaleX(1.5f);
      //  pausebtn.setScaleY(1.5f);
        pausebtn.setLayoutParams(params);
        gameWidgets.requestLayout();
       // pausebtn.setLayoutParams(params);
        //pausebtn.setWidth(300);
        //pausebtn.setText("PAUSE");
        //pausebtn.setGravity(3);

        gameWidgets.addView(pausebtn);
        game.addView(gameSurfaceView);
        game.addView(gameWidgets);

        setContentView(game);
        pausebtn.setOnClickListener(this);



    }
    public void onClick(View v) {
        gameSurfaceView.mediaPlayerGame.pause();
        onPause();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder=new AlertDialog.Builder(GameActivity.this);

                View view= LayoutInflater.from(GameActivity.this).inflate(R.layout.dialog_layout,null);

                builder.setView(view);
                builder.setCancelable(false);

                builder.setPositiveButton("Resume",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameSurfaceView.resumeOnPause();
                    }
                });
                builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        gameSurfaceView.resumeOnPause();

                    }
                });
                final AlertDialog alertDialog=builder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                });
                alertDialog.show();
            }

        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        FullScreencall();
    }
    public void FullScreencall() {
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
    public void onGameOver() {

        gameSurfaceView.mediaPlayerGame.stop();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder=new AlertDialog.Builder(GameActivity.this);

                View view= LayoutInflater.from(GameActivity.this).inflate(R.layout.dialog_diff,null);

                builder.setView(view);
                builder.setCancelable(false);
                final AlertDialog alertDialog=builder.create();

                final Button playAaginbtn=view.findViewById(R.id.playagain);
                final Button savebtn=view.findViewById(R.id.save);
                final Button backtomenu=view.findViewById(R.id.backtomenu);
                final EditText nameEt=view.findViewById(R.id.entername);

                TextView scoreTv=view.findViewById(R.id.score);
                TextView distTv=view.findViewById(R.id.distance);
                final TextView coinsTv=view.findViewById(R.id.coins);


                int coins=gameSurfaceView.coin_counter;
                final long dist=gameSurfaceView.player.getScore();
                final int score=gameSurfaceView.bScore;

                coinsTv.setText("coins " + coins);
                scoreTv.setText("score " + dist);
                distTv.setText("distance " + score);




                savebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name=nameEt.getText().toString();
                        if(name.length()==0){
                            Toast.makeText(GameActivity.this, "You Must enter a Name", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            try {
                                File yourFile = new File("score.txt");
                                yourFile.createNewFile();
                                FileInputStream fileInputStream=openFileInput(yourFile.getName());

                                ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
                                users=(ArrayList<User>) objectInputStream.readObject();
                                objectInputStream.close();


                                FileOutputStream fileOutputStream=openFileOutput("LeaderBoard",MODE_PRIVATE);
                                ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
                                users.add(new User(name,score,dist));
                                objectOutputStream.writeObject(users);
                                objectOutputStream.close();

                                nameEt.setText("");
                                Toast.makeText(GameActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                savebtn.setClickable(false);


                            } catch (ClassNotFoundException | IOException e) {
                                        e.printStackTrace();
                                    }
                        }

                    }
                });


                backtomenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(GameActivity.this,MainActivity.class);
                        alertDialog.dismiss();
                        finish();
                        startActivity(intent);

                    }
                });

                playAaginbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        recreate();
                    }
                });
                alertDialog.show();
            }

        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        gameSurfaceView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSurfaceView.resume();
    }

}
