package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class Skeleton extends Enemy {
    public Skeleton(Bitmap bitmap, int x, int y, int distance,int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);

        bitmaps.add(bitmap);
   //     bitmaps.add(ConstValues.skel1);
        bitmaps.add(ImageBitmaps.skel2);
        bitmaps.add(ImageBitmaps.skel3);
        bitmaps.add(ImageBitmaps.skel4);
        bitmaps.add(ImageBitmaps.skel5);
        bitmaps.add(ImageBitmaps.skel6);
        bitmaps.add(ImageBitmaps.skel7);


        speed = 10 + (int) (random.nextDouble() * distance / 30);
        if (speed >28) {
            this.speed = 28;
        }
    }
}
