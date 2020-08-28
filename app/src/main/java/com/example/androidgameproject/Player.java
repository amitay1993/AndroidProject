package com.example.androidgameproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Player extends Position implements ObjectsInterface {

    private Bitmap playerBitmap;
    private int distance,deltaY;
    private double deltaYplayer;
    private boolean isUp,isPlaying;
    private long startTime;
    private int heightScreen;
    private Bitmap shieldBitmap;
    private Paint shieldInnerPaint,shieldOuterPaint;
    private int shieldRadius;
    private boolean hasShield;

    public Player(Bitmap bitmap, int heightScreen, Resources resources) { //change
        super(100,heightScreen /2-bitmap.getHeight()/2,bitmap.getWidth(),bitmap.getHeight());
        this.playerBitmap = bitmap;
        deltaYplayer =0;
        distance =0;
        shieldRadius=height / 2+20;
        this.heightScreen=heightScreen;
        shieldInnerPaint =new Paint();
        shieldInnerPaint.setStyle(Paint.Style.FILL);
        shieldInnerPaint.setColor(resources.getColor(R.color.lightpurplealpha));

        shieldOuterPaint=new Paint();
        shieldOuterPaint.setStyle(Paint.Style.STROKE);
        shieldOuterPaint.setStrokeWidth(10);
        shieldOuterPaint.setColor(resources.getColor(R.color.lightbluealpha));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(playerBitmap, x, y, null);
        if(hasShield) {
            canvas.drawCircle(x + width / 2 - 20, y + height / 2, shieldRadius, shieldInnerPaint);
            canvas.drawCircle(x + width / 2 - 20, y + height / 2, shieldRadius, shieldOuterPaint);
        }
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public void update() {
        long timeElapsed=System.nanoTime()-startTime/1000000;
        if(timeElapsed>10000){
            distance++;
            startTime=System.nanoTime();
        }

        if(isUp){
            deltaYplayer-=0.8;
            deltaY=(int)deltaYplayer;
        }
        else{
            deltaYplayer+=0.4;
            deltaY=(int)deltaYplayer;
        }
        if(deltaY>14)
            deltaY=14;
        if(deltaY<-14)
            deltaY=-14;
        y+=deltaY*2;
        deltaY=0;
        if(bottomBorder()>this.heightScreen) {
            y = this.heightScreen - height;
            deltaYplayer=0;
        }
        else if(topBorder()<0) {
            y = 0;
            deltaYplayer=0;
        }

    }
    public boolean collisionDetection(Position second) {
        boolean hasCollided=false;
        if(isHasShield()) {
            hasCollided=collisionWithShield(second);
        }
        else
            hasCollided=collisionWithoutShield(second);
        return hasCollided;
    }

    private boolean collisionWithShield(Position second){
        if (second instanceof Obstacle) { // checking collision between circle and rectangle
            int middlePlayerX = super.x + super.width / 2 - 20;
            int middlePlayerY = super.y + super.height / 2;
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
            if (distance <= getShieldRadius()) { // if the distance is less than the radius, collision!
                return true;
            }
            return false;
        }
        else { // checking collision between two circles
            int enemyRadius = second.width / 2;
            int middleEnemyX = second.getX() + enemyRadius, middleEnemyY = second.getY() + enemyRadius;
            int middlePlayerX = super.x + super.width / 2 - 20, middlePlayerY = super.y + super.height / 2;
            int distanceBetweenMidPoints = (int) Math.sqrt((middleEnemyX - middlePlayerX) *
                    (middleEnemyX - middlePlayerX) + (middleEnemyY - middlePlayerY) * (middleEnemyY - middlePlayerY));
            if (distanceBetweenMidPoints <= enemyRadius + getShieldRadius()) // if distance between circles is less than
                return true;                                                       // sum of radius collision!
            return false;
        }
    }
    private boolean collisionWithoutShield(Position second){
        int leftX, rightX, botY, midY, topY;
        leftX = super.leftBorder()+20;
        botY = super.bottomBorder()-20;
        rightX = super.rightBorder()-20;
        midY = super.bottomBorder() - super.getHeight() / 2;
        topY = super.topBorder()+20;

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
    private boolean linesTouching(Line l1,Line l2) {

        float denom=((l2.end.y-l2.start.y)*(l1.end.x-l1.start.x) - (l2.end.x-l2.start.x)*(l1.end.y-l1.start.y));
        float uA = ((l2.end.x-l2.start.x)*(l1.start.y-l2.start.y) - (l2.end.y-l2.start.y)*(l1.start.x-l2.start.x)) / denom;
        float uB = ((l1.end.x-l1.start.x)*(l1.start.y-l2.start.y) - (l1.end.y-l1.start.y)*(l1.start.x-l2.start.x)) / denom;
        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
            return true;
        }
        return false;
    }


    public void setUp(boolean up) {
        this.isUp = up;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }


    public Bitmap getPlayerBitmap() {
        return playerBitmap;
    }

    public boolean isHasShield() {
        return hasShield;
    }

    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
    }

    public int getShieldRadius() {
        return shieldRadius;
    }
}