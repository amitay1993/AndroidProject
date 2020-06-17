package com.example.androidgameproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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

    static int widthScreen, heightScreen;
    final int NUMBER_OF_BACKGROUNDS=4,MILLION=1000000, BULLET_HEIGHT =9, COIN_HEIGHT =20,DELTA_SCORE=20,POWER_HEIGHT=30,
              FIRST_WORLD_DISTANCE=2000,SECOND_WORLD_DISTANCE=4000,THIRD_WORLD_DISTANCE=7000,FOURTH_WORLD_DISTANCE=10000;
    private int currentWorldDistance;
    MainThread mainThread;
    private Background[] backgrounds;
    Player player;

    private long bulletStartTime, enemyStartTime, obstacleStartTime,coinStartTime, powerUpStartTime;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private List<Coin> coins;
    private List<PowerUp> powers;
    private Random random = new Random();
    private Explosion explosion;
    private boolean isGameOver=false,isBackgroundChanged=false,isOnce=true;
    int bScore,coin_counter, backgroundNumber,life_counter=3, bulletSpeed =17,coinSoundId,explosionSoundId;
    private Bitmap coinImg,life;
    Context context;
    private GameListener gameListenerDialogBox;
    MediaPlayer mediaPlayerGame;
    SoundPool coinSound,explosionSound;
    Vibrator vibrator;
    private int indexBulletToChoose=0;
    private ConstValues constValuesClass;
   static BitmapFactory.Options options;
   boolean isPauseDialog;


    public GameSurfaceView(Context context, int width, int height) {
        super(context);
        constValuesClass=new ConstValues(getResources());
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
        powers =new ArrayList<>();

        coinImg=BitmapFactory.decodeResource(getResources(),R.drawable.coin);
        life=BitmapFactory.decodeResource(getResources(),R.drawable.heart);
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player));

        backgrounds=new Background[NUMBER_OF_BACKGROUNDS];
        backgrounds[0] = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background1));
        backgrounds[1] = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background2));
        backgrounds[2] = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background3));
        backgrounds[3] = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background4));

        powerUpStartTime =bulletStartTime = enemyStartTime = obstacleStartTime = System.nanoTime();

        mediaPlayerGame=MediaPlayer.create(context,R.raw.game);
        coinSound=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        explosionSound=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        coinSoundId=coinSound.load(context,R.raw.coin,1);
        explosionSoundId=explosionSound.load(context,R.raw.explosion_sound,1);
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        options = new BitmapFactory.Options();
        options.inScaled = false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("pause","created");
        mainThread.setRunning(true);
        mainThread.start();
        mediaPlayerGame.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

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
            backgrounds[backgroundNumber].update();
            player.update();


            long powerUpTimer = (System.nanoTime() - powerUpStartTime) / MILLION;
            if (powerUpTimer > 6000 - player.getDistance() / 8) { //change 14000
                powers.add(new PowerUp(BitmapFactory.decodeResource(getResources(), R.drawable.power_up),widthScreen + 10, (int) (random.nextDouble() * (heightScreen -POWER_HEIGHT ))));
                powerUpStartTime = System.nanoTime();
            }
            for(int i = 0; i< powers.size(); i++){
                powers.get(i).update();
                if (collisionDetectionPlayer(player, powers.get(i))) {
                    coinSound.play(coinSoundId,1,1,0,0,1);
                    powers.remove(i);
                    indexBulletToChoose=random.nextInt(2)+1;
                    break;
                }
            }


            long bulletTimer = (System.nanoTime() - bulletStartTime) / MILLION;
            if (bulletTimer > 1500 - player.getDistance() / 4) { //change
                bullets.add(new Bullet(BitmapFactory.decodeResource(getResources(), R.drawable.bullet), player.getX() + player.getWidth(), player.getY() + player.getHeight() / 2 - BULLET_HEIGHT, bulletSpeed,getResources(),indexBulletToChoose));
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
            long coinTimeElapsed=(System.nanoTime()-coinStartTime)/MILLION;
            if(coinTimeElapsed>5000-player.getDistance()/4){
                coins.add(new Coin(ConstValues.coinImg, widthScreen + 10, (int) (random.nextDouble() * (heightScreen - COIN_HEIGHT))));
                coinStartTime=System.nanoTime();
            }
            for(int i=0;i<coins.size();i++){
                coins.get(i).update();
                if (collisionDetectionPlayer(player, coins.get(i))) {
                    coinSound.play(coinSoundId,1,1,0,0,1);
                    coins.remove(i);
                    bScore+=DELTA_SCORE;
                    coin_counter++;
                    break;
                }
            }


            long enemyTimer = (System.nanoTime() - enemyStartTime) / MILLION;
            if (enemyTimer > 3000 - player.getDistance() / 4) {
                addEnemies();
                enemyStartTime = System.nanoTime();
            }

            for (int i=0 ;i<enemies.size();i++) {
                enemies.get(i).update();

                if (collisionDetectionPlayer(player, enemies.get(i))) {
                    int enemyY= enemies.get(i).getY();
                    if(enemies.get(i).topBorder()<player.topBorder())
                        enemyY=enemies.get(i).bottomBorder();
                    explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion_new,options), enemies.get(i).getX(), enemyY);
                    vibrate();
                    enemies.remove(i);
                    life_counter--;
                    indexBulletToChoose=0;
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


                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion_new,options), enemies.get(i).getX(), enemies.get(i).getY());
                        explosionSound.play(explosionSoundId,1,1,0,0,1);
                        enemies.remove(i);
                        bullets.remove(j);
                        bScore+=DELTA_SCORE;
                        break;
                    }
                }

            }

            long obstacleTimer = (System.nanoTime() - obstacleStartTime) / MILLION;
            if (obstacleTimer > 8000 - player.getDistance() / 6) {
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
                if (collisionDetectionPlayer(player, obstacles.get(i))) {
                    vibrate();
                    life_counter=0;
                    gameOver();
                }

                for(int j=0;j<bullets.size();j++){
                    if(collisionDetection(obstacles.get(i),bullets.get(j))){
                     //   explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.exp2_0), bullets.get(j).getX(), bullets.get(j).getY());
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
            backgrounds[backgroundNumber].draw(canvas);
            player.draw(canvas);
            canvas.restoreToCount(saveState);
            for (Bullet bullet : bullets)
                bullet.draw(canvas);
            for (Enemy enemy : enemies) {
                enemy.draw(canvas);
            }
            for (Obstacle obstacle : obstacles) {
                obstacle.draw(canvas);
            }
            if (explosion != null) {
                explosion.draw(canvas);
            }
            for (Coin coin: coins){
                coin.draw(canvas);
            }
            for(PowerUp suprise: powers)
                suprise.draw(canvas);

            drawCoinScore(canvas);
            drawHerats(canvas);
            drawTxt(canvas);
            if(isBackgroundChanged) {
                drawLevel(canvas);
            }

        }

    }
    public void drawTxt(Canvas canvas){
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        canvas.drawText("Distance "+player.getDistance(),player.rightBorder()+5,65,paint); // player score is distance
        canvas.drawText("Score "+bScore,player.rightBorder()+5,heightScreen-65,paint); // player score is distance
        canvas.drawText(""+coin_counter,widthScreen-80+coinImg.getWidth(),30+coinImg.getHeight(),paint);

    }
    public void drawLevel(Canvas canvas){
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(75);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        if (player.getDistance()%currentWorldDistance<100) {
            canvas.drawText("Level "+(++backgroundNumber),widthScreen/2f-2*life.getWidth()/2f,life.getHeight()+100,paint);
        }else{
            isBackgroundChanged=false;

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

    public boolean collisionDetectionPlayer(Player first, Position second) {

        int leftX, rightX, botY, midY, topY;
        leftX = first.leftBorder()+20;
        botY = first.bottomBorder()-20;
        rightX = first.rightBorder()-20;
        midY = first.bottomBorder() - first.getHeight() / 2;
        topY = first.topBorder()+20;

        Line[] triangleLines=new Line[3];
        triangleLines[0]= new Line(new Point(leftX,topY),new Point(leftX,botY));
        triangleLines[1]=new Line(new Point(leftX,botY),new Point(rightX,midY));
        triangleLines[2]=new Line(new Point(rightX,midY),new Point(leftX,topY));
        Line[] rectangleLines=new Line[4];

        int topBorder=second.topBorder();
        int leftBorder=second.leftBorder();
        int bottomBorder=second.bottomBorder();
        int rightBorder=second.rightBorder();

        rectangleLines[0]=new Line(new Point(leftBorder,topBorder),new Point(rightBorder,topBorder));
        rectangleLines[1]=new Line(new Point(rightBorder,topBorder),new Point(rightBorder,bottomBorder));
        rectangleLines[2]=new Line(new Point(rightBorder,second.bottomBorder()),new Point(leftBorder,bottomBorder));
        rectangleLines[3]=new Line(new Point(leftBorder,bottomBorder),new Point(leftBorder,topBorder));

        for(Line triangleLine:triangleLines){
            for(Line rectangleLine:rectangleLines){
                if(linesTouching(rectangleLine,triangleLine)) {
                    return true;
                }
            }
        }
        return false;
    }

    // check if two lines are touching
    boolean linesTouching(Line l1,Line l2) {

        float denom=((l2.end.y-l2.start.y)*(l1.end.x-l1.start.x) - (l2.end.x-l2.start.x)*(l1.end.y-l1.start.y));
        float uA = ((l2.end.x-l2.start.x)*(l1.start.y-l2.start.y) - (l2.end.y-l2.start.y)*(l1.start.x-l2.start.x)) / denom;
        float uB = ((l1.end.x-l1.start.x)*(l1.start.y-l2.start.y) - (l1.end.y-l1.start.y)*(l1.start.x-l2.start.x)) / denom;
        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
            return true;
        }
        return false;
    }

    void gameOver() {
        isGameOver = true;
        player.setPlaying(false);
        enemies.clear();
        bullets.clear();
        obstacles.clear();
    //    player=null;
        gameListenerDialogBox.onGameOver();
        mainThread.setRunning(false);
    }

    private void setBackNumber(){
        currentWorldDistance=FIRST_WORLD_DISTANCE;
        if(player.getDistance()<FIRST_WORLD_DISTANCE) {
            backgroundNumber = 0;
            if(isOnce){
                isBackgroundChanged=true;
                isOnce=false;
            }
        }
        else if(player.getDistance()<SECOND_WORLD_DISTANCE) {
            currentWorldDistance=SECOND_WORLD_DISTANCE;
            backgroundNumber = 1;
            bulletSpeed =21;
            if(!isOnce){
                isBackgroundChanged=true;
                isOnce=true;
            }
        }
        else if(player.getDistance()<THIRD_WORLD_DISTANCE) {
            currentWorldDistance=THIRD_WORLD_DISTANCE;
            bulletSpeed = 23;
            backgroundNumber = 2;
            if(isOnce){
                isOnce=false;
                isBackgroundChanged=true;
            }
        }
        else if(bScore<FOURTH_WORLD_DISTANCE) {
            currentWorldDistance=FOURTH_WORLD_DISTANCE;
            backgroundNumber = 3;
            bulletSpeed =25;
            if(!isOnce){
                isOnce=true;
                isBackgroundChanged=true;
            }
        }
    }
    private void addEnemies(){
        if(backgroundNumber ==0) {
            enemies.add(new Dragon(BitmapFactory.decodeResource(getResources(), R.drawable.rsz_dragon1), widthScreen + random.nextInt(20) + 100, (int) (random.nextDouble() * (heightScreen - 150)), player.getDistance(), getResources(),200));
            enemies.add(new Skeleton(ConstValues.skel1, widthScreen + random.nextInt(20) + 200, (int) (random.nextDouble() * (heightScreen - 150)), player.getDistance(), getResources(),120));
            enemies.add(new Groll(ConstValues.roll1, widthScreen + random.nextInt(20) + 300, (int) (random.nextDouble() * (heightScreen - 150)), player.getDistance(), getResources(),150));

        }else if(backgroundNumber ==1) {
            enemies.add(new Missle(ConstValues.missileUpImg, widthScreen + random.nextInt(20) + 400, (int) (random.nextDouble() * (heightScreen - 150)), player.getDistance(), getResources(), random.nextInt(2)));
            enemies.add(new Walle(ConstValues.walleImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 150)), player.getDistance(), 150));
            enemies.add(new SpaceShip(ConstValues.yellowSpaceshipImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 150)), player.getDistance(),  0));
        }else if(backgroundNumber ==2) {
            enemies.add(new UfoGreen(ConstValues.ufoGreemImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 182)), player.getDistance(), 0));
            enemies.add(new UfoYellow(ConstValues.ufoYellowImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 182)), player.getDistance(), 0));
            enemies.add(new UfoRed(ConstValues.ufoRedImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 182)), player.getDistance(), 0));
        }else{
            enemies.add(new Dragon(BitmapFactory.decodeResource(getResources(), R.drawable.rsz_dragon1), widthScreen + random.nextInt(20) + 100, (int) (random.nextDouble() * (heightScreen - 150)), player.getDistance(), getResources(),200));
            enemies.add(new Walle(ConstValues.walleImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 150)), player.getDistance(), 150));
            enemies.add(new UfoRed(ConstValues.ufoGreemImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 182)), player.getDistance(), 0));
            enemies.add(new Skeleton(ConstValues.skel0, widthScreen + random.nextInt(20) + 200, (int) (random.nextDouble() * (heightScreen - 150)), player.getDistance(), getResources(),120));

        }
    }

    public void pause() {
        try {
            if(mainThread.getRunning()) {
                Log.d("pause", "pause");
                mediaPlayerGame.pause();
                mainThread.setRunning(false);
                mainThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
         if(!isPauseDialog)
            mainThread= new MainThread(getHolder(),this);
    }

    public void resumeOnPause(){
        resume();
        surfaceCreated(getHolder());
        //mediaPlayerGame.start();
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


