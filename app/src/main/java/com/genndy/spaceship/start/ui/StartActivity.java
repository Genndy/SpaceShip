package com.genndy.spaceship.start.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.genndy.spaceship.R;
import com.genndy.spaceship.game.ui.GameActivity;

public class StartActivity extends AppCompatActivity {
    private ImageButton mStartGameBtn;
    private ImageView mSun;
    private ImageView mMoon;
    private ImageView mSpaceship;
    private ImageView mStars;
    private ImageView mShadow;
    private ImageView mSpaceshipLogo;
    private boolean isButtonStartClickable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mStartGameBtn = (ImageButton) findViewById(R.id.start_game_button);
        isButtonStartClickable = false;
        mSun = (ImageView) findViewById(R.id.sun);
        mMoon = (ImageView) findViewById(R.id.moon);
        mSpaceship = (ImageView) findViewById(R.id.spaceship_start);
        mStars = (ImageView) findViewById(R.id.start_stars);
        mShadow = (ImageView) findViewById(R.id.shadow);
        mSpaceshipLogo = (ImageView) findViewById(R.id.spaceship_logo);
        startAnimation();
    }

    @Override
    protected void onResume(){mStartGameBtn = (ImageButton) findViewById(R.id.start_game_button);
        mSpaceshipLogo.setVisibility(View.VISIBLE);
        mStartGameBtn.setVisibility(View.VISIBLE);
        mShadow.setVisibility(View.VISIBLE);
        mSpaceship.setVisibility(View.VISIBLE);
        super.onResume();
        startAnimation();
    }

    private void startAnimation(){
        TranslateAnimation taMoon = new TranslateAnimation(0, 0, 400, 0);
        TranslateAnimation taSun = new TranslateAnimation(0, 0, -200, 0);
        TranslateAnimation tas = new TranslateAnimation(0, 0, 100, 0);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mStartGameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StartActivity.this, GameActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mStartGameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            startGame();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAnimation.setDuration(4000);
        taMoon.setDuration(4000);
        taSun.setDuration(4000);
        tas.setDuration(4000);
        mSun.startAnimation(taSun);
        mMoon.startAnimation(taMoon);
        mSpaceship.startAnimation(taMoon);
        mSpaceshipLogo.setAnimation(alphaAnimation);
        mStartGameBtn.setAnimation(alphaAnimation);
    }
    private void startGame(){
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -1400);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        translateAnimation.setDuration(2000);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSpaceship.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(StartActivity.this, GameActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAnimation.setDuration(1000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSpaceshipLogo.setVisibility(View.INVISIBLE);
                mStartGameBtn.setVisibility(View.INVISIBLE);
                mShadow.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mSpaceshipLogo.startAnimation(alphaAnimation);
        mStartGameBtn.startAnimation(alphaAnimation);
        mShadow.startAnimation(alphaAnimation);
        mSpaceship.setImageResource(R.drawable.starship_start_on);
        mSpaceship.startAnimation(translateAnimation);
    }
}