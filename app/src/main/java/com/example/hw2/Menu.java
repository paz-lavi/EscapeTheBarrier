package com.example.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class Menu extends AppCompatActivity {
    Button menu_BT_slow, menu_BT_fast, menu_BT_move, menu_BT_high, menu_BT_settings;
    final int FAST_GAME = 1, SLOW_GAME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);


        menu_BT_slow = findViewById(R.id.menu_BT_slow);
        menu_BT_fast = findViewById(R.id.menu_BT_fast);
        menu_BT_move = findViewById(R.id.menu_BT_move);
        menu_BT_high = findViewById(R.id.menu_BT_high);
        menu_BT_settings = findViewById(R.id.menu_BT_settings);

        menu_BT_slow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu.this, Game.class);
                Bundle b = new Bundle();
                b.putInt(Constants.TYPE_KEY, Constants.SLOW_GAME);
                myIntent.putExtras(b);
                Menu.this.startActivity(myIntent);
            }
        });


        menu_BT_fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu.this, Game.class);
                Bundle b = new Bundle();
                b.putInt(Constants.TYPE_KEY, Constants.FAST_GAME); //Your id
                myIntent.putExtras(b); //Put your id to your next Intent
                Menu.this.startActivity(myIntent);
            }
        });
        menu_BT_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu.this, "Not Available At This Moment", Toast.LENGTH_SHORT).show();
            }
        });
        menu_BT_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this , HighScores.class);
                Menu.this.startActivity(i);


                Toast.makeText(Menu.this, "Not Available At This Moment", Toast.LENGTH_SHORT).show();
            }
        });
        menu_BT_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu.this, "Not Available At This Moment", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

