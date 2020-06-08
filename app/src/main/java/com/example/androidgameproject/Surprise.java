package com.example.androidgameproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;



public class Surprise extends Position implements ObjectsInterface {
    private Bitmap bitmap;
    public Surprise(Bitmap bitmap, int x, int y) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight());

        this.bitmap=bitmap;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void update() {

    }
}
