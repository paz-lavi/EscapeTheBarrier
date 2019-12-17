package com.example.hw2;

public class Constants {
    final static String INSTRUCTIONS = "ֿֿPurpose of the game: \n" +
            "Collect as many coins as possible and avoid barrier. \nYou have three strikes." +
            "\n\nInstructions: \n " +
            "Slow & Fast Mode: Use the arrows to move left and right.\n" +
            "Movement Mode: Tilt right to move right. Tilt left to move left. Tilt forth to increase the speed. " +
            "Tilt back to decrease the speed.\n  ";
    final static String LATITUDE_KEY = "latitude";
    final static String LONGITUDE_KEY = "longitude";
    final static String SCORE_KEY = "score";
    final static String COIN_KEY = "coin";
    final static String RECORD_KEY = "record";
    final static String TYPE_KEY = "type";
    final static String RECORD_ARRAY_KEY = "recARR";
    final static String ACTION_KEY = "location_update";
    final static int SLOW_GAME = 0, RIGHT = 4, SLOW_SPEED = 650, FAST_SPEED = 450, WAIT_TIME = 3500, SLOW_TICK = 4000, FAST_TICK = 3000;
    final static int FAST_GAME = 1;
    final static int MOTION_GAME = 2;
    final static int MAX_RECORDS = 10;
    final static int ROWS = 9, MAX_LIFE = 3, BARRIER = 0, COIN = 1, LANES = 5;
    static final String FILE_NAME = "EscapeTheBarrierHIGHSCORES.txt";
    static final String HIGH_SCORES = "HIGH SCORES";


}
