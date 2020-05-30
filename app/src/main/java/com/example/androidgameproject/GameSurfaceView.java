package com.example.androidgameproject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread mainThread;
    public static final  int SPEED=-5; //change
    private Background background;
    private Player player;
    private List<Bullet> bullets;
    private long bulletStartTime;
    static int widthScreen, heightScreen;


    public GameSurfaceView(Context context,int width,int height) {
        super(context);
        widthScreen =width;
        heightScreen =height;
        mainThread=new MainThread(getHolder(),this);
        getHolder().addCallback(this);
        setFocusable(true);
        bullets=new ArrayList<>();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainThread.setRunning(true);
        mainThread.start();
        background=new Background(BitmapFactory.decodeResource(getResources(),R.drawable.background1));
        player=new Player(BitmapFactory.decodeResource(getResources(),R.drawable.player));
        bulletStartTime=System.nanoTime();




    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry=true;
        while (retry){
            try{
                mainThread.setRunning(false);
                mainThread.join();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action =event.getAction();
        if(action==MotionEvent.ACTION_DOWN){
            if(!player.isPlaying()) {
                player.setPlaying(true);
            }
            else
                player.setUp(true);
        }
       else if(action==MotionEvent.ACTION_UP) {
            player.setUp(false);
        }
        return true;
    }

    public void update(){

        if(player.isPlaying()) {
            background.update();
            player.update();

            long bulletTimer=(System.nanoTime()-bulletStartTime)/1000000;
            if(bulletTimer>2500-player.getScore()/4){ //change
                bullets.add(new Bullet(BitmapFactory.decodeResource(getResources(), R.drawable.bullet), player.getX() + player.getWidth(), player.getY() + player.getHeight()/2-9));
                bulletStartTime=System.nanoTime();
            }
            for(Bullet bullet:bullets){
                bullet.update();
                if(bullet.getX()<-10) //change
                {
                    bullets.remove(bullet);
                }
            }

        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(canvas!=null){
            final int saveState=canvas.save();

            background.draw(canvas);
            player.draw(canvas);
            canvas.restoreToCount(saveState);
            for(Bullet bullet:bullets)
                bullet.draw(canvas);
        }
    }
}
