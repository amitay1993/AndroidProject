package com.example.androidgameproject;


import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Enemy extends Position implements ObjectsInterface {
    protected int speed;
    protected Random random;
    protected List<Bitmap> bitmaps;
    private int frame;
    private int delay;
    private long startTime;

    public Enemy( int x, int y,int width,int height,int delay) {
        super(x,y,width,height);
        frame=0;
        random=new Random();
        this.delay=delay;
        bitmaps=new ArrayList<>();
        startTime=System.nanoTime();
    }

    @Override
    public void draw(Canvas canvas)
    {
        long animTimer=(System.nanoTime()-startTime)/1000000;
        if(animTimer>delay-speed) {
            canvas.drawBitmap(bitmaps.get(frame++ % bitmaps.size()), x, y, null);
            startTime = System.nanoTime();
        }else{
            canvas.drawBitmap(bitmaps.get(frame % bitmaps.size()), x, y, null);
        }
    }

    @Override
    public void update() {
        x-=speed;

    }

}
