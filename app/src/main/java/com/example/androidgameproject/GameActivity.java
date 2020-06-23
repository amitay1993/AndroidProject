package com.example.androidgameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    int checkpoint=0;
    SharedPreferences sharedPreferences;
    boolean sound_bool;
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        sharedPreferences =getSharedPreferences(MainActivity.USER_SP_FILE,MODE_PRIVATE);
        checkpoint= sharedPreferences.getInt("checkpoint",0);
        gameSurfaceView=new GameSurfaceView(this,point.x,point.y,checkpoint);
        sharedPreferences.edit().putInt("checkpoint",0).commit();

        sound_bool=sharedPreferences.getBoolean(MainActivity.SOUND_KEY,true);

        mp = MediaPlayer.create(this,R.raw.playgame_sound);
        mp.setLooping(true);
        if(sound_bool)
            mp.start();

        

        FrameLayout game =new FrameLayout(this);
        LinearLayout gameWidgets = new LinearLayout (this);
        gameWidgets.setGravity(Gravity.BOTTOM | Gravity.RIGHT);

        ImageButton pausebtn = new ImageButton(this);

        pausebtn.setImageDrawable(getResources().getDrawable(R.drawable.pausebtn));
        pausebtn.setBackground(null);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(100,0,100,50);

        pausebtn.setLayoutParams(params);
        gameWidgets.requestLayout();

        gameWidgets.addView(pausebtn);
        game.addView(gameSurfaceView);
        game.addView(gameWidgets);

        setContentView(game);
        pausebtn.setOnClickListener(this);

    }
    public void onClick(View v) {
    //    gameSurfaceView.mediaPlayerGame.pause();
        if(sound_bool)
            mp.pause();
        gameSurfaceView.isPauseDialog=true;
        onPause();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder=new AlertDialog.Builder(GameActivity.this);

                View view= LayoutInflater.from(GameActivity.this).inflate(R.layout.dialog_layout,null);

                builder.setView(view);
                builder.setCancelable(false);
                final AlertDialog alertDialog=builder.create();

                final ImageButton resumeBtn=view.findViewById(R.id.playagain);
                final ImageButton menuBtn=view.findViewById(R.id.backtomenu);
                final ImageButton soundBtn=view.findViewById(R.id.sound_game_btn);

                if(!sound_bool)
                    soundBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_mute_icon_black));
                soundBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sound_bool) {
                            soundBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_mute_icon_black));
                            sharedPreferences.edit().putBoolean(MainActivity.SOUND_KEY, false).commit();
                            sound_bool = false;
                            mp.pause();
                        } else {
                            soundBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_speaker_icon_black));
                            sharedPreferences.edit().putBoolean(MainActivity.SOUND_KEY, true).commit();
                            sound_bool = true;
                        }
                    }
                });


                resumeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fullScreencall();
                        gameSurfaceView.isPauseDialog=false;
                        alertDialog.dismiss();
                        if(sound_bool)
                            mp.start();
                        gameSurfaceView.resumeOnPause();
                    }
                });

                menuBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(GameActivity.this,MainActivity.class);
                        alertDialog.dismiss();
                        finish();
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }

        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        fullScreencall();
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
    public void onGameOver() {
        if(sound_bool)
            mp.pause();
       // gameSurfaceView.mediaPlayerGame.stop();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder=new AlertDialog.Builder(GameActivity.this);

                View view= LayoutInflater.from(GameActivity.this).inflate(R.layout.dialog_diff,null);

                builder.setView(view);
                builder.setCancelable(false);
                final AlertDialog alertDialog=builder.create();
                final CheckBox checkpointcb=view.findViewById(R.id.checkpointcb);
                final ImageButton playAaginbtn=view.findViewById(R.id.playagain);
                final ImageButton savebtn=view.findViewById(R.id.save);
                final ImageButton backtomenu=view.findViewById(R.id.backtomenu);
                final EditText nameEt=view.findViewById(R.id.entername);



                TextView scoreTv=view.findViewById(R.id.score);
                TextView distTv=view.findViewById(R.id.distance);
                final TextView coinsTv=view.findViewById(R.id.coins);


                int coins=gameSurfaceView.coin_counter;
                final long dist=gameSurfaceView.player.getDistance();
                final int score=gameSurfaceView.bScore;

                coinsTv.setText(getString(R.string.Coins)+ coins);
                scoreTv.setText(getString(R.string.Score) + score);
                distTv.setText(getString(R.string.Distance) + dist);


                if(gameSurfaceView.getCheckPoint()>0){
                    checkpointcb.setVisibility(View.VISIBLE);
                    checkpointcb.setText(checkpointcb.getText()+" " +gameSurfaceView.getCheckPoint()+"?");
                }

                checkpointcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            sharedPreferences.edit().putInt("checkpoint",gameSurfaceView.getCheckPoint()).commit();
                        }else{
                            sharedPreferences.edit().putInt("checkpoint",0).commit();
                        }
                    }
                });


                savebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name=nameEt.getText().toString();
                        if(name.length()==0){
                            Toast.makeText(GameActivity.this,getString(R.string.enter_your_name), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            try {
                                FileInputStream fileInputStream=openFileInput("LeaderBoard");
                                ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
                                users=(ArrayList<User>) objectInputStream.readObject();
                                for( User user:users){
                                    System.out.println(user.toString());
                                }
                                objectInputStream.close();

                            } catch (ClassNotFoundException | IOException e) {
                                        e.printStackTrace();
                                    }
                            try{
                                FileOutputStream fileOutputStream=openFileOutput("LeaderBoard",MODE_PRIVATE);
                                ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
                                users.add(new User(name,score,dist));
                                objectOutputStream.writeObject(users);
                                objectOutputStream.close();

                                nameEt.setText("");
                                Toast.makeText(GameActivity.this, getString(R.string.Saved), Toast.LENGTH_SHORT).show();
                                savebtn.setClickable(false);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
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
        if(sound_bool)
            mp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fullScreencall();
        gameSurfaceView.resume();
        if(!gameSurfaceView.isPauseDialog&&sound_bool)
            mp.start();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(GameActivity.this,MainActivity.class);
        finish();
        startActivity(intent);

    }
}
