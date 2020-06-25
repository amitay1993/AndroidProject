package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player extends Position implements ObjectsInterface {

    private Bitmap playerBitmap;
    private int distance,deltaY;
    private double deltaYplayer;
    private boolean isUp,isPlaying;
    private long startTime;
    private int heightScreen;
    private Bitmap shieldBitmap;
    private Paint shieldInnerPaint,shieldOuterPaint;
    private int shieldRadius;
    private boolean hasShield;

    public Player(Bitmap bitmap, int heightScreen, Resources resources) { //change
        super(100,heightScreen /2-bitmap.getHeight()/2,bitmap.getWidth(),bitmap.getHeight());
        this.playerBitmap = bitmap;
        deltaYplayer =0;
        distance =0;
        shieldRadius=height / 2+20;
        this.heightScreen=heightScreen;
        shieldInnerPaint =new Paint();
        shieldInnerPaint.setStyle(Paint.Style.FILL);
        shieldInnerPaint.setColor(resources.getColor(R.color.lightpurplealpha));

        shieldOuterPaint=new Paint();
        shieldOuterPaint.setStyle(Paint.Style.STROKE);
        shieldOuterPaint.setStrokeWidth(10);
        shieldOuterPaint.setColor(resources.getColor(R.color.lightbluealpha));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(playerBitmap, x, y, null);
        if(hasShield) {
            canvas.drawCircle(x + width / 2 - 20, y + height / 2, shieldRadius, shieldInnerPaint);
            canvas.drawCircle(x + width / 2 - 20, y + height / 2, shieldRadius, shieldOuterPaint);
        }
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

    public boolean isHasShield() {
        return hasShield;
    }

    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
    }

    public int getShieldRadius() {
        return shieldRadius;
    }
}