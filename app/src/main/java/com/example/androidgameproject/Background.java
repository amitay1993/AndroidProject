package com.example.androidgameproject;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background  implements ObjectsInterface {
    private Bitmap bitmap;
    private int x,y;

    public Background(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap,x,y,null);
        if(x<0){ // when background out of screen draw it from start
            canvas.drawBitmap(bitmap,x+GameSurfaceView.widthScreen,y,null);
        }
    }
        public void setBitmap(Bitmap bitmap){
        this.bitmap=bitmap;
    }

    @Override
    public void update() {
        final int deltaX = -5;

        x+=deltaX;
        if(x<(-1)*GameSurfaceView.widthScreen) {
            x=0;
        }

    }


}
