package com.example.androidgameproject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread mainThread;
    public static final  int SPEED=-5; //change
    private Background background;
    private Player player;
    private List<Bullet> bullets;
    private long bulletStartTime,enemyStartTime,obstacleStartTime;
    static int widthScreen, heightScreen;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    Random random=new Random();
    private Explosion explosion;



    public GameSurfaceView(Context context,int width,int height) {
        super(context);
        widthScreen =width;
        heightScreen =height;
        mainThread=new MainThread(getHolder(),this);
        getHolder().addCallback(this);
        setFocusable(true);
        bullets=new ArrayList<>();
        enemies =new ArrayList<>();
        obstacles=new ArrayList<>();


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainThread.setRunning(true);
        mainThread.start();
        background=new Background(BitmapFactory.decodeResource(getResources(),R.drawable.background1));
        player=new Player(BitmapFactory.decodeResource(getResources(),R.drawable.player));
        bulletStartTime=enemyStartTime=obstacleStartTime=System.nanoTime();




    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry=true;
        while (retry){
            try{
                mainThread.setRunning(false);
                mainThread.join();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action =event.getAction();
        if(action==MotionEvent.ACTION_DOWN){
            if(!player.isPlaying()) {
                player.setPlaying(true);
            }
            else
                player.setUp(true);
        }
       else if(action==MotionEvent.ACTION_UP) {
            player.setUp(false);
        }
        return true;
    }

    public void update(){

        if(player.isPlaying()) {
            background.update();
            player.update();

            long bulletTimer=(System.nanoTime()-bulletStartTime)/1000000;
            if(bulletTimer>2500-player.getScore()/4){ //change
                bullets.add(new Bullet(BitmapFactory.decodeResource(getResources(), R.drawable.bullet), player.getX() + player.getWidth(), player.getY() + player.getHeight()/2-9));
                bulletStartTime=System.nanoTime();
            }
            for(Bullet bullet:bullets){
                bullet.update();

                if(bullet.leftBorder()>widthScreen+200) //change
                {
                    Log.d("enemy", bullet.leftBorder()+" "+widthScreen+"");
                    Log.d("enemy", "bullet");
                    bullets.remove(bullet);
                    break;
                }
            }
            long enemyTimer=(System.nanoTime()-enemyStartTime)/1000000;
            if(enemyTimer>10000-player.getScore()/4) {
                enemies.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.rsz_dragon2), widthScreen + 10, (int) (random.nextDouble()* (heightScreen - 130)) ,player.getScore(),getResources()));
                enemyStartTime = System.nanoTime();
            }

            for(Enemy enemy:enemies){
                enemy.update();

                if(collisionDetection(enemy,player)){
                    enemies.remove(enemy);
                    player.setPlaying(false);
                    break;
                }

                if(enemy.rightBorder()<0)
                {
                    Log.d("enemy", "enemy");
                  enemies.remove(enemy);
                  break;
                }
                for(Bullet bullet:bullets){
                    if(collisionDetection(enemy,bullet)){
                      //  explosion=new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.rsz_dragon2),enemy.getX(),enemy.getY(),getResources());
                        enemies.remove(enemy);
                        bullets.remove(bullet);
                        break;
                    }
                }

            }

            long obstacleTimer=(System.nanoTime()-obstacleStartTime)/1000000;
            if(obstacleTimer>12000-player.getScore()/4) {
                obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(), R.drawable.rsz_pillarnew1crop_removebg), widthScreen + 10, heightScreen/2));
                obstacleStartTime = System.nanoTime();
            }
            for(Obstacle obstacle : obstacles){
                obstacle.update();
                if(collisionDetection(player,obstacle)){
                    player.setPlaying(false);
                    break;
                }
                if(obstacle.rightBorder()<0){
                    obstacles.remove(obstacle);
                }

            }

        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(canvas!=null){
            final int saveState=canvas.save();

            background.draw(canvas);
            player.draw(canvas);
            canvas.restoreToCount(saveState);
            for(Bullet bullet:bullets)
                bullet.draw(canvas);
            for(Enemy enemy:enemies)
                enemy.draw(canvas);
            for(Obstacle obstacle:obstacles)
                obstacle.draw(canvas);
//            if(explosion!=null) {
//                explosion.draw(canvas);
//                explosion=null;
//            }
        }
    }
    public boolean collisionDetection(Position first,Position second){

        if(first instanceof Enemy && second instanceof Player&&Rect.intersects(first.getRect(), second.getRect()))
            Log.d("rect", first.getRect().left+" "+first.getRect().right+" "+second.getRect().left+" "+second.getRect().right);
        return Rect.intersects(first.getRect(), second.getRect());
    }

}
