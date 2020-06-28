package com.example.androidgameproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    static int widthScreen, heightScreen;

    final int NUMBER_OF_BACKGROUNDS=4,MILLION=1000000, BULLET_HEIGHT =9, COIN_HEIGHT =30,DELTA_SCORE=20,POWER_HEIGHT=30,SHIELD_HEIGHT=73,HEART_HEIGHT=90,
            FIRST_WORLD_DISTANCE=2000,SECOND_WORLD_DISTANCE=4000,THIRD_WORLD_DISTANCE=7000,FOURTH_WORLD_DISTANCE=12000;
    private int currentWorldDistance,gameSurfaceCheckPoint=0;
  
    MainThread mainThread;
    private Background[] backgrounds;
    Player player;

    private long bulletStartTime, enemyStartTime, obstacleStartTime,coinStartTime, powerUpStartTime,shieldStartTime,heartStartTime;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private List<Allie> allies;
    private List<Explosion> explosions;
    private Random random = new Random();
    private Explosion explosion;
    private boolean isGameOver=false,isBackgroundChanged=false,isOnce=true;
    int bScore,coin_counter, backgroundNumber,life_counter=3, bulletSpeed =17,coinSoundId,explosionSoundId,powerUpSoundId,shieldSoundId;
    private Bitmap coinImg,life;
    Context context;
    private GameListener gameListenerDialogBox;

    SoundPool coinSound,explosionSound,powerUpSound,shieldSound;
    Vibrator vibrator;

    Typeface typeface;
    private int indexBulletToChoose=0;
    private ImageBitmaps imageBitmaps;
    boolean isPauseDialog;


    public GameSurfaceView(Context context, int width, int height,int checkPoint) {
        super(context);
      
        typeface = ResourcesCompat.getFont(context, R.font.hippopotamus);
        imageBitmaps =new ImageBitmaps(getResources());
        gameListenerDialogBox=((GameListener)context);
        this.context=context;
        widthScreen = width;
        heightScreen = height;
        mainThread = new MainThread(getHolder(), this);
        getHolder().addCallback(this);
        setFocusable(true);
        player = new Player(ImageBitmaps.playerImg, heightScreen,getResources());

        setCheckPoint(checkPoint);

        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        obstacles = new ArrayList<>();
        explosions=new ArrayList<>();
        allies=new ArrayList<>();

        coinImg= ImageBitmaps.coinImg;
        life= ImageBitmaps.heartImg;

        backgrounds=new Background[NUMBER_OF_BACKGROUNDS];
        backgrounds[0] = new Background(ImageBitmaps.backgroundImg1);
        backgrounds[1] = new Background(ImageBitmaps.backgroundImg2);
        backgrounds[2] = new Background(ImageBitmaps.backgroundImg3);
        backgrounds[3] = new Background(ImageBitmaps.backgroundImg4);

        heartStartTime=powerUpStartTime =bulletStartTime=enemyStartTime = obstacleStartTime = System.nanoTime();

        coinSound=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        explosionSound=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        powerUpSound=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        shieldSound=new SoundPool(5, AudioManager.STREAM_MUSIC,0);

        coinSoundId=coinSound.load(context,R.raw.coin,1);
        explosionSoundId=explosionSound.load(context,R.raw.explosion_sound,1);
        powerUpSoundId=powerUpSound.load(context,R.raw.power,1);
        shieldSoundId=shieldSound.load(context,R.raw.shield_sound_effect,1);

        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);


    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainThread.setRunning(true);
        mainThread.start();
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
            setBackNumber();
            backgrounds[backgroundNumber].update();
            player.update();

            long heartTimeElapsed=(System.nanoTime()-heartStartTime)/MILLION;
            if(heartTimeElapsed>15000-player.getDistance()/8){
                if(life_counter==1&&backgroundNumber>0)
                    allies.add(new Heart(ImageBitmaps.heartImg, widthScreen + 10, (int) (random.nextDouble() * (heightScreen - HEART_HEIGHT))));
                heartStartTime=System.nanoTime();
            }

            long shieldTimer = (System.nanoTime() - shieldStartTime) / MILLION; // time in ms that passed since the last time entered the if statement and added last object
            if (shieldTimer > 17000 - player.getDistance() / 8) { // timing the object to show on screen more often when distance is bigger
                if(!player.isHasShield()) //if player with shield wont add another shield
                    allies.add(new Shield(ImageBitmaps.shieldImg,widthScreen + 10, (int) (random.nextDouble() * (heightScreen -SHIELD_HEIGHT ))));
                shieldStartTime = System.nanoTime(); // reset the timer
            }

            long powerUpTimer = (System.nanoTime() - powerUpStartTime) / MILLION;
            if (powerUpTimer > 14000 - player.getDistance() / 8) {
                if(indexBulletToChoose==0) //if player with power up wont add another one
                    allies.add(new PowerUp(ImageBitmaps.powerUpImg,widthScreen + 10, (int) (random.nextDouble() * (heightScreen -POWER_HEIGHT ))));
                powerUpStartTime = System.nanoTime();
            }

            long coinTimeElapsed=(System.nanoTime()-coinStartTime)/MILLION;
            if(coinTimeElapsed>9000-player.getDistance()/4){
                allies.add(new Coin(ImageBitmaps.coinImg, widthScreen + 10, (int) (random.nextDouble() * (heightScreen - COIN_HEIGHT))));
                coinStartTime=System.nanoTime();
            }

            for(int i=0;i<allies.size();i++){
                allies.get(i).update();
                if(collisionDetectionPlayer(player,allies.get(i))){
                    if(allies.get(i)instanceof Shield) {
                        shieldSound.play(shieldSoundId, 1, 1, 0, 0, 1);
                        player.setHasShield(true);
                    }
                    else if(allies.get(i)instanceof PowerUp){
                        powerUpSound.play(powerUpSoundId,1,1,0,0,1);
                        indexBulletToChoose=random.nextInt(2)+1;
                    }
                    else if(allies.get(i)instanceof Coin){
                        coinSound.play(coinSoundId,1,1,0,0,1);
                        bScore+=DELTA_SCORE;
                        coin_counter++;
                    }
                    else if(allies.get(i)instanceof Heart){
                        if(life_counter<3)
                            life_counter++;
                    }
                    allies.remove(i);
                    break;
                }

            }


            long bulletTimer = (System.nanoTime() - bulletStartTime) / MILLION;
            int maxBulletDistance=player.getDistance();
            if(maxBulletDistance>4000)
                maxBulletDistance=4000;
            if (bulletTimer > 1500 - maxBulletDistance / 4) {
                bullets.add(new Bullet(ImageBitmaps.bulletImg, player.getX() + player.getWidth(), player.getY() + player.getHeight() / 2 - BULLET_HEIGHT, bulletSpeed,indexBulletToChoose));
                bulletStartTime = System.nanoTime();
            }

            for (int i=0;i<bullets.size();i++) {
                bullets.get(i).update();


                if (bullets.get(i).leftBorder() > widthScreen + 200)
                {
                    bullets.remove(i);
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

                    if(player.isHasShield()){
                        player.setHasShield(false);
                        explosions.add(new Explosion(ImageBitmaps.explosionImg, enemies.get(i).getX(), enemies.get(i).getY(), enemies.get(i).getWidth(), enemies.get(i).getHeight()));
                        explosionSound.play(explosionSoundId,1,1,0,0,1);
                        enemies.remove(i);
                    }
                    else {
                        int explosionY, explosionX;
                        if (enemies.get(i).leftBorder() < player.leftBorder()) {
                            explosionX = player.leftBorder() + 10;
                        } else {
                            explosionX = enemies.get(i).getX();
                        }
                        explosionY = getExplosionY(enemies.get(i), explosionX);
                        int width, height;
                        width = enemies.get(i).getWidth() * 2 / 3;
                        height = enemies.get(i).getHeight() * 2 / 3;

                        explosions.add(new Explosion(ImageBitmaps.explosionImg, explosionX - width / 2, explosionY - height / 2, width, height));
                        vibrate();
                        enemies.remove(i);
                        life_counter--;
                        indexBulletToChoose = 0;
                        if (life_counter == 0) {

                            explosions.add(new Explosion(ImageBitmaps.explosionImg, player.getX() + player.getWidth() / 2 - 1000 / 2, player.getY() + player.getHeight() / 2 - 1000 / 2, 1000, 1000));
                            gameOver();
                        }
                    }
                    break;
                }

                if (enemies.get(i).rightBorder() < 0) {
                    enemies.remove(i);
                    break;
                }
                for (int j=0 ;j<bullets.size();j++) {
                    if (collisionDetection(enemies.get(i), bullets.get(j))) {
                        explosions.add(new Explosion(ImageBitmaps.explosionImg, enemies.get(i).getX(), enemies.get(i).getY(),enemies.get(i).getWidth(),enemies.get(i).getHeight()));
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
                    obstacles.add(new Obstacle(ImageBitmaps.obstacleImg, widthScreen + 10, heightScreen / 2 + 150));
                }
                else {
                    obstacles.add(new Obstacle(ImageBitmaps.obstacleImg, widthScreen + 10, -350));
                }

                obstacleStartTime = System.nanoTime();
            }
            for (int i=0;i<obstacles.size();i++) {
                obstacles.get(i).update();
                if (collisionDetectionPlayer(player, obstacles.get(i))) {
                    if(player.isHasShield()){
                        player.setHasShield(false);
                    }
                    else {
                        vibrate();
                        life_counter = 0;
                        explosions.add(new Explosion(ImageBitmaps.explosionImg, player.getX() + player.getWidth() / 2 - 1000 / 2, player.getY() + player.getHeight() / 2 - 1000 / 2, 1000, 1000));
                        gameOver();
                        break;
                    }
                }

                for(int j=0;j<bullets.size();j++){
                    if(collisionDetection(obstacles.get(i),bullets.get(j))){
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
            for(int i=0;i<explosions.size();i++)
            {
                explosions.get(i).draw(canvas);
                if(explosions.get(i).removeExplosion()){ // if explosion animation end remove explosion from list
                    explosions.remove(i);
                    break; }

            }
            for(Allie allie:allies)
                allie.draw(canvas);

            drawCoinScore(canvas);
            drawHeats(canvas);
            drawTxt(canvas);
            if(isBackgroundChanged) {
                drawLevel(canvas);
            }

        }

    }
    public void drawTxt(Canvas canvas){
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);

        if(context.getString(R.string.language).equals("hebrew")){
           paint.setTypeface(Typeface.DEFAULT_BOLD);
        }
        else
            paint.setTypeface(typeface);

        canvas.drawText(context.getString(R.string.Distance)+" "+player.getDistance(),player.rightBorder()+5,85,paint);
        canvas.drawText(context.getString(R.string.Score)+" "+bScore,player.rightBorder()+5, heightScreen -85,paint);
        canvas.drawText(""+coin_counter,widthScreen-80+coinImg.getWidth(),30+coinImg.getHeight(),paint);

    }
    public void drawLevel(Canvas canvas){
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        paint.setTypeface(typeface);
        if (player.getDistance()%1000<100) {
            canvas.drawText(context.getString(R.string.level)+" "+(++backgroundNumber),widthScreen/2f-life.getWidth()/1.5f,life.getHeight()+100,paint);
        }else{
            isBackgroundChanged=false;
        }
    }
    // draw coins
    public void drawCoinScore(Canvas canvas){
        canvas.drawBitmap(coinImg,widthScreen-130,35,null);
    }

    // draw hearts top of the screen
    public void drawHeats(Canvas canvas){

        for(int i=0;i<life_counter;i++){
            canvas.drawBitmap(life,widthScreen/2f-life.getWidth()+2*i*life.getWidth()/1.5f,30,null);
        }

    }

    // collision detection between bullet and other object
    public boolean collisionDetection(Position first, Position second) {
        return Rect.intersects(first.getRect(), second.getRect());
    }
    private boolean collisionWithoutShield(Player first, Position second){
        int leftX, rightX, botY, midY, topY;
        leftX = first.leftBorder()+20;
        botY = first.bottomBorder()-20;
        rightX = first.rightBorder()-20;
        midY = first.bottomBorder() - first.getHeight() / 2;
        topY = first.topBorder()+20;

        // three triangle lines of the spaceship
        Line[] triangleLines=new Line[3];
        triangleLines[0]= new Line(new Point(leftX,topY),new Point(leftX,botY));
        triangleLines[1]=new Line(new Point(leftX,botY),new Point(rightX,midY));
        triangleLines[2]=new Line(new Point(rightX,midY),new Point(leftX,topY));


        Line[] rectangleLines=new Line[4];
        int topBorder=second.topBorder();
        int leftBorder=second.leftBorder();
        int bottomBorder=second.bottomBorder();
        int rightBorder=second.rightBorder();
        if(second instanceof Obstacle){
            topBorder+=15;
            leftBorder+=15;
            rightBorder-=15;
            bottomBorder-=15;
        }
        else if(second instanceof Missile){
            leftBorder+=20;
            rightBorder-=20;
            topBorder+=30;
            bottomBorder-=30;

        }

        // four rectangle lines of the enemy
        rectangleLines[0]=new Line(new Point(leftBorder,topBorder),new Point(rightBorder,topBorder));
        rectangleLines[1]=new Line(new Point(rightBorder,topBorder),new Point(rightBorder,bottomBorder));
        rectangleLines[2]=new Line(new Point(rightBorder,second.bottomBorder()),new Point(leftBorder,bottomBorder));
        rectangleLines[3]=new Line(new Point(leftBorder,bottomBorder),new Point(leftBorder,topBorder));

        //check if there are 2 lines that touching
        for(Line triangleLine:triangleLines){
            for(Line rectangleLine:rectangleLines){
                if(linesTouching(rectangleLine,triangleLine)) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean collisionWithShield(Player first,Position second){
        if (second instanceof Obstacle) { // checking collision between circle and rectangle
            int middlePlayerX = first.x + first.width / 2 - 20;
            int middlePlayerY = first.y + first.height / 2;
            float testX = middlePlayerX;
            float testY = middlePlayerY;
            // which edge is closest?
            if (middlePlayerX < second.leftBorder())
                testX = second.leftBorder();      // test left edge
            else if (middlePlayerX > second.rightBorder())
                testX = second.rightBorder();   // right edge
            if (middlePlayerY < second.topBorder()) testY = second.topBorder();      // top edge
            else if (middlePlayerY > second.bottomBorder())
                testY = second.bottomBorder();   // bottom edge
            // get distance from closest edges
            float distX = middlePlayerX - testX;
            float distY = middlePlayerY - testY;
            float distance = (float) Math.sqrt((distX * distX) + (distY * distY));
            if (distance <= first.getShieldRadius()) { // if the distance is less than the radius, collision!
                return true;
            }
            return false;
        }
        else { // checking collision between two circles
            int enemyRadius = second.width / 2;
            int middleEnemyX = second.getX() + enemyRadius, middleEnemyY = second.getY() + enemyRadius;
            int middlePlayerX = first.x + first.width / 2 - 20, middlePlayerY = first.y + first.height / 2;
            int distanceBetweenMidPoints = (int) Math.sqrt((middleEnemyX - middlePlayerX) *
                    (middleEnemyX - middlePlayerX) + (middleEnemyY - middlePlayerY) * (middleEnemyY - middlePlayerY));
            if (distanceBetweenMidPoints <= enemyRadius + first.getShieldRadius()) // if distance between circles is less than
                return true;                                                       // sum of radius collision!
            return false;
        }
    }

    private boolean collisionDetectionPlayer(Player first, Position second) {
        boolean hasCollided=false;
        if(first.isHasShield()) {
            hasCollided=collisionWithShield(first,second);
        }
        else
            hasCollided=collisionWithoutShield(first,second);
        return hasCollided;
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
        allies.clear();
        gameListenerDialogBox.onGameOver(); // call listener of game over for game over dialog
        mainThread.setRunning(false);
    }


    public void setCheckPoint(int checkPoint) {

        this.gameSurfaceCheckPoint=checkPoint;
        backgroundNumber=this.gameSurfaceCheckPoint;
        if(backgroundNumber==0)
            player.setDistance(0);
        else if(backgroundNumber==1){
            isOnce=false;
            player.setDistance(FIRST_WORLD_DISTANCE);
        }
        else if(backgroundNumber==2){
            isOnce=true;
            player.setDistance(SECOND_WORLD_DISTANCE);
        }else if(backgroundNumber==3){
            isOnce=false;
            player.setDistance(THIRD_WORLD_DISTANCE);
        }
    }

    public int getCheckPoint() {
        return gameSurfaceCheckPoint;
    }

    private void setBackNumber(){



        if(getCheckPoint()==0) {
            if (player.getDistance() < FIRST_WORLD_DISTANCE) {
                currentWorldDistance = FIRST_WORLD_DISTANCE;
                backgroundNumber = 0;
                if (isOnce) {
                    isBackgroundChanged = true;
                    isOnce = false;
                }
            }else{
                this.gameSurfaceCheckPoint=1;
            }
        }
        else if(getCheckPoint()==1) {
            if (player.getDistance() < SECOND_WORLD_DISTANCE) {
                this.gameSurfaceCheckPoint = 1;
                currentWorldDistance = SECOND_WORLD_DISTANCE;
                backgroundNumber = 1;
                bulletSpeed = 21;
                if (!isOnce) {
                    isBackgroundChanged = true;
                    isOnce = true;
                }
            }
            else{
                this.gameSurfaceCheckPoint=2;
            }

        }
        else if(getCheckPoint()==2) {
            if (player.getDistance() < THIRD_WORLD_DISTANCE) {
                this.gameSurfaceCheckPoint = 2;
                currentWorldDistance = THIRD_WORLD_DISTANCE;
                bulletSpeed = 23;
                backgroundNumber = 2;
                if (isOnce) {
                    isOnce = false;
                    isBackgroundChanged = true;
                }
            }
            else{
                this.gameSurfaceCheckPoint=3;
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
    // add enemies for each level
    private void addEnemies(){
        if(backgroundNumber ==0) {
            enemies.add(new Dragon(ImageBitmaps.dragonImg1, widthScreen + random.nextInt(20) + 100, (int) (random.nextDouble() * (heightScreen - 150)), player.getDistance(), getResources(),200));
            enemies.add(new Skeleton(ImageBitmaps.skel1, widthScreen + random.nextInt(20) + 200, (int) (random.nextDouble() * (heightScreen - 150)), player.getDistance(), getResources(),120));
            enemies.add(new Groll(ImageBitmaps.roll1, widthScreen + random.nextInt(20) + 300, (int) (random.nextDouble() * (heightScreen - 150)), player.getDistance(), getResources(),150));

        }else if(backgroundNumber ==1) {
            enemies.add(new Missile(ImageBitmaps.missile,widthScreen + random.nextInt(20) + 400, (int) (random.nextDouble() * (heightScreen - 150)),player.getX() ,player.getY()));
            enemies.add(new Walle(ImageBitmaps.walleImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 150)), player.getDistance(), 150));
            enemies.add(new SpaceShip(ImageBitmaps.yellowSpaceshipImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 150)), player.getDistance(),  0));
        }else if(backgroundNumber ==2) {
            enemies.add(new UfoGreen(ImageBitmaps.ufoGreenImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 182)), player.getDistance(), 0));
            enemies.add(new UfoYellow(ImageBitmaps.ufoYellowImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 182)), player.getDistance(), 0));
            enemies.add(new UfoRed(ImageBitmaps.ufoRedImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 182)), player.getDistance(), 0));
        }else{
            enemies.add(new Dragon(ImageBitmaps.dragonImg1, widthScreen + random.nextInt(20) + 100, (int) (random.nextDouble() * (heightScreen - 150)), player.getDistance(), getResources(),200));
            enemies.add(new Walle(ImageBitmaps.walleImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 150)), player.getDistance(), 150));
            enemies.add(new UfoRed(ImageBitmaps.ufoRedImg, widthScreen + random.nextInt(20) + 400, (int) ((random.nextDouble()) * (heightScreen - 182)), player.getDistance(), 0));
            enemies.add(new Skeleton(ImageBitmaps.skel1, widthScreen + random.nextInt(20) + 200, (int) (random.nextDouble() * (heightScreen - 150)), player.getDistance(), getResources(),120));

        }
    }

    public void pause() {
        try {

            if(mainThread.getRunning()) {
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

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private void vibrate(){
        //vibrate screen when collision occurs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }
    // get the y coordinate of the collision player with enemy
    private int getExplosionY(Enemy enemy,int x){
        int spaceShipMidY=player.getY()+player.getHeight()/2;
        double slope,freeNumber;
        int explosionY;

        if(enemy.bottomBorder()<spaceShipMidY){
            slope = (double) (player.getY() - spaceShipMidY) / (player.getX()+10 - player.rightBorder());
            freeNumber = player.getY() - slope * player.getX()+10;
            explosionY = (int) (slope * x + freeNumber);
        }
        else if(enemy.topBorder()>spaceShipMidY){
            slope=(double)(player.bottomBorder()-spaceShipMidY)/(player.getX()+10-player.rightBorder());
            freeNumber=player.bottomBorder()-slope*player.getX()+10;
            explosionY= (int) (slope*x+freeNumber);
        }
        else {
            explosionY=spaceShipMidY;
        }
        return explosionY;
    }
}


