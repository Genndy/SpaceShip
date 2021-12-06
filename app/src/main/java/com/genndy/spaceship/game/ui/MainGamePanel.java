package com.genndy.spaceship.game.ui;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.genndy.spaceship.GameConstants;
import com.genndy.spaceship.R;
import com.genndy.spaceship.game.model.Background;
import com.genndy.spaceship.game.model.Blaster;
import com.genndy.spaceship.game.model.Spaceship;
import com.genndy.spaceship.game.model.Asteroid;
import com.genndy.spaceship.result.ui.ResultActivity;

import java.util.ArrayList;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback, GameConstants {
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor sensor;
    private MainThread thread;
    private Spaceship spaceship;
    private int displayWidth;
    private int displayHeight;
    private int shipSpeed;
    private int score;
    private int health;
    private int bullets;
    private int trafficCount;
    private ArrayList<Background> space;
    private ArrayList<Background> stars;
    private ArrayList<Background> stars2;
    private ArrayList<Background> dusts;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Asteroid> asteroidRuins;
    private ArrayList<Blaster> blasters;
    private Paint shadowPaint;
    private Paint tutorialPaint;
    private Bitmap starShipDestroyed;
    private Bitmap starShip1;
    private Bitmap starShip2;
    private Bitmap starShip3;
    private Bitmap asteroidBitmap1;
    private Bitmap asteroidBitmap2;
    private Bitmap asteroidBitmap3;
    private Bitmap asteroidBitmap4;
    private Bitmap asteroidDestroyed;
    private Bitmap dustBitmap1;
    private Bitmap dustBitmap2;
    private Bitmap dustBitmap3;
    private Bitmap dustBitmap4;
    private Bitmap blasterBitmap;
    private Bitmap asteroidRuinBitmap;
    private ArrayList<Bitmap> starshipRuins;
    private Long gameTact;
    private Long bulletReloadTime;
    private Long flameAnimationDelay;
    private Long farStarAnimationDelay;
    private Long endGameTime;


    public MainGamePanel(Context context, Display display, SensorManager sensorManager) {
        super(context);
        preferences = getContext().getSharedPreferences(PREFERENCES_NAME_RECORD, Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(Color.WHITE);
        shadowPaint.setTextSize(35.0f);
        shadowPaint.setShadowLayer(5.0f, 5.0f, 5.0f, Color.BLACK);

        tutorialPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(Color.WHITE);
        shadowPaint.setTextSize(50.0f);
        shadowPaint.setShadowLayer(5.0f, 5.0f, 5.0f, Color.BLACK);

        gameTact = 0L;
        flameAnimationDelay = 0L;
        farStarAnimationDelay = 0L;
        bulletReloadTime = 0L;
        endGameTime = 0L;

        getHolder().addCallback(this);
        setZOrderOnTop(true);
        Point point = new Point();
        display.getSize(point);
        displayWidth = point.x;
        displayHeight = point.y;
        shipSpeed = 10;
        health = 5;
        bullets = 10;

        starshipRuins = new ArrayList<>();

        gameObjectsInit();

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

        sensor = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
        SensorEventListener mySensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                if(!spaceship.isDestroyed()){
                    spaceship.getSpeed().setXv(spaceship.getSpeed().getXv() + (x / -5));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(mySensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public MainGamePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainGamePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()== MotionEvent.ACTION_DOWN){
            if(bullets >= 1 && !spaceship.isDestroyed()){
                blasters.add(new Blaster(blasterBitmap,
                        spaceship.getX(),
                        spaceship.getY()));
                bullets--;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void backToMenu(){
        thread.setRunning(false);
    }

    public void endGame(){
        thread.setRunning(false);
        Intent intent = new Intent(getContext(), ResultActivity.class);
        intent.putExtra(SCORE, score);
        int record = preferences.getInt(RECORDS, 0);
        if (score > record){
            preferencesEditor.putInt(RECORDS, score);
            preferencesEditor.commit();
        }
        getContext().startActivity(intent);
        Activity activity = ((Activity) getContext());
        activity.finish();
    }

    public void update() {
        gameTact++;

        if(spaceship.getCarRight() >= getWidth()){
            spaceship.getSpeed().toggleXDirection();
            spaceship.setX(displayWidth - spaceship.getBitmap().getWidth()/2 - 1);
        }
        if( spaceship.getCarLeft() <= 0){
            spaceship.getSpeed().toggleXDirection();
            spaceship.setX(spaceship.getBitmap().getWidth()/2 + 1);
        }
        if(spaceship.getCarBottom() >= getHeight()){
            spaceship.getSpeed().toggleYDirection();
        }
        if(spaceship.getCarBottom() <= - 10){
            endGame();
//            spaceship.getSpeed().toggleYDirection();
        }

        if(gameTact >= flameAnimationDelay && !spaceship.isDestroyed()){
            flameAnimationDelay += 10;
            switch (getRandomNumber(1, 4)) {
                case 1 : spaceship.setBitmap(starShip1); break;
                case 2 : spaceship.setBitmap(starShip2); break;
                case 3 : spaceship.setBitmap(starShip3); break;
            }
        }

        if(gameTact >= bulletReloadTime){
            if(bullets < 5){
                bullets++;
                bulletReloadTime = gameTact + 500;
            }
        }

        spaceship.update();

        for (Background background : stars){
            if(gameTact >= farStarAnimationDelay){
                background.setY(background.getY() + 1);
                if(background.getY() >= displayHeight){
                    background.setY(-displayHeight * 3);
                }
                farStarAnimationDelay += 4;
            }
        }

        for (Background background : stars2){
            background.setY(background.getY() + 1);
            if(background.getY() >= displayHeight){
                background.setY(-displayHeight * 3);
            }
        }

        for (Background background : dusts){
            background.setY(background.getY() + shipSpeed);
            if(background.getY() >= displayHeight){
                background.setY(-displayHeight * 3);
                if(shipSpeed < 50){
                    shipSpeed++;
                }
            }
        }

        if(score > asteroids.size() * 20 && asteroids.size() < 5) {
            asteroids.add(new Asteroid(asteroidBitmap1, getRandomNumber(asteroidBitmap1.getWidth()/2,
                    displayWidth - asteroidBitmap1.getWidth()/2), -asteroidBitmap1.getHeight(),
                    (int) (- asteroidBitmap1.getHeight() * (0.5 * asteroids.size()))));
            for (Asteroid asteroid : asteroids){
                asteroid.getSpeed().setYv(shipSpeed / 2);
            }
        }

        for (Blaster blaster : blasters){
            blaster.update();
        }
        if(blasters.size() != 0){
            for (int i = 0; i < blasters.size(); i++){
                if(blasters.get(i).getBottom() <= 0){
                    blasters.remove(i);
                }
            }
        }

        for (Asteroid asteroid : asteroids){
            asteroid.update();
            if(asteroid.getY() >= displayHeight){
                score++;
                asteroid.setDestroyed(false);
                asteroid.getSpeed().setYv(shipSpeed / 2);
                asteroid.getSpeed().setXv(0);
                asteroid.setX(getRandomNumber(asteroid.getBitmap().getWidth(),
                        displayWidth - asteroid.getBitmap().getWidth()));
                asteroid.setY(-displayHeight);
                asteroid.setBitmap(asteroidBitmap1);
            }
        }

        for (Asteroid asteroidRuin : asteroidRuins){
            asteroidRuin.update();
        }

        if(asteroidRuins.size() != 0){
            for (int i = 0; i < asteroidRuins.size(); i++){
                if(asteroidRuins.get(i).getTop() <= 0 || asteroidRuins.get(i).getBottom() >= displayHeight ||
                asteroidRuins.get(i).getRight() <= 0 || asteroidRuins.get(i).getLeft() >= displayWidth){
                    asteroidRuins.remove(i);
                }
            }
        }

        for (Asteroid asteroid : asteroids){
            asteroid.update();
            if(asteroid.getY() >= displayHeight){
                score++;
                asteroid.setDestroyed(false);
                asteroid.getSpeed().setYv(shipSpeed / 2);
                asteroid.getSpeed().setXv(0);
                asteroid.setX(getRandomNumber(asteroid.getBitmap().getWidth(),
                        displayWidth - asteroid.getBitmap().getWidth()));
                asteroid.setY(-displayHeight);
                asteroid.setBitmap(asteroidBitmap1);
            }
        }

        for (Asteroid asteroid : asteroids){
            for (Blaster blaster : blasters) {
                if (asteroid.getBottom() >= blaster.getTop() && asteroid.getTop() <= blaster.getBottom()) { // регистрация попадания по высоте
                    if (asteroid.getLeft() <= blaster.getRight() && asteroid.getRight() >= blaster.getLeft()) {
                        if(!asteroid.isDestroyed()){
                            for (int i = 0; i <= getRandomNumber(10, 30); i++){
                                asteroidRuins.add(new Asteroid(asteroidRuinBitmap, asteroid.getX(), asteroid.getY(),
                                        getRandomNumber(-10, 10), getRandomNumber(1, 10)));
                            }
                        }
                        asteroid.setDestroyed(true);
                        asteroid.setBitmap(asteroidDestroyed);
                    }
                }
            }

            if(asteroid.getBottom() >= spaceship.getCarTop() && asteroid.getTop() <= spaceship.getCarBottom()) { // регистрация попадания по высоте
                if(asteroid.getLeft() <= spaceship.getCarRight() && asteroid.getRight() >= spaceship.getCarLeft() &&
                        !asteroid.isDestroyed()){
                    System.out.println("Регистрация попадания");
                    health--;
                    asteroid.setDestroyed(true);
                    if(shipSpeed >= 10){
                        shipSpeed = shipSpeed - 5;
                    }
                    if(spaceship.getX() > asteroid.getX()){
                        spaceship.getSpeed().setXv(10);
                    }else{
                        spaceship.getSpeed().setXv(-10);
                    }
                    for (int i = 0; i <= getRandomNumber(10, 30); i++){
                        asteroidRuins.add(new Asteroid(asteroidRuinBitmap, asteroid.getX(), asteroid.getY(),
                                getRandomNumber(-10, 10), getRandomNumber(-10, 10)));
                        if(asteroidRuins.get(asteroidRuins.size() - 1).getSpeed().getXv() == 0 ||
                                asteroidRuins.get(asteroidRuins.size() - 1).getSpeed().getYv() == 0){
                            asteroidRuins.get(asteroidRuins.size() - 1).getSpeed().setYv(4);
                            asteroidRuins.get(asteroidRuins.size() - 1).getSpeed().setXv(8);
                        }
                    }
                    asteroid.setBitmap(asteroidDestroyed);
                    if(health <= 0){
                        for (int i = 0; i < 6; i++){
                            asteroidRuins.add(new Asteroid(starshipRuins.get(i+1), asteroid.getX(), asteroid.getY(),
                                    getRandomNumber(-10, 10), getRandomNumber(-10, 10)));
                            if(asteroidRuins.get(asteroidRuins.size() - 1).getSpeed().getXv() == 0 ||
                                    asteroidRuins.get(asteroidRuins.size() - 1).getSpeed().getYv() == 0){
                                asteroidRuins.get(asteroidRuins.size() - 1).getSpeed().setYv(4);
                                asteroidRuins.get(asteroidRuins.size() - 1).getSpeed().setXv(8);
                            }
                        }

                        shipSpeed = 3;
                        spaceship.setBitmap(starshipRuins.get(0));
                        spaceship.setDestroyed(true);
                        endGameTime = gameTact + 320;
                        spaceship.getSpeed().setYv(-5);
                        spaceship.getSpeed().setXv(0);
                    }
                }
            }
        }
    }

    private void gameObjectsInit(){
        int carWidth = 165;
        int carHeight = 165;

        starShipDestroyed = BitmapFactory.decodeResource(getResources(), R.drawable.starship_destroyed);
        starShipDestroyed = Bitmap.createScaledBitmap(starShipDestroyed, 160, 210, false);

        starShip1 = BitmapFactory.decodeResource(getResources(), R.drawable.starship_1);
        starShip1 = Bitmap.createScaledBitmap(starShip1, 80, carHeight, false);

        starShip2 = BitmapFactory.decodeResource(getResources(), R.drawable.starship_2);
        starShip2 = Bitmap.createScaledBitmap(starShip2, 80, carHeight, false);

        starShip3 = BitmapFactory.decodeResource(getResources(), R.drawable.starship_3);
        starShip3 = Bitmap.createScaledBitmap(starShip3, 80, carHeight, false);

        blasterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blast);
        blasterBitmap = Bitmap.createScaledBitmap(blasterBitmap, 10, 130, false);

        asteroidBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_1);
        asteroidBitmap1 = Bitmap.createScaledBitmap(asteroidBitmap1, carWidth, carHeight, false);
        asteroidBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_2);
        asteroidBitmap2 = Bitmap.createScaledBitmap(asteroidBitmap2, carWidth, carHeight, false);
        asteroidBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_3);
        asteroidBitmap3 = Bitmap.createScaledBitmap(asteroidBitmap3, carWidth, carHeight, false);
        asteroidBitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_4);
        asteroidBitmap4 = Bitmap.createScaledBitmap(asteroidBitmap4, carWidth, carHeight, false);

        asteroidRuinBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_4);
        asteroidRuinBitmap = Bitmap.createScaledBitmap(asteroidBitmap4, 20, 20, false);

        dustBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.dust_1);
        dustBitmap1 = Bitmap.createScaledBitmap(dustBitmap1, displayWidth, displayHeight, false);
        dustBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.dust_2);
        dustBitmap2 = Bitmap.createScaledBitmap(dustBitmap2, displayWidth, displayHeight, false);
        dustBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.dust_3);
        dustBitmap3 = Bitmap.createScaledBitmap(dustBitmap3, displayWidth, displayHeight, false);
        dustBitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.dust_4);
        dustBitmap4 = Bitmap.createScaledBitmap(dustBitmap4, displayWidth, displayHeight, false);

        starshipRuins.add(Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.starship_ruins_1),
                30, 60, false));

        starshipRuins.add(Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.starship_ruins_2),
                30, 60, false));

        starshipRuins.add(Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.starship_ruins_3),
                30, 60, false));

        starshipRuins.add(Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.starship_ruins_4),
                50, 30, false));

        starshipRuins.add(Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.starship_ruins_5),
                50, 30, false));

        starshipRuins.add(Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.starship_ruins_6),
                30, 30, false));

        starshipRuins.add(Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.starship_ruins_7),
                40, 45, false));

        asteroidDestroyed = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid_destroyed);
        asteroidDestroyed = Bitmap.createScaledBitmap(asteroidDestroyed, carWidth, carHeight, false);

        Bitmap spaceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.space);
        spaceBitmap = Bitmap.createScaledBitmap(spaceBitmap, displayWidth, displayHeight, false);

        Bitmap starsBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.stars_1);
        starsBitmap1 = Bitmap.createScaledBitmap(starsBitmap1, displayWidth, displayHeight, false);

        Bitmap starsBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.stars_2);
        starsBitmap2 = Bitmap.createScaledBitmap(starsBitmap2, displayWidth, displayHeight, false);

        Bitmap starsBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.stars_3);
        starsBitmap3 = Bitmap.createScaledBitmap(starsBitmap3, displayWidth, displayHeight, false);

        Bitmap starsBitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.stars_4);
        starsBitmap4 = Bitmap.createScaledBitmap(starsBitmap4, displayWidth, displayHeight, false);


        spaceship = new Spaceship(starShip1,displayWidth / 2,displayHeight - 300);

        asteroids = new ArrayList<Asteroid>();
        asteroidRuins = new ArrayList<>();
        space = new ArrayList<>();
        stars = new ArrayList<>();
        stars2 = new ArrayList<>();
        dusts = new ArrayList<>();
        blasters = new ArrayList<>();
        asteroids.add(new Asteroid(asteroidBitmap1, getRandomNumber(asteroidBitmap1.getWidth(),
                displayWidth - asteroidBitmap1.getWidth()), -asteroidBitmap1.getHeight(), - asteroidBitmap1.getHeight()));

        for (Asteroid asteroid : asteroids){
            asteroid.getSpeed().setYv(shipSpeed / 2);
        }

        space.add(new Background(spaceBitmap, 0));

        stars.add(new Background(starsBitmap4,0));
        stars.add(new Background(starsBitmap3,-displayHeight));
        stars.add(new Background(starsBitmap2,-displayHeight * 2));
        stars.add(new Background(starsBitmap1,-displayHeight * 3));

        stars2.add(new Background(starsBitmap1,0));
        stars2.add(new Background(starsBitmap2,-displayHeight));
        stars2.add(new Background(starsBitmap3,-displayHeight * 2));
        stars2.add(new Background(starsBitmap4,-displayHeight * 3));

        dusts.add(new Background(dustBitmap1, 0));
        dusts.add(new Background(dustBitmap2,-displayHeight));
        dusts.add(new Background(dustBitmap3,-displayHeight * 2));
        dusts.add(new Background(dustBitmap4,-displayHeight * 3));
    }
    
    protected void onDraw(Canvas canvas){
        space.get(0).draw(canvas);
        for (Background background : stars){
            background.draw(canvas);
        }
        for (Background background : stars2){
            background.draw(canvas);
        }
        for (Background background : dusts){
            background.draw(canvas);
        }
        for (Asteroid asteroid : asteroids){
            asteroid.draw(canvas);
        }
        for (Asteroid asteroidRuin : asteroidRuins){
            asteroidRuin.draw(canvas);
        }
        for (Blaster blaster : blasters){
            blaster.draw(canvas);
        }
        spaceship.draw(canvas);
        canvas.drawText("Score: " + score, 20, 100, shadowPaint);
        canvas.drawText("Health: " + health, 20, 180, shadowPaint);
        canvas.drawText("Bullets: " + bullets, 20, displayHeight - 100, shadowPaint);

        if(gameTact > 40 && gameTact < 400){
            canvas.drawText("Rotate phone to veer", (displayWidth/2) - getWidth()/3, displayHeight/2, shadowPaint);
        }

        if(gameTact > 500 && gameTact < 740){
            canvas.drawText("Tape screen to shoot!", (displayWidth/2) - getWidth()/3, displayHeight/2, shadowPaint);
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}