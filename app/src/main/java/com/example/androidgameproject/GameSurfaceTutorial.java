package com.example.androidgameproject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GameSurfaceTutorial extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private TutorialThread tutorialThread;
    private Player player;
    private ImageBitmaps imageBitmapsClass;
    private Context context;
    private int widthScreen, heightScreen;
    private long bulletStartTime;
    private Background background;
    private List<Bullet> bullets;
    private boolean isGameOver = false,isPressed=false,isStarted=false;


    public GameSurfaceTutorial(Context context, int width, int height) {
        super(context);
        background = new Background(ImageBitmaps.backgroundImg1);
        imageBitmapsClass = new ImageBitmaps(getResources());
        this.context = context;
        widthScreen = width;
        heightScreen = height;
        tutorialThread = new TutorialThread(getHolder(), this);
        getHolder().addCallback(this);
        setFocusable(true);
        player = new Player(ImageBitmaps.playerImg,heightScreen,getResources());
        bullets = new ArrayList<>();
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        tutorialThread.setRunning(true);
        tutorialThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                tutorialThread.setRunning(false);
                tutorialThread.join();
                retry = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (!isGameOver) {
            if (action == MotionEvent.ACTION_DOWN) {
                isStarted=true;

                if (!player.isPlaying()) {
                    player.setPlaying(true);
                } else {
                    player.setUp(true);
                    isPressed = true;
                }
            } else if (action == MotionEvent.ACTION_UP) {
                isPressed=false;
                player.setUp(false);
            }
        }
        return true;
    }

    public void update() {
        if (player.isPlaying()) {
        long bulletTimer = (System.nanoTime() - bulletStartTime) / 1000000;
        if (bulletTimer > 1500) { //change
            bullets.add(new Bullet(ImageBitmaps.bulletImg, player.getX() + player.getWidth(), player.getY() + player.getHeight() / 2 - 9, 17,getResources(),0));
            bulletStartTime = System.nanoTime();
        }
        for (int i=0;i<bullets.size();i++) {
            bullets.get(i).update();
        }
            player.update();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            final int saveState = canvas.save();
            background.draw(canvas);
            player.draw(canvas);
            canvas.restoreToCount(saveState);
            for (Bullet bullet : bullets)
                bullet.draw(canvas);
            drawTxt(canvas);
        }
    }

    private void drawTxt(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        if(!isStarted) {
            canvas.drawText("Press to start", widthScreen / 2, heightScreen / 2, paint);
        }

        if (isStarted) {
            if (!isPressed) {
                canvas.drawText("tap  and hold screen to go up", widthScreen / 2, heightScreen / 2, paint);
            }// player score is distance
            else {
                canvas.drawText("release the screen to go down", widthScreen / 2, heightScreen / 2, paint);
            }
        }
    }
}
