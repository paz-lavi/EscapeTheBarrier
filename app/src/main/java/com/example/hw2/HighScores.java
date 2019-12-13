package com.example.hw2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class HighScores extends AppCompatActivity {
    private ArrayList records;
    private File root, dataFile;
    private Gson gson;
    private final String FILE_NAME = "EscapeTheBarrierHIGHSCORES.txt";
    private TextView[] lables;
    boolean ok;
    Button high_BT_maps;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        records = new ArrayList<>();
        gson = new Gson();
        root = getPath();
        dataFile = new File(root, FILE_NAME);
        ok = false;
        high_BT_maps = findViewById(R.id.high_BT_maps);
        readFromFile();
        lables = new TextView[]{findViewById(R.id.high_LBL_place1), findViewById(R.id.high_LBL_place2), findViewById(R.id.high_LBL_place3)
                , findViewById(R.id.high_LBL_place4), findViewById(R.id.high_LBL_place5), findViewById(R.id.high_LBL_place6), findViewById(R.id.high_LBL_place7)
                , findViewById(R.id.high_LBL_place8), findViewById(R.id.high_LBL_place9), findViewById(R.id.high_LBL_place10)};
        String str = "";
        Bundle b = getIntent().getExtras();
        if (b != null){
            str = b.getString(Constants.RECORD_KEY);
        if (str != "") {
            Log.d("Hige Score Class", "str = " + str);
            records.add(gson.fromJson(str, new TypeToken<Record>() {
            }.getType()));
            setTopTen();
        }
        } else Log.d("Hige Score Class", "arrived from menu");

        for (int i = 0; i < records.size(); i++) {
            if (i >= 10)
                break;
            Log.d("Hige Score Class", "setting text");
            lables[i].setText(records.get(i).toString());
        }
        high_BT_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HighScores.this, MapsActivity.class);
                Bundle b = new Bundle();
                String str = gson.toJson(records);
                Log.d("Hige Score Class", "str =" + str);
                b.putString(Constants.RECORD_ARRAY_KEY, str);
                i.putExtras(b);
                HighScores.this.startActivity(i);
            }
        });
    }

    private void setTopTen() {
        Collections.sort(records);
        Log.d("Hige Score Class", "size" + records.size());
        while (records.size() > Constants.MAX_RECORDS) {
            records.remove(records.size() - 1);
        }
        writeToFile();
    }

    private File getPath() {
        File rootPath = new File(Environment.getExternalStorageDirectory(), "directory_name");
        if (!rootPath.exists()) {
            if (rootPath.mkdirs()) {
                Log.d("ANDROID_TEST", "Directory create success :" + rootPath.getAbsolutePath());
            } else {
                Log.d("ANDROID_TEST", "FAILED TO CREATE DIRECTORY :" + rootPath.getAbsolutePath());
            }
        } else Log.d("ANDROID_TEST", "root: :" + rootPath.getAbsolutePath());
        return rootPath;
    }


    private void writeToFile() {
        if (records.size() > 0) {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        write();
                    } else {
                        requestPermission(); // Code for permission
                        writeToFile();
                    }
                } else {
                    write();
                }
            }
        }
    }

    private void write() {
        File sdcard = getFilesDir();
        File dir = new File(sdcard.getAbsolutePath() + File.separator + "game" + "");
        dir.mkdirs();
        File file = new File(dir, FILE_NAME);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            String data = gson.toJson(records);
            Log.d("Hige Score Class", "data = " + data);
            os.write(data.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void readFromFile() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    read();
                } else {
                    requestPermission(); // Code for permission
                    readFromFile();
                }
            } else {
                read();
            }
        }
    }

    private void read() {
        File sdcard = getFilesDir();
        File dir = new File(sdcard.getAbsolutePath() + File.separator + "game" + "");
        dir.mkdirs();
        File file = new File(dir, FILE_NAME);
        FileInputStream os = null;
        try {
            os = new FileInputStream(file);
            byte[] input = new byte[os.available()];
            String data = "";
            Log.d("Hige Score Class", "data = " + data);
            os.read(input);
            os.close();
            String s = new String(input);

            data = s.toString();
            records = gson.fromJson(data, new TypeToken<ArrayList<Record>>() {
            }.getType());
            Log.d("Hige Score Class", "record = " + records.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(HighScores.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(HighScores.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(HighScores.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(HighScores.this, "Write External Storage permission allows us to create files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(HighScores.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(HighScores.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.d("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}


