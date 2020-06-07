package com.example.androidgameproject;

import android.graphics.Bitmap;

public class UfoYellow extends Enemy {
    public UfoYellow(Bitmap bitmap, int x, int y, int distance, int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);
        bitmaps.add(bitmap);
        speed = 12 + (int) (random.nextDouble() * distance / 28); // change
        if (speed >32) {
            this.speed = 32;
        }
    }
}