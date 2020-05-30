package com.example.androidgameproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Enemy extends Position implements ObjectsInterface {
    private int score,speed;
    private Random random;
    private Bitmap bitmap;

    public Enemy(Bitmap bitmap,int x,int y,int speed) {
        super(x,y,bitmap.getWidth(),bitmap.getHeight());
        this.bitmap = bitmap;
        this.speed=4+(int)random.nextDouble()*30/score; // change
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
