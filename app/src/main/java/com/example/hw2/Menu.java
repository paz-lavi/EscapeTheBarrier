package com.example.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {
    Button menu_BT_slow, menu_BT_fast, menu_BT_move, menu_BT_high, menu_BT_instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);

        init();

    }


    /**
     * initialize the activity
     */
    private void init() {

        menu_BT_slow = findViewById(R.id.menu_BT_slow);
        menu_BT_fast = findViewById(R.id.menu_BT_fast);
        menu_BT_move = findViewById(R.id.menu_BT_move);
        menu_BT_high = findViewById(R.id.menu_BT_high);
        menu_BT_instructions = findViewById(R.id.menu_BT_instructions);

        configureButtons();

    }

    private void configureButtons() {


        menu_BT_slow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntent(Constants.SLOW_GAME);
            }
        });
        menu_BT_fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntent(Constants.FAST_GAME);

            }
        });
        menu_BT_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntent(Constants.MOTION_GAME);
            }
        });
        menu_BT_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, MapsActivity.class);
                Menu.this.startActivity(i);


            }
        });
        menu_BT_instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Instructions.class);
                Menu.this.startActivity(i);
            }
        });

    }

    /**
     * @param gameType - game mode slow speed or movement
     *                 make intent to the game activity , sending the index for the game mode
     */
    private void makeIntent(int gameType) {
        Intent myIntent = new Intent(Menu.this, Game.class);
        Bundle b = new Bundle();
        b.putInt(Constants.TYPE_KEY, gameType);
        myIntent.putExtras(b);
        Menu.this.startActivity(myIntent);
    }
}

