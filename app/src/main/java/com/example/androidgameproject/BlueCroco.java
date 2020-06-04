package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BlueCroco extends Enemy{
    public BlueCroco(Bitmap bitmap, int x, int y, int score, Resources res,int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);
        bitmaps.add(bitmap);

        speed = 4 + (int) (random.nextDouble() * score / 30); // change
        if (speed >25) {
            this.speed = 25;
        }
    }
}
