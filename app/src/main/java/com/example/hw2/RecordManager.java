package com.example.hw2;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
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

public class RecordManager {
    // private  Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private ArrayList<Record> records;
    private Gson gson;
    private final String game = "game";


    public RecordManager(Activity activity) {
        this.activity = activity;
        records = new ArrayList<>();
        gson = new Gson();
        readFromFile();


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
        File sdcard = activity.getApplicationContext()
                .getFilesDir();
        File dir = new File(sdcard.getAbsolutePath() + File.separator + game);
        dir.mkdirs();
        File file = new File(dir, Constants.FILE_NAME);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            String data = gson.toJson(records);
            Log.d("My File Class", "data = " + data);
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
        File sdcard = activity.getApplicationContext().getFilesDir();
        File dir = new File(sdcard.getAbsolutePath() + File.separator + game);
        dir.mkdirs();
        File file = new File(dir, Constants.FILE_NAME);
        FileInputStream os = null;
        try {
            os = new FileInputStream(file);
            byte[] input = new byte[os.available()];
            os.read(input);
            os.close();
            String s = new String(input);

            records = gson.fromJson(s, new TypeToken<ArrayList<Record>>() {
            }.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(activity.getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                !ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

        }
    }

    public ArrayList getRecords() {
        return records;
    }

    public void addRecord(Record r) {
        records.add(r);
        Collections.sort(records);
        while (records.size() > Constants.MAX_RECORDS) {
            records.remove(records.size() - 1);
        }
        writeToFile();
    }

    public int getLastPlace() {
        if (records.size() > 0)
            return records.get(records.size() - 1).getScore();
        else
            return -1;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.d("value", "Permission Denied, You cannot use local drive .");
            }

        }
    }
}
