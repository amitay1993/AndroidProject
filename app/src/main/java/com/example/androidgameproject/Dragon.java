package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class Dragon extends Enemy {
    public Dragon(Bitmap bitmap, int x, int y, int distance, int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);
        bitmaps.add(bitmap);
        bitmaps.add(ImageBitmaps.dragonImg2);
        speed = 10 + (int) (random.nextDouble() * distance / 30);
        if (speed >30) {
            this.speed = 30;
        }
    }
}
