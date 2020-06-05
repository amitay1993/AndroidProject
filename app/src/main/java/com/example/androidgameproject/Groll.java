package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Groll extends Enemy {
    public Groll(Bitmap bitmap, int x, int y, int score, Resources res,int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);

        bitmaps.add(bitmap);
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.roll1));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.noll_003));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.roll4));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.roll5));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.roll6));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.roll7));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.roll8));


        speed = 8 + (int) (random.nextDouble() * score / 30); // change
        if (speed >30) {
            this.speed = 30;
        }
    }
}
