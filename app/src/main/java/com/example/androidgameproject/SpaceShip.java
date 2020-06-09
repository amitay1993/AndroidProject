package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class SpaceShip extends Enemy{
    public SpaceShip(Bitmap bitmap, int x, int y, int distance, int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);
        bitmaps.add(bitmap);

        speed = 12 + (int) (random.nextDouble() * distance / 30); // change
        if (speed >30) {
            this.speed = 30;
        }
    }
}
