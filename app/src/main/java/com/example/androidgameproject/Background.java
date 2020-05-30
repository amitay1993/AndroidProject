package com.example.androidgameproject;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background  implements ObjectsInterface {
    private Bitmap bitmap;
    private int x,y,deltaX;

    public Background(Bitmap bitmap) {
        this.bitmap = bitmap;
        deltaX=GameSurfaceView.SPEED;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap,x,y,null);
        if(x<0){
            canvas.drawBitmap(bitmap,x+GameSurfaceView.WIDTH,y,null);
        }
    }

    @Override
    public void update() {
        x+=deltaX;
        if(x<(-1)*GameSurfaceView.WIDTH) {
            x=0;
        }

    }


}
