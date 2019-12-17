package com.example.hw2;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Instructions extends AppCompatActivity {
    TextView instructions_LBL_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_instructions);

        instructions_LBL_text = findViewById(R.id.instructions_LBL_text);
        instructions_LBL_text.setText(Constants.INSTRUCTIONS);
    }
}
