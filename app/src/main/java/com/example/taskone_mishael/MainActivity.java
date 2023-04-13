package com.example.taskone_mishael;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final int NUM_OF_LANES = 3;
    public static final int NUM_OF_ROWS = 6;
    public static final int CAR_LEFT_LANE = 0;
    public static final int CAR_CENTER_LANE = 1;
    public static final int CAR_RIGHT_LANE = 2;
    public static final int NUM_OF_LIFE = 3;
    public static final int DELAY = 1000;
    private AppCompatImageView main_IMG_background;
    private ShapeableImageView [] main_IMG_hearts;
    private ShapeableImageView [][] main_IMG_rocks;
    private ShapeableImageView [] main_IMG_car;
    private FloatingActionButton main_BTN_left;
    private FloatingActionButton main_BTN_right;
    private AlertDialog.Builder builder;
    private int numOfCrashes;
    private CountDownTimer gameTimer;
    private Vibrator vibrator;
    public Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        builder = new AlertDialog.Builder(this);
        findViews();
        setBackground();
        setKeyboard();
        startTime();
    }

    private void setBackground() {
        Glide
                .with(this)
                .load(R.drawable.main_img_road)
                .centerCrop()
                .into(main_IMG_background);
    }

    private void findViews() {
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};

        main_IMG_rocks = new ShapeableImageView[NUM_OF_ROWS][NUM_OF_LANES];
        main_IMG_rocks[0][0] = findViewById(R.id.main_IMG_rock_00);
        main_IMG_rocks[0][1] = findViewById(R.id.main_IMG_rock_01);
        main_IMG_rocks[0][2] = findViewById(R.id.main_IMG_rock_02);

        main_IMG_rocks[1][0] = findViewById(R.id.main_IMG_rock_10);
        main_IMG_rocks[1][1] = findViewById(R.id.main_IMG_rock_11);
        main_IMG_rocks[1][2] = findViewById(R.id.main_IMG_rock_12);

        main_IMG_rocks[2][0] = findViewById(R.id.main_IMG_rock_20);
        main_IMG_rocks[2][1] = findViewById(R.id.main_IMG_rock_21);
        main_IMG_rocks[2][2] = findViewById(R.id.main_IMG_rock_22);

        main_IMG_rocks[3][0] = findViewById(R.id.main_IMG_rock_30);
        main_IMG_rocks[3][1] = findViewById(R.id.main_IMG_rock_31);
        main_IMG_rocks[3][2] = findViewById(R.id.main_IMG_rock_32);

        main_IMG_rocks[4][0] = findViewById(R.id.main_IMG_rock_40);
        main_IMG_rocks[4][1] = findViewById(R.id.main_IMG_rock_41);
        main_IMG_rocks[4][2] = findViewById(R.id.main_IMG_rock_42);

        main_IMG_rocks[5][0] = findViewById(R.id.main_IMG_rock_50);
        main_IMG_rocks[5][1] = findViewById(R.id.main_IMG_rock_51);
        main_IMG_rocks[5][2] = findViewById(R.id.main_IMG_rock_52);

        main_IMG_car = new ShapeableImageView[NUM_OF_LANES];
        main_IMG_car [0] = findViewById(R.id.main_IMG_car_left);
        main_IMG_car [1] = findViewById(R.id.main_IMG_car_center);
        main_IMG_car [2] = findViewById(R.id.main_IMG_car_right);

        main_BTN_left = findViewById(R.id.main_FAB_left);
        main_BTN_right = findViewById(R.id.main_FAB_right);

        main_IMG_background = findViewById(R.id.main_IMG_background);

        rand = new Random(NUM_OF_LANES + 1);

        numOfCrashes = 0;
    }

    private void setKeyboard(){
        main_BTN_left.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                    moveLeft(v);
                    return true;
                }
                return false;
            }
        });

        main_BTN_right.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                    moveRight(v);
                    return true;
                }
                return false;
            }
        });
    }

    public void moveRight(View view) {
        if (main_IMG_car[CAR_LEFT_LANE].getVisibility() == VISIBLE){
            main_IMG_car[CAR_LEFT_LANE].setVisibility(INVISIBLE);
            main_IMG_car[CAR_CENTER_LANE].setVisibility(VISIBLE);
        }

        else if (main_IMG_car[CAR_CENTER_LANE].getVisibility() == VISIBLE) {
            main_IMG_car[CAR_CENTER_LANE].setVisibility(INVISIBLE);
            main_IMG_car[CAR_RIGHT_LANE].setVisibility(VISIBLE);
        }
    }

    public void moveLeft(View view) {
        if (main_IMG_car[CAR_RIGHT_LANE].getVisibility() == VISIBLE){
            main_IMG_car[CAR_RIGHT_LANE].setVisibility(INVISIBLE);
            main_IMG_car[CAR_CENTER_LANE].setVisibility(VISIBLE);
        }

        else if (main_IMG_car[CAR_CENTER_LANE].getVisibility() == VISIBLE) {
            main_IMG_car[CAR_CENTER_LANE].setVisibility(INVISIBLE);
            main_IMG_car[CAR_LEFT_LANE].setVisibility(VISIBLE);
        }
    }

    private void startTime() {
        if (gameTimer == null) {
            gameTimer = new CountDownTimer(999999999, DELAY) {
                @Override
                public void onTick(long millisUntilFinished) {
                    removeLastLine();
                    newRock();
                    moveDown();
                    checkCrash();
                }

                @Override
                public void onFinish() {
                    gameTimer.cancel();
                }
            }.start();
        }
    }

    private void stopTime() {
        gameTimer.cancel();
    }

    private void checkCrash() {
        for(int i = 0;i < NUM_OF_LANES; i++){
            if(main_IMG_car[i].getVisibility() == main_IMG_rocks[NUM_OF_ROWS-1][i].getVisibility()
            && main_IMG_car[i].getVisibility() == VISIBLE){
                addCrash();
            }
        }
    }

    private void addCrash() {
        if(numOfCrashes < NUM_OF_LIFE){
            main_IMG_hearts[numOfCrashes].setVisibility(INVISIBLE);
            numOfCrashes++;
            Toast.makeText(getApplicationContext(), NUM_OF_LIFE - numOfCrashes +" Hearts Left", Toast.LENGTH_SHORT).show();
            makeVibrate();
        }

        else{
            Toast.makeText(getApplicationContext(), "Game Over!", Toast.LENGTH_SHORT).show();
            gameOver();
        }
    }

    private void gameOver() {
        stopTime();
        builder.setMessage("Press Start to try again")
                .setCancelable(false)
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        newGame();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Game Over!");
        alert.show();

    }

    private void newGame() {
        /*Reset All Rocks*/
        for(int i = 0; i < NUM_OF_ROWS;i++)
            for(int j = 0;j < NUM_OF_LANES;j++)
                main_IMG_rocks[i][j].setVisibility(INVISIBLE);

        /*Reset All Car*/
        for(int i = 0;i < NUM_OF_LANES; i++)
            main_IMG_car[i].setVisibility(INVISIBLE);

        /*Reset All Heart*/
        for(int i = 0; i < NUM_OF_LIFE; i++)
            main_IMG_hearts[i].setVisibility(VISIBLE);

        main_IMG_car[CAR_CENTER_LANE].setVisibility(VISIBLE);

        numOfCrashes = 0;
        gameTimer.start();
    }

    private void makeVibrate() {
        final VibrationEffect vibrationEffect1;

        // this is the only type of the vibration which requires system version Oreo (API 26)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // this effect creates the vibration of default amplitude for 1000ms(1 sec)
            vibrationEffect1 = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);

            // it is safe to cancel other vibrations currently taking place
            vibrator.cancel();
            vibrator.vibrate(vibrationEffect1);
        }
    }

    private void removeLastLine() {
        for(int i = 0; i < NUM_OF_LANES; i++)
        {
            main_IMG_rocks[NUM_OF_ROWS - 1][i].setVisibility(INVISIBLE);
        }
    }

    private void moveDown() {
        for(int i = NUM_OF_ROWS - 1;i > 0 ; i--) {
            for(int j = NUM_OF_LANES - 1; j >= 0 ;j--){
                if(main_IMG_rocks[i-1][j].getVisibility() == VISIBLE){
                    main_IMG_rocks[i-1][j].setVisibility(INVISIBLE);
                    main_IMG_rocks[i][j].setVisibility(VISIBLE);
                }
            }
        }
    }

    public void newRock(){
        int randLane = rand.nextInt(NUM_OF_LANES+1);// 0-4
        if (randLane != NUM_OF_LANES)
            main_IMG_rocks[0][randLane].setVisibility(VISIBLE);
    }
}