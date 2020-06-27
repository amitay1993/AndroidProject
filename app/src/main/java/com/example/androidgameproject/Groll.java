package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;

public class Groll extends Enemy {
    public Groll(Bitmap bitmap, int x, int y, int distance, Resources res,int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);

        bitmaps.add(bitmap);
        bitmaps.add(ImageBitmaps.roll2);
        bitmaps.add(ImageBitmaps.roll3);
        bitmaps.add(ImageBitmaps.roll4);

        speed = 10 + (int) (random.nextDouble() * distance / 30); 
        if (speed >28) {
            this.speed = 28;
        }
    }
    public Rect getRect(){
        return new Rect(x+10,y-10,rightBorder()-10,bottomBorder()+10);
    }
}
