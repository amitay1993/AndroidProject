package com.example.androidgameproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bullet extends Position implements ObjectsInterface {
    private int speed;
    private Bitmap bitmap;

    public Bullet(Bitmap bitmap ,int x,int y) {

        super(x,y,bitmap.getWidth(),bitmap.getHeight());

        this.speed = 18;
        this.bitmap = bitmap;
    }

    @Override
    public void draw(Canvas canvas) {
            canvas.drawBitmap(bitmap,x,y,null);
    }

    @Override
    public void update() {
        x+=speed; // change
    }
}
