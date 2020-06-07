package com.example.androidgameproject;

import android.graphics.Bitmap;

public class UfoGreen extends Enemy {
    public UfoGreen(Bitmap bitmap, int x, int y, int distance, int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);
        bitmaps.add(bitmap);
        speed = 10 + (int) (random.nextDouble() * distance / 28); // change
        if (speed >29) {
            this.speed = 29;
        }
    }
}