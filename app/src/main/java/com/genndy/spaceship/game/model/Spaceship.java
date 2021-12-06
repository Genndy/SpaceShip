package com.genndy.spaceship.game.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Spaceship {

    private Bitmap bitmap;// картинка с машиной
    private int x;// координата X
    private int y;// координата Y
    private int carLeft;
    private int carRight;
    private int carTop;
    private int carBottom;
    private boolean invincible = false;
    private Speed speed;

    public Spaceship(Bitmap bitmap, int x, int y){
        this.bitmap= bitmap;
        this.x= x;
        this.y= y;
        speed = new Speed();
    }

    public void draw(Canvas canvas){
        Matrix matrix = new Matrix();
        matrix.postTranslate(-bitmap.getWidth()/2, -bitmap.getHeight()/2);
        matrix.postTranslate(x-(bitmap.getWidth()/2), y-(bitmap.getHeight()/2));
        canvas.drawBitmap(bitmap, x-(bitmap.getWidth()/2), y-(bitmap.getHeight()/2),null);
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap= bitmap;
    }
    public int getX(){
        return x;
    }
    public void setX(int x){
        this.x= x;
    }
    public int getY(){ return y; }
    public void setY(int y){
        this.y= y;
    }
    public int getCarLeft() { return carLeft; }
    public int getCarRight() { return carRight; }
    public int getCarTop() { return carTop; }
    public int getCarBottom() { return carBottom; }
    public Speed getSpeed() {
        return speed;
    }

    public boolean isDestroyed() { return invincible; }
    public void setDestroyed(boolean invincible) { this.invincible = invincible; }

    public void update() {
        x=(int)(x+speed.getXv());
        y=(int)(y+speed.getYv());
        carLeft = x - bitmap.getWidth()/2;
        carBottom = y + bitmap.getHeight()/2;
        carRight = x + bitmap.getWidth()/2;
        carTop = y - bitmap.getHeight()/2;
    }
}
