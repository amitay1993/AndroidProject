package com.example.androidgameproject;

import android.graphics.Bitmap;

public class UfoRed extends Enemy {
    public UfoRed(Bitmap bitmap, int x, int y, int distance, int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);
        bitmaps.add(bitmap);
        speed = 11 + (int) (random.nextDouble() * distance / 28);
        if (speed >31) {
            this.speed = 31;
        }
    }
}
