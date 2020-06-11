package com.example.androidgameproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.SharedMemory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import java.util.Map;
import java.util.Set;

public class GameActivity extends AppCompatActivity  implements GameListener, View.OnClickListener {
    Point point;
    GameSurfaceView gameSurfaceView;
    List<User> users=new ArrayList<>();
    int checkpoint=0;
    SharedPreferences checkPointsharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        checkPointsharedPreferences=getSharedPreferences("usercheckpoint",MODE_PRIVATE);
        checkpoint=checkPointsharedPreferences.getInt("checkpoint",0);
        gameSurfaceView=new GameSurfaceView(this,point.x,point.y,checkpoint);
        checkPointsharedPreferences.edit().putInt("checkpoint",0).commit();




        FrameLayout game =new FrameLayout(this);
        LinearLayout gameWidgets = new LinearLayout (this);
        gameWidgets.setGravity(Gravity.BOTTOM | Gravity.END);

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
        gameSurfaceView.mediaPlayerGame.pause();
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

                resumeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
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

                coinsTv.setText("coins " + coins);
                scoreTv.setText("score " + dist);
                distTv.setText("distance " + score);


                if(gameSurfaceView.getCheckPoint()>0){
                    checkpointcb.setVisibility(View.VISIBLE);
                    checkpointcb.setText(checkpointcb.getText()+" " +gameSurfaceView.getCheckPoint()+"?");
                }

                checkpointcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            checkPointsharedPreferences.edit().putInt("checkpoint",gameSurfaceView.getCheckPoint()).commit();
                        }else{
                            checkPointsharedPreferences.edit().putInt("checkpoint",0).commit();
                        }
                    }
                });


                savebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name=nameEt.getText().toString();
                        if(name.length()==0){
                            Toast.makeText(GameActivity.this, "You Must enter a Name", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(GameActivity.this, "Saved", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSurfaceView.resume();
    }

}
