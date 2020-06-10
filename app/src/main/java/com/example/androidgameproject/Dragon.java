package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Dragon extends Enemy {
    public Dragon(Bitmap bitmap, int x, int y, int distance, Resources res, int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);
        System.out.println(width+" "+height +"baby");
        bitmaps.add(bitmap);
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.rsz_dragon2));
        speed = 10 + (int) (random.nextDouble() * distance / 30); // change
        if (speed >30) {
            this.speed = 30;
        }
    }

}
