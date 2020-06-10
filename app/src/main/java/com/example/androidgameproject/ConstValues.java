package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public class ConstValues {
    static Bitmap coinImg,ufoGreemImg,ufoRedImg,ufoYellowImg,ufoLightGreenImg,missileUpImg,missileDownImg,walleImg,
    yellowSpaceshipImg,roll0,roll1,roll2,roll3,roll4,roll5,roll6,roll7,roll8;
    public ConstValues(Resources res){
        coinImg=BitmapFactory.decodeResource(res, R.drawable.coin);
        ufoGreemImg = BitmapFactory.decodeResource(res, R.drawable.ufo_green);
        ufoRedImg=BitmapFactory.decodeResource(res, R.drawable.ufo_red);
        ufoYellowImg= BitmapFactory.decodeResource(res, R.drawable.ufo_yellow);
        ufoLightGreenImg=BitmapFactory.decodeResource(res, R.drawable.ufo_green);
        missileUpImg=BitmapFactory.decodeResource(res, R.drawable.rsz_rotatemissile);
        walleImg=BitmapFactory.decodeResource(res, R.drawable.walle);
        missileDownImg=BitmapFactory.decodeResource(res, R.drawable.rsz_downmissile);
        yellowSpaceshipImg= BitmapFactory.decodeResource(res, R.drawable.rsz_spaceship1);

        roll0=BitmapFactory.decodeResource(res, R.drawable.roll0);
        roll1=BitmapFactory.decodeResource(res, R.drawable.roll1);
        roll2=(BitmapFactory.decodeResource(res, R.drawable.noll_003));
        roll3=BitmapFactory.decodeResource(res, R.drawable.roll4);
        roll4=BitmapFactory.decodeResource(res, R.drawable.roll5);
        roll5=BitmapFactory.decodeResource(res, R.drawable.roll6);
        roll6=BitmapFactory.decodeResource(res, R.drawable.roll7);
        roll7=BitmapFactory.decodeResource(res, R.drawable.roll8);



    }
}
