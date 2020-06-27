package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;




public class ImageBitmaps {
    static Bitmap coinImg,ufoGreemImg,ufoRedImg,ufoYellowImg,ufoLightGreenImg,missile,walleImg,heartImg,powerUpImg,bulletImg,bulletImg2,bulletImg1,
    yellowSpaceshipImg,roll0,roll1,roll2,roll3,roll4,skel0,skel1,skel2,skel3,skel4,skel5,skel6,skel7,skel8,shieldImg,dragonImg1,dragonImg2,playerImg,
    explosionImg,obstacleImg,backgroundImg1,backgroundImg2,backgroundImg3,backgroundImg4,backgroundTutorialImg,fingerTutotial,goldMedal,silverMedal,bronzeMedal;


    public ImageBitmaps(Resources res){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        coinImg=BitmapFactory.decodeResource(res, R.drawable.coin);
        heartImg=BitmapFactory.decodeResource(res, R.drawable.heartrsz);
        heartImg= Bitmap.createScaledBitmap(heartImg,100,100,false);
        powerUpImg=BitmapFactory.decodeResource(res, R.drawable.power_up);
        bulletImg=BitmapFactory.decodeResource(res, R.drawable.bullet);
        bulletImg1=BitmapFactory.decodeResource(res, R.drawable.superblue);
        bulletImg2=BitmapFactory.decodeResource(res, R.drawable.supered);
        explosionImg=BitmapFactory.decodeResource(res, R.drawable.explosion_new,options);
        obstacleImg=BitmapFactory.decodeResource(res, R.drawable.new_pillar);
        ufoGreemImg = BitmapFactory.decodeResource(res, R.drawable.ufo_green);
        ufoRedImg=BitmapFactory.decodeResource(res, R.drawable.ufo_red);
        ufoYellowImg= BitmapFactory.decodeResource(res, R.drawable.ufo_yellow);
        ufoLightGreenImg=BitmapFactory.decodeResource(res, R.drawable.ufo_green);
        missile=BitmapFactory.decodeResource(res, R.drawable.rsz_straightmissile);
        walleImg=BitmapFactory.decodeResource(res, R.drawable.walle);
        yellowSpaceshipImg= BitmapFactory.decodeResource(res, R.drawable.rsz_spaceship1);
        shieldImg =BitmapFactory.decodeResource(res,R.drawable.shield);
        dragonImg1= BitmapFactory.decodeResource(res, R.drawable.rsz_dragon1);
        dragonImg2 =BitmapFactory.decodeResource(res,R.drawable.rsz_dragon2);
        playerImg=BitmapFactory.decodeResource(res,R.drawable.player);


        roll1=BitmapFactory.decodeResource(res, R.drawable.roll1);
        roll2=(BitmapFactory.decodeResource(res, R.drawable.roll2));
        roll3=BitmapFactory.decodeResource(res, R.drawable.roll4);
        roll4=BitmapFactory.decodeResource(res, R.drawable.roll6);


        skel1=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_004));
        skel2=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_005));
        skel3=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_006));
        skel4=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_007));
        skel5=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_008));
        skel6=(BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_009));
        skel7=BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_009);
        skel8=BitmapFactory.decodeResource(res, R.drawable.keleton_slashing_009);

        backgroundImg1=BitmapFactory.decodeResource(res,R.drawable.background_oron_1);
        backgroundImg2=BitmapFactory.decodeResource(res,R.drawable.oron_background2_new);
        backgroundImg3=BitmapFactory.decodeResource(res,R.drawable.background_oron3);
        backgroundImg4=BitmapFactory.decodeResource(res,R.drawable.background_oron4);
        backgroundTutorialImg=BitmapFactory.decodeResource(res,R.drawable.background_oron_1);


        fingerTutotial=BitmapFactory.decodeResource(res,R.drawable.rsz_finger);

        goldMedal=BitmapFactory.decodeResource(res,R.drawable.medalgold);
        silverMedal=BitmapFactory.decodeResource(res,R.drawable.medalsilver);
        bronzeMedal=BitmapFactory.decodeResource(res,R.drawable.bronzemedal);





    }
}
