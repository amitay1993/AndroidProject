package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Coin extends Position implements ObjectsInterface {

    private Bitmap bitmap;
    public Coin(Bitmap bitmap, int x, int y){
        super(x,y,bitmap.getWidth(),bitmap.getHeight());

        this.bitmap=bitmap;

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap,x,y,null);
    }

    @Override
    public void update() {
        final int speed=10;
        x-=speed;
    }
}
