package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

// draw bullet according to the bulledIndex, each bullet is different image

public class Bullet extends Position implements ObjectsInterface {
    private int speed;
    private Bitmap[] bitmaps;
    private int bulletIndex;

    public Bullet(Bitmap bitmap , int x, int y, int speed,int bulletIndex) {
        super(x,y,bitmap.getWidth()/2,bitmap.getHeight());
        this.bulletIndex=bulletIndex;
        this.speed = speed;
        this.bitmaps =new Bitmap[3];
        bitmaps[0]=bitmap;
        bitmaps[1]= ImageBitmaps.bulletImg1;
        bitmaps[2]= ImageBitmaps.bulletImg2;
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
            width=bitmaps[1].getWidth()/2;
        }
        else if(bulletIndex==2){
            y-=36;
            height=bitmaps[2].getHeight();
            width=bitmaps[2].getWidth()/2;
        }
    }
    public Rect getRect(){
        return new Rect(x+8,y-5,rightBorder()-8,bottomBorder()+5);
    }
    public boolean collisionDetection(Position second ){
        return Rect.intersects(getRect(), second.getRect());
    }

}
