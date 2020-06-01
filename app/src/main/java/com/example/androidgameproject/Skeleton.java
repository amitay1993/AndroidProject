package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Skeleton extends Enemy {
    public Skeleton(Bitmap bitmap, int x, int y, int score, Resources res) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),120);

        bitmaps.add(bitmap);
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_003));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_004));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_005));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_006));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_007));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_008));
        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_009));
        speed = 8 + (int) (random.nextDouble() * score / 30); // change
        if (speed >30) {
            this.speed = 30;
        }
    }
}
