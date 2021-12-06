package com.genndy.spaceship.result.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.genndy.spaceship.R;
import com.genndy.spaceship.GameConstants;
import com.genndy.spaceship.game.ui.GameActivity;

public class ResultActivity extends AppCompatActivity implements GameConstants {
    private SharedPreferences sharedPreferences;
    private TextView mScore;
    private ImageButton mStartNewGame;
    private ImageView mStarship;
    private int score;
    private int record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mScore = (TextView) findViewById(R.id.score_text);
        mStartNewGame = (ImageButton) findViewById(R.id.start_new_game_button);
        mStarship = (ImageView) findViewById(R.id.starship_cracked_end);
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME_RECORD, Context.MODE_PRIVATE);
        score = getIntent().getIntExtra(SCORE, 0);
        record = sharedPreferences.getInt(RECORDS, 0);

        AnimationSet set = new AnimationSet(false);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 1500, 150, -650);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 30, 1F, 1F);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.4f, 1f, 1.4f);
        set.addAnimation(translateAnimation);
        set.addAnimation(rotateAnimation);
        set.addAnimation(scaleAnimation);
        set.setDuration(15000);
        mStarship.startAnimation(set);

        if(score < record){
            mScore.setText("Score: " + score + "\n\nRecord:" + record);
        }else {
            mScore.setText("Score: " + score + "\n\nNEW RECORD!");
        }

        mStartNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}