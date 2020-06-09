package com.example.androidgameproject;

import android.graphics.Rect;

abstract public class Position {
    protected int y,x,deltaY,deltaX,width,height;

    public Position(int x,int y, int width, int height) {
        this.y = y;
        this.x = x;
        this.width = width;
        this.height = height;
    }
    public int topBorder(){
        return y;
    }
    public int bottomBorder(){
        return y+height;
    }
    public int leftBorder(){
        return x;
    }
    public int rightBorder(){
        return x+width;
    }

    public Rect getRect(){
        return new Rect(x+10,y,rightBorder()-10,bottomBorder());
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
