package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Walle extends Enemy {
    public Walle(Bitmap bitmap, int x, int y, int score, Resources res) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),150);
        bitmaps.add(bitmap);
        speed = 8 + (int) (random.nextDouble() * score / 30); // change
        if (speed >30) {
            this.speed = 30;
        }
    }
}