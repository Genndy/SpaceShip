package com.genndy.spaceship.game.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Blaster {
    private Bitmap bitmap;
    private int startPoint;
    private int x;
    private int y;
    private int left;
    private int right;
    private int top;
    private int bottom;

    public Blaster(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
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
        y=(int)(y-15);

        left = x - bitmap.getWidth()/2;
        right = x + bitmap.getWidth()/2;
        bottom = y + bitmap.getHeight()/2;
        top = y - bitmap.getHeight()/2;
    }
}
