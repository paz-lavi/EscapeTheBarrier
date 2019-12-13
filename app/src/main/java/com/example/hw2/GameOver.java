package com.example.hw2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

public class GameOver extends AppCompatActivity {

    private Button gameover_BT_pa, gameover_BT_menu, gameover_BT_save;
    private TextView gameover_LBL_score;
    private LocationManager locationManager;
    private LocationListener listener;
    private EditText gameover_EDT_name;
    private double latitude;
    private double longitude;
    private int score = 0;
    private int coins = 0;
    private Gson gson;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over);


        gameover_BT_menu = findViewById(R.id.gameover_BT_menu);
        gameover_BT_pa = findViewById(R.id.gameover_BT_pa);
        gameover_LBL_score = findViewById(R.id.gameover_LBL_score);
        gameover_EDT_name = findViewById(R.id.gameover_EDT_name);
        gameover_BT_save = findViewById(R.id.gameover_BT_save);

        gson = new Gson();


        Bundle b = getIntent().getExtras();
        if (b != null) {
            score = b.getInt(Constants.SCORE_KEY);
            coins = b.getInt(Constants.COIN_KEY);
            latitude = b.getDouble(Constants.LATITUDE_KEY , latitude);
           longitude = b.getDouble(Constants.LONGITUDE_KEY , longitude);
        }
        gameover_LBL_score.setText(score + " Barriers\n" + coins + " Coins");


        gameover_BT_pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GameOver.this, Game.class);
                GameOver.this.startActivity(myIntent);
                finish();
            }
        });
        gameover_BT_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        configure_button();

    }


    void configure_button() {

        gameover_BT_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click();
            }
        });

    }

    private void click() {

        Record r = new Record(gameover_EDT_name.getText().toString(), coins, latitude, longitude);
        Toast.makeText(GameOver.this, r.toString(), Toast.LENGTH_LONG).show();
        Intent i = new Intent(GameOver.this, HighScores.class);
        Bundle b = new Bundle();
        String str = gson.toJson(r);
        Log.d("ssstt", "str =" + str);
        b.putString(Constants.RECORD_KEY, str);
        i.putExtras(b);
        GameOver.this.startActivity(i);

    }


}


