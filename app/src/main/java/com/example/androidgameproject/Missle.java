package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Missle extends Enemy {
    int upOrDown;
    public Missle(Bitmap bitmap, int x, int y, int score, Resources res,int upOrDown) {
        super(x, y, bitmap.getWidth(),bitmap.getHeight(),0);

        if(upOrDown==0){
            bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.rsz_downmissile));
        }else{
            bitmaps.add(bitmap);
        }

        this.upOrDown =upOrDown;
        speed = 4 + (int) (random.nextDouble() * score / 30); // change
        if (speed >30) {
            this.speed = 30;
        }
    }
    @Override
    public void update() {
        if(this.upOrDown ==1) {
            x -= speed + 30;
            y -= speed + 2;
        }else{
            x-= speed + 30;
            y+= speed + 2;
        }
    }
}
