package com.genndy.spaceship.game.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Asteroid {
    private Bitmap bitmap;
    private int startPoint;
    private int x;
    private int y;
    private int left;
    private int right;
    private int top;
    private int bottom;
    private Speed speed;
    private boolean isDestroyed;

    public Asteroid(Bitmap bitmap, int x, int y, int startPoint) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.speed = new Speed();
        this.startPoint = startPoint;
        this.isDestroyed = false;
    }

    public Asteroid(Bitmap bitmap, int x, int y, int xv, int yv) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.speed = new Speed();
        this.speed.setYv(yv);
        this.speed.setXv(xv);
        this.startPoint = startPoint;
        this.isDestroyed = false;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x-(bitmap.getWidth()/2), y-(bitmap.getHeight()/2),null);
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public void update() {
        x=(int)(x+speed.getXv());
        y=(int)(y+speed.getYv());

        left = x - bitmap.getWidth()/2;
        right = x + bitmap.getWidth()/2;
        bottom = y + bitmap.getHeight()/2;
        top = y - bitmap.getHeight()/2;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }
}
