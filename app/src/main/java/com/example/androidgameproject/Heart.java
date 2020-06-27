package com.example.androidgameproject;

import android.graphics.Bitmap;

public class Heart extends Allie {
    public Heart(Bitmap bitmap, int x, int y){
        super(bitmap,x,y);
    }

    @Override
    public void update() {
        final int speed=13;
        x-=speed;
    }
}
