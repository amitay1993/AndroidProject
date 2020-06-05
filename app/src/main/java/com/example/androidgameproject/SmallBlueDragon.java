package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class SmallBlueDragon extends Enemy {
    public SmallBlueDragon(Bitmap bitmap, int x, int y, int distance, int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(), delay);
        bitmaps.add(bitmap);

        speed = 11 + (int) (random.nextDouble() * distance / 30); // change
        if (speed > 27) {
            this.speed = 27;
        }
    }
}
