package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

public class Enemy extends Position implements ObjectsInterface {
    private int score,speed;
    private Random random;
    private Bitmap[] bitmaps;
    private int frame=0;
    private final int delay=200;
    private long startTime;

    public Enemy(Bitmap bitmap, int x, int y,int score,Resources res) {
        super(x,y,bitmap.getWidth(),bitmap.getHeight());
      //  this.bitmap = bitmap;
        bitmaps =new Bitmap[2];
        bitmaps[0]=bitmap;
        bitmaps[1]= BitmapFactory.decodeResource(res,R.drawable.rsz_dragon1);
        random=new Random();
        this.score=score;
        this.speed=4+(int)random.nextDouble()*30/(score); // change
        if(speed>40){
            this.speed=40;
        }//speed limit
        startTime=System.nanoTime();
    }

    @Override
    public void draw(Canvas canvas)
    {
        long animTimer=(System.nanoTime()-startTime)/1000000;
        if(animTimer>delay-speed) {
            canvas.drawBitmap(this.bitmaps[frame++ % bitmaps.length], x, y, null);
            startTime = System.nanoTime();
        }else{
            canvas.drawBitmap(this.bitmaps[frame % bitmaps.length], x, y, null);
        }
    }

    @Override
    public void update() {
        x-=speed;

    }

}
