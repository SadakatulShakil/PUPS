package com.astronist.pups;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {
    private GifImageView splashView;
    public static final String TAG = "splash";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashView = findViewById(R.id.splashView);
        runSplash();
    }

    private void runSplash() {
        Thread timer  = new Thread(){
            public void run(){
                try {
                    sleep(3500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                finally{
                    finish();
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}