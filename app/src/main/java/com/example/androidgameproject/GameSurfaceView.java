package com.example.androidgameproject;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    MainThread mainThread;
    GameActivity gameActivity;

    public static final int SPEED = -5; //change
    private Background[] backgrounds;
    Player player;
    private List<Bullet> bullets;
    private long bulletStartTime, enemyStartTime, obstacleStartTime,coinStartTime,backgroundLevelStartTime;
    static int widthScreen, heightScreen;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private List<Coin> coins;
    Random random = new Random();
    private Explosion explosion;
    private boolean isGameOver=false;
    int bScore,coin_counter,backNumber,life_counter=3,bullet_speed=17;
    private Bitmap coinImg,life,pauseBtn;
    Context context;
    private GameListener gameListenerDialogBox;
    MediaPlayer mediaPlayerGame;
    SoundPool coinSound;
    int coinSoundId,prevNum;
    Vibrator vibrator;


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
        mediaPlayerGame=MediaPlayer.create(context,R.raw.game);
        coinSound=new SoundPool(99, AudioManager.STREAM_MUSIC,0);
        coinSoundId=coinSound.load(context,R.raw.coin,1);
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds







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
            mediaPlayerGame.start();

            setBackNumber();
            backgrounds[backNumber].update();
            player.update();

            long bulletTimer = (System.nanoTime() - bulletStartTime) / 1000000;
            if (bulletTimer > 1500 - player.getScore() / 3) { //change
                bullets.add(new Bullet(BitmapFactory.decodeResource(getResources(), R.drawable.bullet), player.getX() + player.getWidth(), player.getY() + player.getHeight() / 2 - 9,bullet_speed));
                bulletStartTime = System.nanoTime();
            }
            for (int i=0;i<bullets.size();i++) {
                bullets.get(i).update();


                if (bullets.get(i).leftBorder() > widthScreen + 200) //change
                {
                    bullets.remove(i);
                    break;
                }

            }
            long coinTimeElapsed=(System.nanoTime()-coinStartTime)/1000000;
            if(coinTimeElapsed>5500-player.getScore()/2){
                coins.add(new Coin(BitmapFactory.decodeResource(getResources(), R.drawable.coin), widthScreen + 10, (int) (random.nextDouble() * (heightScreen -20 ))));
                coinStartTime=System.nanoTime();
            }
            for(int i=0;i<coins.size();i++){
                coins.get(i).update();
                if (collisionDetection(player, coins.get(i))) {
                    coinSound.play(coinSoundId,5,5,1,0,1);
                    coins.remove(i);
                    bScore+=20;
                    coin_counter++;
                    break;
                }
            }



            long enemyTimer = (System.nanoTime() - enemyStartTime) / 1000000;
            if (enemyTimer > 3000 - player.getScore() / 3) {
                addEnemies();
                enemyStartTime = System.nanoTime();
            }

            for (int i=0 ;i<enemies.size();i++) {
                enemies.get(i).update();

                if (collisionDetection(player, enemies.get(i))) {
                    explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.exp2_0), enemies.get(i).getX(), enemies.get(i).getY(), getResources());
                    vibrate();
                    enemies.remove(i);
                    life_counter--;
                    if(life_counter==0){
                        gameOver();
                    }
                    break;
                }

                if (enemies.get(i).rightBorder() < 0) {
                    enemies.remove(i);
                    break;
                }
                for (int j=0 ;j<bullets.size();j++) {
                    if (collisionDetection(enemies.get(i), bullets.get(j))) {

                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.exp2_0), enemies.get(i).getX(), enemies.get(i).getY(), getResources());
                        enemies.remove(i);
                        bullets.remove(j);
                        bScore+=20;
                        break;
                    }
                }

            }

            long obstacleTimer = (System.nanoTime() - obstacleStartTime) / 1000000;
            if (obstacleTimer > 10000 - player.getScore() / 4) {
                if(random.nextInt(2)==1) {
                    obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(), R.drawable.new_pillar), widthScreen + 10, heightScreen / 2 + 150));
                }
                else {
                    obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(), R.drawable.new_pillar), widthScreen + 10, -350));
                }

                obstacleStartTime = System.nanoTime();
            }
            for (int i=0;i<obstacles.size();i++) {
                obstacles.get(i).update();
                if (collisionDetection(player, obstacles.get(i))) {
                    vibrate();
                    life_counter=0;
                    gameOver();
                }

                for(int j=0;j<bullets.size();j++){
                    if(collisionDetection(obstacles.get(i),bullets.get(j))){
                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.exp2_0), bullets.get(j).getX(), bullets.get(j).getY(), getResources());
                        bullets.remove(j);
                        break;
                    }
                }
                if (obstacles.get(i).rightBorder() < 0) {
                    obstacles.remove(i);
                    break;
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
            if(prevNum<backNumber) {
                prevNum=backNumber;
                drawLevel(canvas);
            }

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
    public void drawLevel(Canvas canvas){
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(90);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        long levelTimer = (System.nanoTime() - backgroundLevelStartTime) / 1000000;
        if(levelTimer<3000){
            paint.setTextSize(90);
            canvas.drawText("Level "+backNumber,widthScreen/2f,50,paint);
            backgroundLevelStartTime=System.nanoTime();
        }
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


    }



    private void setBackNumber(){
        if(bScore<100) {
            backNumber = 0;

        }
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
        if(backNumber==0) {
            enemies.add(new Dragon(BitmapFactory.decodeResource(getResources(), R.drawable.rsz_dragon1), widthScreen + random.nextInt(20) + 100, (int) (random.nextDouble() * (heightScreen - 150)), player.getScore(), getResources()));
            enemies.add(new Skeleton(BitmapFactory.decodeResource(getResources(), R.drawable.keleton_slashing_002), widthScreen + random.nextInt(20) + 200, (int) (random.nextDouble() * (heightScreen - 150)), player.getScore(), getResources()));
            enemies.add(new Groll(BitmapFactory.decodeResource(getResources(), R.drawable.roll0), widthScreen + random.nextInt(20) + 300, (int) (random.nextDouble() * (heightScreen - 150)), player.getScore(), getResources()));
        }else if(backNumber==1) {
            enemies.add(new Missle(BitmapFactory.decodeResource(getResources(), R.drawable.rsz_rotatemissile), widthScreen + random.nextInt(20) + 400, (int) (random.nextDouble() * (heightScreen - 150)), player.getScore(), getResources(), random.nextInt(2)));
            enemies.add(new Walle(BitmapFactory.decodeResource(getResources(), R.drawable.walle), widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 150)), player.getScore(), getResources()));
            enemies.add(new BlueCroco(BitmapFactory.decodeResource(getResources(), R.drawable.bluecroco), widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 150)), player.getScore(), getResources(), 0));
        }else if(backNumber>=2) {
        //    enemies.add(new SmallBlueDragon(BitmapFactory.decodeResource(getResources(), R.drawable.smallbluedragon), widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 150)), player.getScore(), getResources(), 0));
            enemies.add(new YellowMissile(BitmapFactory.decodeResource(getResources(), R.drawable.yellowspace), widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 150)), player.getScore(), getResources(),0));


        }

    }

    public void pause() {
        try {
            mediaPlayerGame.pause();
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

    private void vibrate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }
}


