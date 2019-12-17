package com.example.hw2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {

    private Button gameover_BT_pa, gameover_BT_menu, gameover_BT_save;
    private TextView gameover_LBL_score, gameover_LBL_newrecord;
    private EditText gameover_EDT_name;
    private double latitude;
    private double longitude;
    private int score = 0;
    private int coins = 0;
    private RecordManager recordManager;
    private final String COINS_STR = " Coins";
    private boolean new_high_score;


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
        gameover_LBL_newrecord = findViewById(R.id.gameover_LBL_newrecord);
        gameover_EDT_name = findViewById(R.id.gameover_EDT_name);
        gameover_BT_save = findViewById(R.id.gameover_BT_save);


        Bundle b = getIntent().getExtras();
        if (b != null) {
            Log.d("latlag", "n kbkn");

            score = b.getInt(Constants.SCORE_KEY);
            coins = b.getInt(Constants.COIN_KEY);
            latitude = b.getDouble(Constants.LATITUDE_KEY, latitude);
            longitude = b.getDouble(Constants.LONGITUDE_KEY, longitude);
        }
        gameover_LBL_score.setText(coins + COINS_STR);


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

        recordManager = new RecordManager(GameOver.this);

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

        Intent i = new Intent(GameOver.this, MapsActivity.class);

        if (new_high_score) {
            Record r = new Record(gameover_EDT_name.getText().toString(), coins, latitude, longitude);
            Log.d("latlag", "lat = " + latitude + " log= " + longitude);
            recordManager.addRecord(r);
        }
        GameOver.this.startActivity(i);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (coins > recordManager.getLastPlace() || recordManager.getRecords().size() < Constants.MAX_RECORDS) {
            gameover_LBL_newrecord.setVisibility(View.VISIBLE);
            gameover_EDT_name.setVisibility(View.VISIBLE);
            gameover_BT_save.setVisibility(View.VISIBLE);
            new_high_score = true;
        } else {
            gameover_LBL_newrecord.setVisibility(View.INVISIBLE);
            gameover_EDT_name.setVisibility(View.INVISIBLE);
            gameover_BT_save.setText(Constants.HIGH_SCORES);
            new_high_score = false;

        }
    }
}


