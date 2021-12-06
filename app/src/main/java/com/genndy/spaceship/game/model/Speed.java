package com.genndy.spaceship.game.model;

public class Speed {
    private float xv=0;// составляющая скорости по оси X
    private float yv=0;// составляющая скорости по оси Y

    public Speed(){
        this.xv=0;
        this.yv=0;
    }

    public Speed(float xv, float yv){
        this.xv = xv;
        this.yv = yv;
    }

    public float getXv(){
        return xv;
    }
    public void setXv(float xv){
        this.xv= xv;
    }
    public float getYv(){
        return yv;
    }
    public void setYv(float yv){
        this.yv= yv;
    }

    // изменяем направление по оси X
    public void toggleXDirection(){
        xv=-xv/2;
    }

    // изменяем направление по оси Y
    public void toggleYDirection(){
        yv=-yv/2;
    }
}
