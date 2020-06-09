package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Skeleton extends Enemy {
    public Skeleton(Bitmap bitmap, int x, int y, int distance, Resources res,int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);

        bitmaps.add(bitmap);
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_003));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_004));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_005));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_006));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_007));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_008));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_009));
        speed = 10 + (int) (random.nextDouble() * distance / 30); // change
        if (speed >28) {
            this.speed = 28;
        }
    }
}
