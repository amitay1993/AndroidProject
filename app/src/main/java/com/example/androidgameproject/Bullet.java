package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Bullet extends Position implements ObjectsInterface {
    private int speed;
    private Bitmap[] bitmaps;
    private int bulletIndex;

    public Bullet(Bitmap bitmap , int x, int y, int speed, Resources res,int bulletIndex) {
        super(x,y,bitmap.getWidth(),bitmap.getHeight());
        this.bulletIndex=bulletIndex;
        this.speed = speed;

        this.bitmaps =new Bitmap[3];
        bitmaps[0]=bitmap;
        bitmaps[1]=BitmapFactory.decodeResource(res,R.drawable.superblue);
        bitmaps[2]=BitmapFactory.decodeResource(res,R.drawable.supered);
        bulletPos();


    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(bitmaps[bulletIndex],x,y,null);
    }

    @Override
    public void update() {
        x+=speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setBulletIndex(int bulletIndex) {
        this.bulletIndex = bulletIndex;
    }
    private void bulletPos(){
        if(bulletIndex==1){
            y-=46;
            height=bitmaps[1].getHeight();
            width=bitmaps[1].getWidth();
        }
        else if(bulletIndex==2){
            y-=36;
            height=bitmaps[2].getHeight();
            width=bitmaps[2].getWidth();
        }
    }
    public Rect getRect(){
        return new Rect(x,y-5,rightBorder(),bottomBorder()+5);
    }

}
