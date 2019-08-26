//Tom Boukai
//307929075
package com.project.mainactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private TextView nameText;
    private TextView scoreTextView;
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        nameText = findViewById(R.id.nameText);
        scoreTextView = findViewById(R.id.scoreTextView);
        startBtn = findViewById(R.id.startBtn);
        nameText.setText(sharedPreferences.getString("name", "Tom"));
        scoreTextView.setText("Score: " + sharedPreferences.getInt("score", 0));
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}