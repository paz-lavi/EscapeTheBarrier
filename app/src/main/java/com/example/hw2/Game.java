package com.example.hw2;


import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game extends AppCompatActivity {

    private double latitude;
    private double longitude;
    private BroadcastReceiver broadcastReceiver;

    private final int LEFT = 0, MAX = 4, MIN = 0;
    private ImageView[][] avatars, coins_views;
    private ImageView[] cars, hearts, counter;
    private ImageView game_IV_rightArrow;
    private ImageView game_IV_leftArrow, game_IV_carshcar;
    private int pos = 2; //startig possition in the middle
    private int score, coins_grabed;
    private int life = 3, gameType = -1, duration_speed, tick_speed;
    private float lastAccelerometerY;
    private boolean continue_game, game_on, go, back_pressed = false, pause = false, first = true, can = false;
    private TextView game_TV_score, game_TV_scorecoin;
    private Queue<ObjectAnimator> aniQ;
    private Queue<ImageView>[] barrier, coins;
    private MediaPlayer sound_coin;
    private MediaPlayer sound_car_crash;
    private SensorManager mSensorManager;
    private Sensor mSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        init();


    }

    /**
     * initialize the activity
     */
    private void init() {
        startGpsService();
        typeOfGame();

        duration_speed = Constants.SLOW_SPEED;
        tick_speed = Constants.SLOW_TICK;
        continue_game = true;
        game_on = false;
        go = true;
        score = 0;
        coins_grabed = 0;
        aniQ = new ConcurrentLinkedQueue<>();
        setBarrierImageView();
        setCoinsImageView();

        sound_car_crash = MediaPlayer.create(Game.this, R.raw.sound_car_crash);
        sound_coin = MediaPlayer.create(Game.this, R.raw.sound_coin);

        counter = new ImageView[]{findViewById(R.id.game_IV_one),
                findViewById(R.id.game_IV_tow),
                findViewById(R.id.game_IV_three)};
        for (int i = 0; i < 3; i++) {
            counter[i].setVisibility(View.INVISIBLE);
        }

        cars = new ImageView[]{findViewById(R.id.game_IV_carLeft), findViewById(R.id.game_IV_carLeftM),
                findViewById(R.id.game_IV_carMid), findViewById(R.id.game_IV_carRightM),
                findViewById(R.id.game_IV_carRight)};
        hearts = new ImageView[]{findViewById(R.id.game_IV_heart1),
                findViewById(R.id.game_IV_heart2),
                findViewById(R.id.game_IV_heart3)};

        game_IV_rightArrow = findViewById(R.id.game_IV_rightArrow);
        game_IV_leftArrow = findViewById(R.id.game_IV_leftArrow);
        game_IV_carshcar = findViewById(R.id.game_IV_carshcar);

        game_TV_score = findViewById(R.id.game_TV_score);
        game_TV_scorecoin = findViewById(R.id.game_TV_scorecoin);

        if (gameType == Constants.MOTION_GAME) {
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            game_IV_leftArrow.setVisibility(View.INVISIBLE);
            game_IV_rightArrow.setVisibility(View.INVISIBLE);
        } else {
            game_IV_leftArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveCarLeft();
                }
            });

            game_IV_rightArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveCarRight();
                }
            });

        }

        showCar();
        heartDraw();
    }

    /**
     * set the car in pos to visible and the rest to invisible
     */
    private void showCar() {
        for (int i = 0; i < cars.length; i++) {

            if (i == pos)
                cars[i].setVisibility(View.VISIBLE);
            else
                cars[i].setVisibility(View.INVISIBLE);
        }
    }

    /**
     * linking all  the coins to the activity and adding them to coins queue array
     */
    private void setCoinsImageView() {
        coins_views = new ImageView[][]{
                {findViewById(R.id.game_IV_coinlfet1), findViewById(R.id.game_IV_coinlfet2),
                        findViewById(R.id.game_IV_coinlfet3), findViewById(R.id.game_IV_coinlfet4),
                        findViewById(R.id.game_IV_coinlfet5), findViewById(R.id.game_IV_coinlfet6),
                        findViewById(R.id.game_IV_coinlfet7), findViewById(R.id.game_IV_coinlfet8),
                        findViewById(R.id.game_IV_coinlfet9)},

                {findViewById(R.id.game_IV_coinlfetm1), findViewById(R.id.game_IV_coinlfetm2),
                        findViewById(R.id.game_IV_coinlfetm3), findViewById(R.id.game_IV_coinlfetm4),
                        findViewById(R.id.game_IV_coinlfetm5), findViewById(R.id.game_IV_coinlfetm6),
                        findViewById(R.id.game_IV_coinlfetm7), findViewById(R.id.game_IV_coinlfetm8),
                        findViewById(R.id.game_IV_coinlfetm9)},

                {findViewById(R.id.game_IV_coinmid1), findViewById(R.id.game_IV_coinmid2),
                        findViewById(R.id.game_IV_coinmid3), findViewById(R.id.game_IV_coinmid4),
                        findViewById(R.id.game_IV_coinmid5), findViewById(R.id.game_IV_coinmid6),
                        findViewById(R.id.game_IV_coinmid7), findViewById(R.id.game_IV_coinmid8),
                        findViewById(R.id.game_IV_coinmid9)},

                {findViewById(R.id.game_IV_coinRightm1), findViewById(R.id.game_IV_coinRightm2),
                        findViewById(R.id.game_IV_coinRightm3), findViewById(R.id.game_IV_coinRightm4),
                        findViewById(R.id.game_IV_coinRightm5), findViewById(R.id.game_IV_coinRightm6),
                        findViewById(R.id.game_IV_coinRightm7), findViewById(R.id.game_IV_coinRightm8),
                        findViewById(R.id.game_IV_coinRightm9)},

                {findViewById(R.id.game_IV_coinRight1), findViewById(R.id.game_IV_coinRight2),
                        findViewById(R.id.game_IV_coinRight3), findViewById(R.id.game_IV_coinRight4),
                        findViewById(R.id.game_IV_coinRight5), findViewById(R.id.game_IV_coinRight6),
                        findViewById(R.id.game_IV_coinRight7), findViewById(R.id.game_IV_coinRight8),
                        findViewById(R.id.game_IV_coinRight9)}
        };

        coins = new ConcurrentLinkedQueue[]{new ConcurrentLinkedQueue<ImageView>(), new ConcurrentLinkedQueue<ImageView>(),
                new ConcurrentLinkedQueue<ImageView>(), new ConcurrentLinkedQueue<ImageView>()
                , new ConcurrentLinkedQueue<ImageView>()};


        for (int i = 0; i < Constants.LANES; i++) {
            for (int j = 0; j < Constants.ROWS; j++) {
                coins_views[i][j].setY(0f);
                coins[i].add(coins_views[i][j]);
            }

        }

    }

    /**
     * linking all  the barrier to the activity and adding them to coins queue array
     */

    private void setBarrierImageView() {
        //barrier image view
        avatars = new ImageView[][]{
                {findViewById(R.id.game_IV_barrierLeft1), findViewById(R.id.game_IV_barrierLeft2),
                        findViewById(R.id.game_IV_barrierLeft3), findViewById(R.id.game_IV_barrierLeft4),
                        findViewById(R.id.game_IV_barrierLeft5), findViewById(R.id.game_IV_barrierLeft6),
                        findViewById(R.id.game_IV_barrierLeft7), findViewById(R.id.game_IV_barrierLeft8),
                        findViewById(R.id.game_IV_barrierLeft9)},

                {findViewById(R.id.game_IV_barrierLeftM1), findViewById(R.id.game_IV_barrierLeftM2),
                        findViewById(R.id.game_IV_barrierLeftM3), findViewById(R.id.game_IV_barrierLeftM4),
                        findViewById(R.id.game_IV_barrierLeftM5), findViewById(R.id.game_IV_barrierLeftM6),
                        findViewById(R.id.game_IV_barrierLeftM7), findViewById(R.id.game_IV_barrierLeftM8),
                        findViewById(R.id.game_IV_barrierLeftM9)},

                {findViewById(R.id.game_IV_barrierMid1), findViewById(R.id.game_IV_barrierMid2),
                        findViewById(R.id.game_IV_barrierMid3), findViewById(R.id.game_IV_barrierMid4),
                        findViewById(R.id.game_IV_barrierMid5), findViewById(R.id.game_IV_barrierMid6),
                        findViewById(R.id.game_IV_barrierMid7), findViewById(R.id.game_IV_barrierMid8),
                        findViewById(R.id.game_IV_barrierMid9)},

                {findViewById(R.id.game_IV_barrierRightM1), findViewById(R.id.game_IV_barrierRightM2),
                        findViewById(R.id.game_IV_barrierRightM3), findViewById(R.id.game_IV_barrierRightM4),
                        findViewById(R.id.game_IV_barrierRightM5), findViewById(R.id.game_IV_barrierRightM6),
                        findViewById(R.id.game_IV_barrierRightM7), findViewById(R.id.game_IV_barrierRightM8),
                        findViewById(R.id.game_IV_barrierRightM9)},

                {findViewById(R.id.game_IV_barrierRight1), findViewById(R.id.game_IV_barrierRight2),
                        findViewById(R.id.game_IV_barrierRight3), findViewById(R.id.game_IV_barrierRight4),
                        findViewById(R.id.game_IV_barrierRight5), findViewById(R.id.game_IV_barrierRight6),
                        findViewById(R.id.game_IV_barrierRight7), findViewById(R.id.game_IV_barrierRight8),
                        findViewById(R.id.game_IV_barrierRight9)}
        };
        barrier = new ConcurrentLinkedQueue[]{new ConcurrentLinkedQueue<ImageView>(), new ConcurrentLinkedQueue<ImageView>(),
                new ConcurrentLinkedQueue<ImageView>(), new ConcurrentLinkedQueue<ImageView>()
                , new ConcurrentLinkedQueue<ImageView>()};


        for (int i = 0; i < Constants.LANES; i++) {
            for (int j = 0; j < Constants.ROWS; j++) {
                barrier[i].add(avatars[i][j]);
            }

        }

    }

    /**
     * set type of game. 0 for slow speed , 1 for fast speed
     */
    private void typeOfGame() {
        Bundle b = getIntent().getExtras();
        if (b != null)
            gameType = b.getInt(Constants.TYPE_KEY);
    }

    /**
     * on back pressed set flag "back_pressed" to true. Ensure that the game is stopped does not conflict with a new game
     */
    @Override
    public void onBackPressed() {
        back_pressed = true;
        finish();
        super.onBackPressed();
    }


    /**
     * move the car 1 position left. if already at the leftest position this method has no effect
     */
    private void moveCarLeft() {
        if (pos != LEFT) {
            pos--;
            for (int i = 0; i < Constants.LANES; i++) {
                if (i == pos) {
                    cars[pos].setVisibility(View.VISIBLE);
                } else {
                    cars[i].setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
     * move the car 1 position right. if already at the rightest position this method has no effect
     */
    private void moveCarRight() {
        if (pos != Constants.RIGHT) {
            pos++;
            for (int i = 0; i < Constants.LANES; i++) {
                if (i == pos) {
                    cars[pos].setVisibility(View.VISIBLE);
                } else {
                    cars[i].setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
     * set all barrier to invisible
     */
    private void clear() {
        for (int i = 0; i < Constants.LANES; i++) {
            for (int j = 0; j < Constants.ROWS; j++) {
                avatars[i][j].setVisibility(View.INVISIBLE);
                coins_views[i][j].setVisibility(View.INVISIBLE);
            }
        }
        game_IV_carshcar.setVisibility(View.INVISIBLE);
    }


    /**
     * @param col  number of the lane to show the object.
     * @param type the type of the object. 1 - coin , 0 - Barrier
     *             creating and starting animation of coin or barrier . handling animation start , end and cancel
     */
    private void startAnim(final int col, final int type) {
        if (!continue_game || !go || back_pressed || pause)
            return;

        final ImageView iv;
        if (type == Constants.COIN) {

            iv = coins[col].poll();
        } else {

            iv = barrier[col].poll();
        }

        final float c = cars[1].getY();
        final ObjectAnimator animator = ObjectAnimator.ofFloat(iv, "y", -100, c - 30);
        animator.setDuration(setDuration());
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (iv != null) {
                    iv.setVisibility(View.VISIBLE);
                    aniQ.add(animator);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (pause) {

                    iv.setVisibility(View.INVISIBLE);
                    return;
                }
                if (continue_game && game_on) {
                    if (type == Constants.COIN) {
                        if (!grabCoin(col, pos, iv)) {
                            endAnim(iv);
                        }
                        coins[col].add(iv);


                    } else {
                        if (continueGame(col)) {
                            endAnim(iv);
                        }

                        barrier[col].add(iv);
                    }

                } else {
                    return;
                }
                aniQ.remove(animator);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (type == Constants.COIN) {
                    coins[col].add(iv);
                } else {
                    barrier[col].add(iv);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    /**
     * @param col      the number of the lane to check
     * @param position the position of the car
     * @param iv       the ImageView of the coin
     * @return true if the coin collected , false if not
     * check if the coin collected by the car. if collected playing a sound, updating the counter and send iv to collect animation
     */
    private boolean grabCoin(int col, int position, ImageView iv) {
        if (col == position) {
            game_TV_scorecoin.setText("" + ++coins_grabed);
            sound_coin.seekTo(0);
            sound_coin.start();
            coinInvisible(iv);
            return true;
        }
        return false;
    }

    /**
     * @param iv ImageView for animation
     *           fade out a ImageView from the screen
     */
    private void endAnim(final ImageView iv) {


        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_finish);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                iv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        iv.startAnimation(animation);
    }


    /**
     * this method calling herself and scrolls a random numbers used as number of lane to show am object and the type of the object.
     */
    private void loopFunc4() {
        if (!continue_game || back_pressed || pause)
            return;


        final Handler handler = new Handler();
        Runnable myRun = new Runnable() {
            @Override
            public void run() {

                Stack<Integer> stack = new Stack<>();
                int r;
                int rep = (int) ((Math.random() * ((3 - 1) + 1)) + 1);
                boolean[] selected = {false, false, false, false, false};
                for (int i = 0; i < rep; i++) {
                    do {
                        r = rand();
                    }
                    while (selected[r]);
                    selected[r] = true;
                    stack.push(r);
                }
                while (!stack.empty()) {
                    if ((((int) ((Math.random() * 2)))) == Constants.COIN) {
                        startAnim(stack.pop().intValue(), Constants.COIN);

                    } else {
                        startAnim(stack.pop().intValue(), Constants.BARRIER);
                    }

                }

                loopFunc4();


            }
        };
        handler.postDelayed(
                myRun, setTick());

    }

    /**
     * scrolls a random numbers between MIN and MAX
     */
    private int rand() {
        return (int) ((Math.random() * ((MAX - MIN) + 1)) + MIN);
    }


    /**
     * This method checks whether the car collided with a barrier. If yes set a boolean flag to stop the game.
     * In this case we will move to another method to resume the game.
     *
     * @param col - The column number to check against the car's location
     */
    private boolean continueGame(int col) {
        if (col == pos && !back_pressed) {
            MySignal.vibrate(Game.this, 500);
            sound_car_crash.seekTo(0);
            sound_car_crash.start();
            carAnimation();
            continue_game = false;
            game_on = false;
            if (life >= 0)
                heartInvisible(life);
            pauseAnimations();
            waitForAll();

            return false;
        } else {
            score++;
            game_TV_score.setText("" + score);
        }
        return true;
    }

    private void pauseAnimations() {
        can = true;
        while (!aniQ.isEmpty()) {
            aniQ.poll().cancel();
        }

    }


    /***
     * this method used to start resume and finish the game.
     * If there is no life left to play the method will switch to the next activity.
     * If there is life left, we will resume the game.  */
    private void play() {

        if (!game_on) {

            life--;

            clear();
            if (life == -1) {
                if (back_pressed) {
                    finish();
                    return;
                }
                Intent myIntent = new Intent(Game.this, GameOver.class);
                Bundle b = new Bundle();
                b.putInt(Constants.SCORE_KEY, score);
                b.putInt(Constants.COIN_KEY, coins_grabed);
                b.putDouble(Constants.LATITUDE_KEY, latitude);
                b.putDouble(Constants.LONGITUDE_KEY, longitude);
                myIntent.putExtras(b);
                Game.this.startActivity(myIntent);
                finish();
                return;
            }
            for (int i = 3; i >= 0; i--) {
                countDown(i);
            }
            continue_game = true;
            game_on = true;
            loopFunc4();


        }

    }

    /**
     * This method will display a 3-second countdown on the screen.
     * Holds a boolean flag that signals when the countdown is over and the game can be started
     *
     * @param i - the number that need to show on the screen
     */
    private void countDown(final int i) {

        go = false;
        int time = 0;
        switch (i) {
            case 0:
                time = 4000;
                break;
            case 1:
                time = 3000;
                break;
            case 2:
                time = 2000;
                break;
            case 3:
                time = 1000;
                break;
            default:
                break;
        }

        final Handler handler = new Handler();
        final Runnable myRun = new Runnable() {
            @Override
            public void run() {

                if (i == 0) {
                    go = true;
                    return;
                }
                counterAnimation(i);
            }
        };
        handler.postDelayed(
                myRun, time);


    }

    /**
     * This method pauses the game, lets all the commands in the background end and synchronize
     * that the game is stopped and then calls another method to start a new game
     */
    private void waitForAll() {
        final Handler handler = new Handler();
        Runnable myRun = new Runnable() {
            @Override
            public void run() {
                play();
            }
        };
        handler.postDelayed(
                myRun, Constants.WAIT_TIME);

    }

    /**
     * This method sets all hearts to visible
     */
    private void heartDraw() {
        for (int i = 0; i < Constants.MAX_LIFE; i++) {
            hearts[i].setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param coin - the collected coin that should be invisible
     *             animation for collected coin
     */
    private void coinInvisible(final ImageView coin) {
        coin.setY(cars[1].getY());
        coin.setScaleY(1);
        coin.setScaleX(1);
        coin.setRotation(0);
        coin.animate()
                .scaleX(0).scaleY(0)
                .rotation(360).setDuration(1000).setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        coin.setVisibility(View.INVISIBLE);
                        coin.setScaleY(1);
                        coin.setScaleX(1);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                //just wanted to show you possible methods you can add more
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    /**
     * @param i - index for the heart that should be invisible
     *          animation for setting a heart ImageView invisible
     */
    private void heartInvisible(final int i) {
        hearts[i].setScaleY(1);
        hearts[i].setScaleX(1);
        hearts[i].setRotation(0);
        hearts[i].animate()
                .scaleY(0)
                .scaleX(0)
                .rotation(360).setDuration(4000).setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hearts[i].setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                //just wanted to show you possible methods you can add more
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    /**
     * animation to show after the car collided with a barrier
     */
    private void carAnimation() {
        game_IV_carshcar.setVisibility(View.VISIBLE);
        game_IV_carshcar.setScaleY(1);
        game_IV_carshcar.setScaleX(1);
        game_IV_carshcar.setRotation(0);
        game_IV_carshcar.animate()
                .scaleY(1)
                .scaleX(1)
                .rotation(180).setDuration(4000).setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                //just wanted to show you possible methods you can add more
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }


    /**
     * @param i - the number to show
     *          animate a number on the screen
     */
    private void counterAnimation(final int i) {
        counter[i - 1].setVisibility(View.VISIBLE);
        counter[i - 1].setScaleY(0);
        counter[i - 1].setScaleX(0);
        counter[i - 1].animate()
                .scaleY(2)
                .scaleX(2)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        counter[i - 1].setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                //just wanted to show you possible methods you can add more
                .setDuration(1000)
                .start();
    }

    /**
     * on stop pause all active animation and signal the game is stop
     */
    @Override
    protected void onStop() {
        super.onStop();
        pause = true;
        ConcurrentLinkedQueue temp_queue_anim = new ConcurrentLinkedQueue<ObjectAnimator>();
        ObjectAnimator o;

        while (!aniQ.isEmpty()) {
//
            o = aniQ.poll();
            o.pause();
            temp_queue_anim.add(o);
        }
        aniQ = temp_queue_anim;
    }


    /**
     * first time called - start a new game
     * else continue the game from the last state.
     * resume all the paused animation and signal the game is on
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (first) {

            first = false;
            pause = false;
            play();
        } else {
            ConcurrentLinkedQueue temp_queue_anim = new ConcurrentLinkedQueue<ObjectAnimator>();
            ObjectAnimator o;

            while (!aniQ.isEmpty()) {
                o = aniQ.poll();
                o.resume();
                temp_queue_anim.add(o);

            }
            aniQ = temp_queue_anim;
            pause = false;
            loopFunc4();

        }
    }

    /**
     * on movement game register sensor listener
     * register broadcast Receiver for gps update
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (gameType == Constants.MOTION_GAME)
            mSensorManager.registerListener(sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_UI);

        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    latitude = (double) intent.getExtras().get(Constants.LATITUDE_KEY);
                    longitude = (double) intent.getExtras().get(Constants.LONGITUDE_KEY);

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION_KEY));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    /**
     * start a gps service
     */
    private void startGpsService() {
        if (!runtime_permissions()) {
            Intent i = new Intent(getApplicationContext(), GPS_Service.class);
            startService(i);
        }

    }

    /**
     * request gps permissions on runtime
     */
    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startGpsService();
            } else {
                runtime_permissions();
            }
        }
    }


    /**
     * on movement mode unregister sensor listener
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (gameType == Constants.MOTION_GAME)
            mSensorManager.unregisterListener(sensorEventListener);
    }


    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            sensorChangedHandel(event);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * handling  sensor changed .
     * setting the car position depending the phone tilt to the side.
     * updating animation duration (in/decrease game speed) depending the phone tilt to forth and back
     */
    private void sensorChangedHandel(SensorEvent event) {
        float temp = Math.abs(lastAccelerometerY) * 1000000000 - Math.abs(event.values[1]) * 1000000000;
        int delta = (int) temp;
        if (event.values[0] > -2f && event.values[0] < 2f && pos != 2)
            setPos(2);
        else if (event.values[0] > 2f && event.values[0] < 6f && pos != 1)
            setPos(1);
        else if (event.values[0] > 6f && pos != 0)
            setPos(0);
        else if (event.values[0] < -2f && event.values[0] > -6f && pos != 3)
            setPos(3);
        else if (event.values[0] < -6f && pos != 4)
            setPos(4);
        if (event.values[1] > 8f && duration_speed < 6000 && delta > 0) {
            tick_speed += 10;
            duration_speed += 50;
        } else if (event.values[1] < 4.5f && duration_speed > 2000 && delta < 0) {
            tick_speed -= 10;
            duration_speed -= 50;
        }

        lastAccelerometerY = event.values[1];

        ObjectAnimator[] arr = new ObjectAnimator[aniQ.size()];
        aniQ.toArray(arr);
        for (ObjectAnimator r : arr) {
            r.setDuration(setDuration());
        }
    }

    /**
     * set car position at movement mode
     */
    private void setPos(int posToSet) {

        for (int i = 0; i < Constants.LANES; i++) {
            pos = posToSet;
            if (i == posToSet) {
                cars[posToSet].setVisibility(View.VISIBLE);
            } else {
                cars[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * @return duration time for animation depending the game mode
     */
    private int setDuration() {
        if (gameType == Constants.MOTION_GAME) {
            return duration_speed;
        }
        return gameType == Constants.FAST_GAME ? Constants.FAST_SPEED : Constants.SLOW_SPEED;
    }

    /**
     * @return tick time for new animations post delay depending the game mode
     */
    private int setTick() {
        if (gameType == Constants.MOTION_GAME)
            return tick_speed;
        return gameType == Constants.FAST_GAME ? Constants.FAST_TICK : Constants.SLOW_TICK;

    }
}



