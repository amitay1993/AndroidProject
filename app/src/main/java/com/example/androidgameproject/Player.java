package com.example.androidgameproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends Position implements ObjectsInterface {

    private Bitmap playerBitmap;
    private Bitmap[] bitmaps;
    private int score;
    private double deltaYplayer;
    private boolean isUp,isPlaying;
    private long startTime;
    private Animation animation;

    public Player(Bitmap bitmap) { //change
        super(100,GameSurfaceView.heightScreen /2-bitmap.getHeight()/2,bitmap.getWidth(),bitmap.getHeight());
        this.playerBitmap = bitmap;
        deltaYplayer =0;
        score=0;

    }


    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(playerBitmap,x,y,null);
    }

    @Override
    public void update() {
        long timeElapsed=System.nanoTime()-startTime/1000000;
        if(timeElapsed>100){ //change
            score++;
            startTime=System.nanoTime();
        }

        if(isUp){
            deltaYplayer-=0.8;
            deltaY=(int)deltaYplayer;  //change
        }
        else{   //change
            deltaYplayer+=0.2;
            deltaY=(int)deltaYplayer;
        }
        if(deltaY>14)
            deltaY=14;   //change
        if(deltaY<-14)
            deltaY=-14;
          y+=deltaY*2; // change ****
          deltaY=0;
          if(bottomBorder()>GameSurfaceView.heightScreen) {
              y = GameSurfaceView.heightScreen - height;
              deltaYplayer=0;
          }
          else if(topBorder()<0) {
              y = 0;
              deltaYplayer=0;
          }



    }

    public void setUp(boolean up) {
       this.isUp = up;
    }

    public int getScore() {
        return score;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
    public void resetDeltaY(){
        deltaY=0;
    }
    public void resetScore(){
        score=0;
    }
}
