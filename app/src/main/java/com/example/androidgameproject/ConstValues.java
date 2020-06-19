package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public class ConstValues {
    static Bitmap coinImg,ufoGreemImg,ufoRedImg,ufoYellowImg,ufoLightGreenImg,missile,walleImg,
    yellowSpaceshipImg,roll0,roll1,roll2,roll3,roll4,skel0,skel1,skel2,skel3,skel4,skel5,skel6,skel7,skel8;
    public ConstValues(Resources res){
        coinImg=BitmapFactory.decodeResource(res, R.drawable.coin);
        ufoGreemImg = BitmapFactory.decodeResource(res, R.drawable.ufo_green);
        ufoRedImg=BitmapFactory.decodeResource(res, R.drawable.ufo_red);
        ufoYellowImg= BitmapFactory.decodeResource(res, R.drawable.ufo_yellow);
        ufoLightGreenImg=BitmapFactory.decodeResource(res, R.drawable.ufo_green);
        missile=BitmapFactory.decodeResource(res, R.drawable.rsz_straightmissile);
        walleImg=BitmapFactory.decodeResource(res, R.drawable.walle);
        yellowSpaceshipImg= BitmapFactory.decodeResource(res, R.drawable.rsz_spaceship1);

      //  roll0=BitmapFactory.decodeResource(res, R.drawable.roll0);
        roll1=BitmapFactory.decodeResource(res, R.drawable.roll1);
        roll2=(BitmapFactory.decodeResource(res, R.drawable.roll2));
        roll3=BitmapFactory.decodeResource(res, R.drawable.roll4);
        roll4=BitmapFactory.decodeResource(res, R.drawable.roll6);



       // skel0=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_003));
        skel1=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_004));
        skel2=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_005));
        skel3=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_006));
        skel4=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_007));
        skel5=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_008));
        skel6=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_009));
        skel7=BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_009);
        skel8=BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_009);



    }
}
