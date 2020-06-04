package com.example.androidgameproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Obstacle extends Position implements ObjectsInterface{
    private int score;
    final private int speed=12;
    private Bitmap bitmap;

    public Obstacle(Bitmap bitmap,int x,int y) {
        super(x,y,bitmap.getWidth(),bitmap.getHeight());
        this.bitmap = bitmap;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap,x,y,null);
    }

    @Override
    public void update() {
        x-=speed;
    }


}
