package com.example.hw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        welcome();


    }

    private void welcome() {
        final Handler handler = new Handler();
        Runnable myRun = new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(MainActivity.this, Menu.class);
                MainActivity.this.startActivity(myIntent);
                finish();


            }
        };
        handler.postDelayed(
                myRun, 1000);

    }
}
