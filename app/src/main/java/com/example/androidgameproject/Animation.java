package com.example.androidgameproject;

import android.graphics.Bitmap;

public class Animation {

    private Bitmap[] frames;
    private int currentFrame;
    private long startTime,delay;
    private boolean playOunce;


    public void setFrames(Bitmap[] frames){
        this.frames=frames;
        currentFrame=0;
        startTime=System.nanoTime();
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
    public void update(){
        long elapseTime=(System.nanoTime()-startTime)/1000000;
        if(elapseTime>delay){
            currentFrame++;
            startTime=System.nanoTime();
        }
        if(currentFrame==frames.length){
            currentFrame=0;
            playOunce=true;
        }

    }

    public Bitmap getBitmap() {
        return frames[currentFrame];
    }

    public boolean isPlayOunce() {
        return playOunce;
    }

}
