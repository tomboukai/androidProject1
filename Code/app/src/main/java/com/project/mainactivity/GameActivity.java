//Tom Boukai
//307929075
package com.project.mainactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private CountDownTimer countDownTimer;
    private final int LIVES = 3;
    private final int DELAY_TIME = 3;
    private SharedPreferences sharedPreferences;
    private int points;
    private int lives;
    private int delay;

    private TextView scoreTxt;
    private TextView timerTxt;
    private TextView livesTxt;

    private GridLayout gridLayout;
    private Button[] buttons = new Button[9];
    int[] images;
    Drawable[] imagesD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        init();
        timer();
    }


    void init() {

        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        images = new int[4];
        imagesD = new Drawable[4];

        images[0] = R.drawable.default_button;
        images[1] = R.drawable.mole_button;
        images[2] = R.drawable.hit_button;
        images[3] = R.drawable.miss_button;

        imagesD[0] = getResources().getDrawable(R.drawable.default_button);
        imagesD[1] = getResources().getDrawable(R.drawable.mole_button);
        imagesD[2] = getResources().getDrawable(R.drawable.hit_button);
        imagesD[3] = getResources().getDrawable(R.drawable.miss_button);

        scoreTxt = findViewById(R.id.scoreText);
        timerTxt = findViewById(R.id.timerText);
        livesTxt = findViewById(R.id.livesText);

        gridLayout = findViewById(R.id.gridLayout);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            buttons[i] = (Button) gridLayout.getChildAt(i);
            buttons[i].setOnClickListener(this);
        }
        points = 0;
        lives = LIVES;
        delay = DELAY_TIME;

        livesTxt.setText("Lives: " + lives);
        scoreTxt.setText("Score: " + points);
    }

    private void timer() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerTxt.setText(millisUntilFinished / 1000 + "s");
                if (delay == 0)
                    makeMole();
                else
                    delay--;
            }

            public void onFinish() {
                timerTxt.setText("Time's up");
                gameOver();
            }
        }.start();
    }

    private void makeMole() {
        delay = DELAY_TIME;
        Random rand = new Random();
        int num = rand.nextInt(buttons.length - 1);
        while (true) {
            if (!checkBackgroundMole(buttons[num])) {
                buttons[num].setBackgroundResource(images[1]);
                break;
            } else
                num = rand.nextInt(buttons.length - 1);
        }

        final int theNum = num;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkBackgroundMole(buttons[theNum]))
                    buttons[theNum].setBackgroundResource(images[0]);
            }
        }, 3000);
    }

    @Override
    public void onClick(View v) {

        Button button = findViewById(v.getId());
        if (checkBackgroundDefault(button)) {
            button.setBackgroundResource(images[3]);
            lives--;
            livesTxt.setText("Lives: " + lives);
            if (lives == 0)
                gameOver();

            final Button b = button;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (checkBackgroundMiss(b))
                        b.setBackgroundResource(images[0]);
                }
            }, 3000);
        } else if (checkBackgroundMole(button)) {
            button.setBackgroundResource(images[2]);
            points += 100;
            scoreTxt.setText("Score: " + points);
            final Button b = button;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (checkBackgroundHit(b))
                        b.setBackgroundResource(images[0]);
                }
            }, 3000);
        }
    }

    private void gameOver() {
        sharedPreferences.edit().putInt("score", points).apply();
        Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
        startActivity(intent);
        countDownTimer.cancel();
        finish();
    }

    boolean checkBackgroundDefault(Button b) {
        return (b.getBackground().getConstantState() == getResources().getDrawable(images[0]).getConstantState());
    }

    boolean checkBackgroundMole(Button b) {
        return (b.getBackground().getConstantState() == getResources().getDrawable(images[1]).getConstantState());
    }

    boolean checkBackgroundHit(Button b) {
        return (b.getBackground().getConstantState() == getResources().getDrawable(R.drawable.hit_button).getConstantState());
    }

    boolean checkBackgroundMiss(Button b) {
        return (b.getBackground().getConstantState() == getResources().getDrawable(R.drawable.miss_button).getConstantState());
    }
}