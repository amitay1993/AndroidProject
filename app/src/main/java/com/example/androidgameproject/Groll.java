package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Groll extends Enemy {
    public Groll(Bitmap bitmap, int x, int y, int distance, Resources res,int delay) {
        super(x, y, bitmap.getWidth(), bitmap.getHeight(),delay);

        bitmaps.add(bitmap);
//        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.roll1));
//        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.noll_003));
//        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.roll4));
//        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.roll5));
//        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.roll6));
//        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.roll7));
//        bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.roll8));


        bitmaps.add(ConstValues.roll1);
        bitmaps.add(ConstValues.roll2);
        bitmaps.add(ConstValues.roll3);
        bitmaps.add(ConstValues.roll4);
        bitmaps.add(ConstValues.roll5);
        bitmaps.add(ConstValues.roll6);
        bitmaps.add(ConstValues.roll7);


        speed = 10 + (int) (random.nextDouble() * distance / 30); // change
        if (speed >28) {
            this.speed = 28;
        }
    }
    public Rect getRect(){
        return new Rect(x+10,y-10,rightBorder()-10,bottomBorder());
    }
}
