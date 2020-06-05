package com.example.androidgameproject;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private int score;
    private long distance;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", distance=" + distance +
                '}';
    }

    public User(String name, int score, long distance) {
        this.name = name;
        this.score = score;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public User() {
    }
}
