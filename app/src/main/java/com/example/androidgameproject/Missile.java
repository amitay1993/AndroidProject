package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;


public class Missile extends Enemy {
    int upOrDown;
    float x,y,velocityX, velocityY;
    public Missile(Bitmap bitmap, int x, int y, int playerX, int playerY) {
        super(x, y, bitmap.getWidth(),bitmap.getHeight(),0);
        this.x=x;
        this.y=y;
        calculateDirection(playerX,playerY);
        Bitmap bInput= bitmap;
        float degrees = (float) Math.toDegrees(Math.tan(velocityY/velocityX)); //rotation degree
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
       bitmaps.add(Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true));


         this.upOrDown =upOrDown;

    }
    @Override
    public void update() {
        x += velocityX;
        y += velocityY;
        super.x=(int)this.x;
        super.y= (int) this.y;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmaps.get(0),this.x,this.y,null);

    }

    private void calculateDirection(float x, float y){
        float distanceX,distanceY;
        distanceX=x-this.x;
        distanceY=y-this.y;
        float realDistance= (float) Math.sqrt(distanceX*distanceX+distanceY*distanceY);
        velocityX=(distanceX/realDistance)*15;
        velocityY =(distanceY/realDistance)*15;

    }
}
