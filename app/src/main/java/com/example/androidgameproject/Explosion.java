package com.example.androidgameproject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Explosion extends Position implements ObjectsInterface {
    private int x,y,row,frames;
    Bitmap bitmaps[];


    public Explosion(Bitmap bitmap, int x, int y, Resources res) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight());
        bitmaps = new Bitmap[3];
        bitmaps[0]=bitmap;
        bitmaps[1]= BitmapFactory.decodeResource(res, R.drawable.frame0001);
        bitmaps[2]= BitmapFactory.decodeResource(res, R.drawable.frame0002);
    }

    @Override
    public void draw(Canvas canvas) {
        for(Bitmap bitmap:bitmaps)
         canvas.drawBitmap(bitmap,x,y,null);
    }

    @Override
    public void update() {

    }
}
