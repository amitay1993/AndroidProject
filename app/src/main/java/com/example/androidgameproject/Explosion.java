package com.example.androidgameproject;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Explosion extends Position implements ObjectsInterface {

    private int frame;
    private Bitmap[] bitmaps;


    public Explosion(Bitmap spriteSheet, int x, int y) {
        super(x, y, 100, 100);
        final int rows=3,cols=5;

        bitmaps = new Bitmap[rows*cols];
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                bitmaps[i*cols+j]=Bitmap.createBitmap(spriteSheet,j*width,i*height,width,height);
            }
        }
    }


    @Override
    public void draw(Canvas canvas) {
        if (frame < bitmaps.length) {
            //  long animTimer = (System.nanoTime() - startTime) / 1000000;
            //    if (animTimer > delay) {
            canvas.drawBitmap(bitmaps[frame++],x,y,null);
            //   startTime = System.nanoTime();
        }
    }
    //   while (frame<bitmaps.length)
    //     canvas.drawBitmap(bitmaps[frame++],x,y,null);



    @Override
    public void update() {


    }
}