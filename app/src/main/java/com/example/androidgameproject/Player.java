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

    public Player(Bitmap bitmap, int width,int height,int numberOfFrames ) { //change

        this.playerBitmap = bitmap;
        animation=new Animation();
        x=100;
        y=GameSurfaceView.HEIGHT/2;
        deltaYplayer =0;
        score=0;
        super.height=height;
        super.width=width;
        bitmaps=new Bitmap[numberOfFrames];

        for(int i=0;i<bitmaps.length;i++){
            bitmaps[i]=Bitmap.createBitmap(playerBitmap,i*width,0,width,height); // change
        }
        animation.setFrames(bitmaps);
        animation.setDelay(10);
        startTime=System.nanoTime();



    }


    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(animation.getBitmap(),x,y,null);
    }

    @Override
    public void update() {
        long timeElapsed=System.nanoTime()-startTime/1000000;
        if(timeElapsed>100){ //change
            score++;
            startTime=System.nanoTime();
        }
        animation.update();

        if(isUp){
            deltaYplayer-=1.1;
            deltaY=(int)deltaYplayer;  //change
        }
        else{   //change
            deltaYplayer+=1.1;
            deltaY=(int)deltaYplayer;
        }
        if(deltaY>14)
            deltaY=14;   //change
        if(deltaY<-14)
            deltaY=-14;
        y+=deltaY*2;
        deltaY=0;

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
