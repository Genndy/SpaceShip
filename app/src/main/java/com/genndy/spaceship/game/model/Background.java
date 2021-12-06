package com.genndy.spaceship.game.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
    private Bitmap bitmap;
    private int y;

    public Background(Bitmap bitmap, int y) {
        this.bitmap = bitmap;
        this.y = y;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, 0, y,null);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
