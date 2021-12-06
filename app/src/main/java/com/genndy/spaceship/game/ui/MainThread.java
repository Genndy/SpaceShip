package com.genndy.spaceship.game.ui;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread{

    private static final String TAG= MainThread.class.getSimpleName();

    private boolean running;

    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;

    public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void run(){
        Canvas canvas;
        Log.d(TAG,"Starting game loop");
        while(running){
            canvas=null;
            try{ // пытаемся заблокировать canvas для изменение картинки на поверхности
                canvas= this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder){
                    this.gamePanel.update(); // обновляем состояние формируем новый кадр
                    this.gamePanel.draw(canvas);//Вызываем метод для рисования
                }
            } finally{ // в случае ошибки, плоскость не перешла в требуемое состояние
                if(canvas!=null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
