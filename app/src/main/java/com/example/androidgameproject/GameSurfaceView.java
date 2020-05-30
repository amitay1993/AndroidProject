package com.example.androidgameproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread mainThread;
    public static final int WIDTH=856; //change
    public static final int HEIGHT=480; //change
    public static final  int SPEED=-5; //change
    private Background background;
    private Player player;


    public GameSurfaceView(Context context) {
        super(context);
        mainThread=new MainThread(getHolder(),this);
        getHolder().addCallback(this);
        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainThread.setRunning(true);
        mainThread.start();
        background=new Background(BitmapFactory.decodeResource(getResources(),R.drawable.background1));
        player=new Player(BitmapFactory.decodeResource(getResources(),R.drawable.playertest),30,45,3);




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
            if(!player.isPlaying())
                player.setPlaying(true);
            else
                player.setUp(true);
        }
        if(action==MotionEvent.ACTION_UP){
            player.setUp(false);
        }



        return true;
    }

    public void update(){

        if(player.isPlaying()) {
            background.update();
            player.update();
        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        final float scaleFactorX=getWidth()/WIDTH; // change
        final float scaleFactorY=getHeight()/HEIGHT; // change

        if(canvas!=null){
            final int saveState=canvas.save();
            canvas.scale(scaleFactorX,scaleFactorY);
            background.draw(canvas);
            player.draw(canvas);
            canvas.restoreToCount(saveState);
        }
    }
}
