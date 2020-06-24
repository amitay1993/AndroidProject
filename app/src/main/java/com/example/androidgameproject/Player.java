package com.example.androidgameproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends Position implements ObjectsInterface {

    private Bitmap playerBitmap;
    private int distance,deltaY;
    private double deltaYplayer;
    private boolean isUp,isPlaying;
    private long startTime;
    private int heightScreen;

    public Player(Bitmap bitmap,int heightScreen) { //change
        super(100,heightScreen /2-bitmap.getHeight()/2,bitmap.getWidth(),bitmap.getHeight());
        this.playerBitmap = bitmap;
        deltaYplayer =0;
        distance =0;
        this.heightScreen=heightScreen;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(playerBitmap,x,y,null);
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public void update() {
        long timeElapsed=System.nanoTime()-startTime/1000000;
        if(timeElapsed>10000){
            distance++;
            startTime=System.nanoTime();
        }

        if(isUp){
            deltaYplayer-=0.8;
            deltaY=(int)deltaYplayer;
        }
        else{
            deltaYplayer+=0.4;
            deltaY=(int)deltaYplayer;
        }
        if(deltaY>14)
            deltaY=14;
        if(deltaY<-14)
            deltaY=-14;
        y+=deltaY*2;
        deltaY=0;
        if(bottomBorder()>this.heightScreen) {
            y = this.heightScreen - height;
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

    public int getDistance() {
        return distance;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }


    public Bitmap getPlayerBitmap() {
        return playerBitmap;
    }
}