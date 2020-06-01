package com.example.androidgameproject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Explosion extends Position implements ObjectsInterface {
    private int frame;
    Bitmap spriteSheet;
    private final int delay=1;
    Bitmap[] bitmaps;
    long startTime;



    public Explosion(Bitmap spriteSheet, int x, int y, Resources res) {
        super(x, y, 64, 64);
        this.spriteSheet=spriteSheet;
        bitmaps = new Bitmap[16];
       for(int i=0;i<4;i++){
           for(int j=0;j<4;j++){
               bitmaps[i*4+j]=Bitmap.createBitmap(spriteSheet,j*64,i*64,64,64);
           }


       }
        startTime=System.nanoTime();
    }

    @Override
    public void draw(Canvas canvas) {
        if (frame < bitmaps.length) {
           /* long animTimer = (System.nanoTime() - startTime) / 1000000;
            if (animTimer > delay) {*/
                canvas.drawBitmap(bitmaps[frame++],x,y,null);
                /*startTime = System.nanoTime();*/
         //   }
        }
    }

    @Override
    public void update() {


    }
}
