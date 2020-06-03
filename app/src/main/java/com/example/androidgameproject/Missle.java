package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;



public class Missle extends Enemy {
    int up;
    public Missle(Bitmap bitmap, int x, int y, int score, Resources res,int up) {
        super(x, y, bitmap.getWidth(),bitmap.getHeight(),0);

        if(up==0){
            
            bitmaps.add(BitmapFactory.decodeResource(res, R.drawable.rsz_downmissile));
        }else{
            bitmaps.add(bitmap);
        }

        this.up=up;
        speed = 4 + (int) (random.nextDouble() * score / 30); // change
        if (speed >30) {
            this.speed = 30;
        }
    }
    @Override
    public void update() {
        if(this.up==1) {
            x -= speed + 30;
            y -= speed + 2;
        }else{
            x-= speed + 30;
            y+= speed + 2;
        }
    }
}
