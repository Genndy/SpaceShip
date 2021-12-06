package com.genndy.spaceship.game.ui;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.genndy.spaceship.R;

public class GameActivity extends AppCompatActivity {
    private MainGamePanel mainGamePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  // запрос на отключение строки заголовка
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN); // перевод приложения в полноэкранный режим
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mainGamePanel = new MainGamePanel(this, getWindowManager().getDefaultDisplay(), sensorManager);
        setContentView(mainGamePanel); // устанавливаем MainGamePanel как View
    }

    @Override
    public void onBackPressed() {
        mainGamePanel.backToMenu();
        super.onBackPressed();
    }

    @Override
    protected void onStop(){
        Log.d(TAG,"Stopping...");
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        Log.d(TAG,"Destroying...");
        super.onDestroy();
    }
}