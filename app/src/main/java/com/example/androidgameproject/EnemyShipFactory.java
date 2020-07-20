package com.example.androidgameproject;


import android.content.res.Resources;

import java.util.Random;

public class EnemyShipFactory {
    Random random=new Random();
    public Enemy makeEnemy(String newEnemy){
        if(newEnemy.equals("G")){
                return new Groll(ImageBitmaps.roll1, GameSurfaceView.widthScreen + random.nextInt(20) + 300, (int) (random.nextDouble() * (GameSurfaceView.heightScreen - 150)), GameSurfaceView.player.getDistance(),150);
        }
        else if(newEnemy.equals("D")){
                return new Dragon(ImageBitmaps.dragonImg1, GameSurfaceView.widthScreen + random.nextInt(20) + 100, (int) (random.nextDouble() * (GameSurfaceView.heightScreen - 150)), GameSurfaceView.player.getDistance(), 200);
        }
        else if(newEnemy.equals("M")) {
                return new Missile(ImageBitmaps.missile,GameSurfaceView.widthScreen + random.nextInt(20) + 400, (int) (random.nextDouble() * (GameSurfaceView.heightScreen - 150)),GameSurfaceView.player.getX() ,GameSurfaceView.player.getY());
        }
        else if(newEnemy.equals("W")) {
            return (new Walle(ImageBitmaps.walleImg, GameSurfaceView.widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (GameSurfaceView.heightScreen - 150)), GameSurfaceView.player.getDistance(), 150));
        }
        else if(newEnemy.equals("Y")){
                return new SpaceShip(ImageBitmaps.yellowSpaceshipImg, GameSurfaceView.widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (GameSurfaceView.heightScreen - 150)), GameSurfaceView.player.getDistance(),  0);
            }
        else if(newEnemy.equals("UG")) {
            return new UfoGreen(ImageBitmaps.ufoGreenImg, GameSurfaceView.widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (GameSurfaceView.heightScreen - 182)), GameSurfaceView.player.getDistance(), 0);
        }
         else if(newEnemy.equals("UY")){
                return new UfoYellow(ImageBitmaps.ufoYellowImg, GameSurfaceView.widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (GameSurfaceView.heightScreen - 182)), GameSurfaceView.player.getDistance(), 0);
         }
         else if(newEnemy.equals("UR")){
                return new UfoRed(ImageBitmaps.ufoRedImg, GameSurfaceView.widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (GameSurfaceView.heightScreen - 182)), GameSurfaceView.player.getDistance(), 0);
        }
        else if(newEnemy.equals("SP")) {
            return new SpaceShip(ImageBitmaps.yellowSpaceshipImg, GameSurfaceView.widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (GameSurfaceView.heightScreen - 150)), GameSurfaceView.player.getDistance(),  0);
        }
        else {
            return new Skeleton(ImageBitmaps.skel1, GameSurfaceView.widthScreen + random.nextInt(20) + 200, (int) (random.nextDouble() * (GameSurfaceView.heightScreen - 150)), GameSurfaceView.player.getDistance(),120);
        }
    }
}
