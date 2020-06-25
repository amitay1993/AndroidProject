package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Coin extends Allie  {

    public Coin(Bitmap bitmap, int x, int y){
        super(bitmap,x,y);
    }

    @Override
    public void update() {
        final int speed=11;
        x-=speed;
    }
}
