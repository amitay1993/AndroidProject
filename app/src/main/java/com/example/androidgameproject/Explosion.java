package com.example.androidgameproject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Explosion extends Position implements ObjectsInterface {

    private int frame;
    private Bitmap[] bitmaps;

    public Explosion(Bitmap spriteSheet, int x, int y, Resources res) {
        super(x, y, 64, 64);
        final int rowsCols=4;
        bitmaps = new Bitmap[rowsCols*rowsCols];
       for(int i=0;i<rowsCols;i++){
           for(int j=0;j<rowsCols;j++){
               bitmaps[i*rowsCols+j]=Bitmap.createBitmap(spriteSheet,j*width,i*height,width,height);
           }
       }
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
