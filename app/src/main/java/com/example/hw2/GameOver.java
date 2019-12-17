package com.example.hw2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

        init();
    }

    /**
     * initialize the activity
     */
    private void init() {
        gameover_BT_menu = findViewById(R.id.gameover_BT_menu);
        gameover_BT_pa = findViewById(R.id.gameover_BT_pa);
        gameover_LBL_score = findViewById(R.id.gameover_LBL_score);
        gameover_LBL_newrecord = findViewById(R.id.gameover_LBL_newrecord);
        gameover_EDT_name = findViewById(R.id.gameover_EDT_name);
        gameover_BT_save = findViewById(R.id.gameover_BT_save);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            coins = b.getInt(Constants.COIN_KEY);
            latitude = b.getDouble(Constants.LATITUDE_KEY, latitude);
            longitude = b.getDouble(Constants.LONGITUDE_KEY, longitude);
        }
        gameover_LBL_score.setText(coins + COINS_STR);

        recordManager = new RecordManager(GameOver.this);

        configureButtons();
    }

    /**
     * configuring click listener for all the activity buttons
     */
    void configureButtons() {

        gameover_BT_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnSaveBT();
            }
        });

        gameover_BT_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gameover_BT_pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GameOver.this, Game.class);
                GameOver.this.startActivity(myIntent);
                finish();
            }
        });


    }

    /**
     * handling click on the SAVE or High Score Button
     * If a new record is reached add it to the records DB
     */
    private void clickOnSaveBT() {
        Intent i = new Intent(GameOver.this, MapsActivity.class);
        if (new_high_score) {
            Record r = new Record(gameover_EDT_name.getText().toString(), coins, latitude, longitude);
            recordManager.addRecord(r);
        }
        GameOver.this.startActivity(i);
        finish();

    }


    /**
     * If a new record is reached show edit text for get the player name
     * else change "save" BT to high score BT. in both case after click on this BT this activity finish and moving to
     * the high score activity
     */
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
            gameover_BT_save.setVisibility(View.VISIBLE);
            new_high_score = false;

        }
    }
}


