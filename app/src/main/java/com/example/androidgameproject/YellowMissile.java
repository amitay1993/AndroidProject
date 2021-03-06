package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class YellowMissile extends Enemy {

    public YellowMissile(Bitmap bitmap, int x, int y, int score, int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(), delay);
        this.speed = 10 + (int) (random.nextDouble() * score / 30);
        if (speed >30) {
            this.speed = 30;
        }
    }
}
