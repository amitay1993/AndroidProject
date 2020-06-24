package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Skeleton extends Enemy {
    public Skeleton(Bitmap bitmap, int x, int y, int distance, Resources res,int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);

        bitmaps.add(bitmap);
   //     bitmaps.add(ConstValues.skel1);
        bitmaps.add(ConstValues.skel2);
        bitmaps.add(ConstValues.skel3);
        bitmaps.add(ConstValues.skel4);
        bitmaps.add(ConstValues.skel5);
        bitmaps.add(ConstValues.skel6);
        bitmaps.add(ConstValues.skel7);


        speed = 10 + (int) (random.nextDouble() * distance / 30);
        if (speed >28) {
            this.speed = 28;
        }
    }
}
