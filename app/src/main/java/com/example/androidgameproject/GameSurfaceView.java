package com.example.androidgameproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaRouter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    MainThread mainThread;
    GameActivity gameActivity;
    public static final int SPEED = -5; //change
    private Background[] backgrounds;
    private Player player;
    private List<Bullet> bullets;
    private long bulletStartTime, enemyStartTime, obstacleStartTime,coinStartTime;
    static int widthScreen, heightScreen;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private List<Coin> coins;
    Random random = new Random();
    private Explosion explosion;
    private boolean isGameOver=false;
    private int bScore,coin_counter,backNumber,life_counter=3,bullet_speed=10;
    private Bitmap coinImg,life,pauseBtn;
    Context context;
    private GameListener gameListenerDialogBox;



    public GameSurfaceView(Context context, int width, int height) {
        super(context);
        gameListenerDialogBox=((GameListener)context);
        this.context=context;
        widthScreen = width;
        heightScreen = height;
        mainThread = new MainThread(getHolder(), this);
        getHolder().addCallback(this);
        setFocusable(true);
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        obstacles = new ArrayList<>();
        coins=new ArrayList<>();
        coinImg=BitmapFactory.decodeResource(getResources(),R.drawable.coin);
        life=BitmapFactory.decodeResource(getResources(),R.drawable.heart);
        pauseBtn=BitmapFactory.decodeResource(getResources(),R.drawable.pausebtn);
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player));
        backgrounds=new Background[4];
        backgrounds[0] = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background1));
        backgrounds[1] = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background2));
        backgrounds[2] = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background3));
        backgrounds[3] = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background4));
        bulletStartTime = enemyStartTime = obstacleStartTime = System.nanoTime();






    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainThread.setRunning(true);
        mainThread.start();

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                mainThread.setRunning(false);
                mainThread.join();
                retry=false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(!isGameOver) {
            if (action == MotionEvent.ACTION_DOWN) {
                if (!player.isPlaying()) {
                    player.setPlaying(true);
                } else
                    player.setUp(true);
            } else if (action == MotionEvent.ACTION_UP) {
                player.setUp(false);
            }
        }
        return true;
    }

    public void update() {

        if (player.isPlaying()) {

            setBackNumber();
            backgrounds[backNumber].update();;
            player.update();

            long bulletTimer = (System.nanoTime() - bulletStartTime) / 1000000;
            if (bulletTimer > 2500 - player.getScore() / 3) { //change
                bullets.add(new Bullet(BitmapFactory.decodeResource(getResources(), R.drawable.bullet), player.getX() + player.getWidth(), player.getY() + player.getHeight() / 2 - 9,bullet_speed));
                bulletStartTime = System.nanoTime();
            }
            for (Bullet bullet : bullets) {
                bullet.update();

                if (bullet.leftBorder() > widthScreen + 200) //change
                {
                    bullets.remove(bullet);
                    break;
                }
            }
            long cointTimeElapsed=(System.nanoTime()-coinStartTime)/1000000;
            if(cointTimeElapsed>5500-player.getScore()/2){
                coins.add(new Coin(BitmapFactory.decodeResource(getResources(), R.drawable.coin), widthScreen + 10, (int) (random.nextDouble() * (heightScreen -20 ))));
                coinStartTime=System.nanoTime();
            }



            long enemyTimer = (System.nanoTime() - enemyStartTime) / 1000000;
            if (enemyTimer > 3000 - player.getScore() / 3) {
                addEnemies();
               // enemies.add(new Dragon(BitmapFactory.decodeResource(getResources(), R.drawable.rsz_dragon1), widthScreen + 10, (int) (random.nextDouble() * (heightScreen -130 )), player.getScore(), getResources()));
                enemyStartTime = System.nanoTime();
            }

            for(Coin coin: coins){
                coin.update();
                if (collisionDetection(player, coin)) {
                   // Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
                    coins.remove(coin);
                    bScore+=20;
                    coin_counter++;
                    break;
                }
            }

            for (Enemy enemy : enemies) {
                enemy.update();

                if (collisionDetection(player, enemy)) {
                    enemies.remove(enemy);
                    explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.exp2_0), enemy.getX(), enemy.getY(), getResources());
                    life_counter--;
                    if(life_counter==0){
                        gameOver();
                    }
                    break;
                }

                if (enemy.rightBorder() < 0) {
                    Log.d("enemy", "enemy");
                    enemies.remove(enemy);
                    break;
                }
                for (Bullet bullet : bullets) {
                    if (collisionDetection(enemy, bullet)) {
                       // Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.exp2_0), enemy.getX(), enemy.getY(), getResources());
                        enemies.remove(enemy);
                        bullets.remove(bullet);
                        bScore+=20;
                        break;
                    }
                }

            }

            long obstacleTimer = (System.nanoTime() - obstacleStartTime) / 1000000;
            if (obstacleTimer > 20000 - player.getScore() / 4) {
                obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(), R.drawable.rsz_pillarnew1crop_removebg), widthScreen + 10, heightScreen / 2));
                obstacleStartTime = System.nanoTime();
            }
            for (Obstacle obstacle : obstacles) {
                obstacle.update();
                if (collisionDetection(player, obstacle)) {
                    life_counter=0;
                    gameOver();
                    break;
                }
                if (obstacle.rightBorder() < 0) {
                    obstacles.remove(obstacle);
                }

            }

        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            final int saveState = canvas.save();
            backgrounds[backNumber].draw(canvas);

            player.draw(canvas);
            canvas.restoreToCount(saveState);
            for (Bullet bullet : bullets)
                bullet.draw(canvas);
            for (Enemy enemy : enemies)
                enemy.draw(canvas);
            for (Obstacle obstacle : obstacles)
                obstacle.draw(canvas);
            if (explosion != null) {
                explosion.draw(canvas);
            }
            for (Coin coin: coins){
                coin.draw(canvas);
            }
            drawCoinScore(canvas);
            drawHerats(canvas);
            drawTxt(canvas);

        }
    }
    public void drawTxt(Canvas canvas){
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        canvas.drawText("Distance "+player.getScore(),player.rightBorder()+5,65,paint); // player score is distance
        canvas.drawText("Score "+bScore,player.rightBorder()+5,heightScreen-65,paint); // player score is distance
        canvas.drawText(""+coin_counter,widthScreen-80+coinImg.getWidth(),30+coinImg.getHeight(),paint);
    }

    public void drawCoinScore(Canvas canvas){
        canvas.drawBitmap(coinImg,widthScreen-130,40,null); // player score is distance
    }
    public void drawHerats(Canvas canvas){

            for(int i=0;i<life_counter;i++){
                canvas.drawBitmap(life,widthScreen/2f-life.getWidth()+i*life.getWidth()/1.5f,30,null); // player score is distance
            }

        }


    public boolean collisionDetection(Position first, Position second) {
        return Rect.intersects(first.getRect(), second.getRect());
    }

    public boolean collisionDetectionObstacle(Position first, Position second) {
        int leftX, rightX, botY, midY, topY;
        double slopeAsc, slopeDsc, freeNumberAsc, freeNumberDsc;
        leftX = first.leftBorder();
        botY = first.bottomBorder();
        rightX = first.rightBorder();
        midY = first.bottomBorder() - first.getHeight() / 2;
        topY = first.topBorder();
        Rect rect = second.getRect();

        if (rect.contains(rightX, midY))
            return true;


        slopeAsc = (double) (midY - botY) / (rightX - leftX); // y=mx+n , n=y-mx
        slopeDsc = -slopeAsc;
        freeNumberAsc = midY - (slopeAsc * rightX);
        freeNumberDsc = midY - (slopeDsc * rightX);


        int lineYasc = (int) (slopeAsc * second.getX() + freeNumberAsc);
        int lineYdsc = (int) (slopeDsc * second.getX() + freeNumberDsc);
        return second.getY() <= lineYasc && second.getY() >= lineYdsc;
    }

    void gameOver() {
        isGameOver = true;
        player.setPlaying(false);
        enemies.clear();
        bullets.clear();
        obstacles.clear();
        gameListenerDialogBox.onGameOver();
        mainThread.setRunning(false);
        //((Activity)context).finish();


    }

//        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage("Alert message to be shown");
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.show();

    private void setBackNumber(){
        if(bScore<100)
            backNumber=0;
        else if(bScore<400) {
            backNumber = 1;
            bullet_speed=21;
        }
        else if(bScore<600) {
            bullet_speed = 23;
            backNumber = 2;
        }
        else if(bScore<1200) {
            backNumber = 3;
            bullet_speed=25;
        }
    }
    private void addEnemies(){
        enemies.add(new Dragon(BitmapFactory.decodeResource(getResources(), R.drawable.rsz_dragon1), widthScreen + random.nextInt(20)+100, (int) (random.nextDouble() * (heightScreen -150 )), player.getScore(), getResources()));
        enemies.add(new Skeleton(BitmapFactory.decodeResource(getResources(), R.drawable.keleton_slashing_002), widthScreen + random.nextInt(20)+200, (int) (random.nextDouble() * (heightScreen -150 )), player.getScore(), getResources()));
        enemies.add(new Groll(BitmapFactory.decodeResource(getResources(), R.drawable.roll0), widthScreen + random.nextInt(20)+300, (int) (random.nextDouble() * (heightScreen -150 )), player.getScore(), getResources()));
        enemies.add(new Missle(BitmapFactory.decodeResource(getResources(), R.drawable.), widthScreen + random.nextInt(20)+400, (int) (random.nextDouble() * (heightScreen -150 )), player.getScore(), getResources(),random.nextInt(2)));
        enemies.add(new Walle(BitmapFactory.decodeResource(getResources(), R.drawable.walle), widthScreen + random.nextInt(20)+400, (int) ((random.nextDouble()+0.8f) * (heightScreen -150 )), player.getScore(), getResources()));

    }

    public void pause() {
        try {
            mainThread.setRunning(false);
            mainThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        mainThread= new MainThread(getHolder(),this);
    }

    public void resumeOnPause(){
        resume();
        surfaceCreated(getHolder());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}


