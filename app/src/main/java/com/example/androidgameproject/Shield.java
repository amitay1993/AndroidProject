package com.example.androidgameproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Shield extends Allie  {
    public Shield(Bitmap bitmap, int x, int y){
        super(bitmap,x,y);
    }
    @Override
    public void update() {
        final int speed=12;
        x-=speed;
    }
}
