package com.example.androidgameproject;

import java.io.Serializable;

public class User implements Serializable, Comparable {
    private String name;
    private int score;
    private long distance;

    @Override
    public int compareTo(Object o) {
        User other = (User)o;
        return Integer.compare(((User) o).score,this.score);
    }

    @Override
    public String toString() {
        return "Name= " + name +
                ", Score= " + score +
                ", Distance= " + distance;
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
