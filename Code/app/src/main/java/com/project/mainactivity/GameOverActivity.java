//Tom Boukai
//307929075
package com.project.mainactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class GameOverActivity extends AppCompatActivity {

    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private SharedPreferences sharedPreferences;
    private TextView nameText;
    private TextView scoreTextView;
    private Button startBtn;
    private TableLayout table;
    private double lon;
    private double lat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        init();
        fillTable();
    }

    private void fillTable() {

        CollectionReference collectionReference = db.collection("LeaderBoards");
        Query query;
        query = collectionReference.orderBy("score", Query.Direction.DESCENDING).limit(10);
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        TableRow tableRow = new TableRow(getApplicationContext());

                        TextView name = new TextView(getApplicationContext());
                        name.setPadding(25, 10, 10, 0);
                        name.setTextSize(20);
                        name.setTextColor(Color.WHITE);
                        name.setText(documentSnapshot.get("name").toString());
                        TextView score = new TextView(getApplicationContext());
                        score.setText(documentSnapshot.get("score").toString());
                        score.setPadding(25, 10, 10, 0);
                        score.setTextSize(20);
                        score.setTextColor(Color.WHITE);

                        tableRow.addView(name);
                        tableRow.addView(score);
                        tableRow.setOnClickListener(v -> {
                            lon = documentSnapshot.getDouble("lon");
                            lat = documentSnapshot.getDouble("lat");
                        });
                        table.addView(tableRow);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show();
                });
    }

    private void init() {
        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        nameText = findViewById(R.id.nameText);
        scoreTextView = findViewById(R.id.scoreTextView);
        startBtn = findViewById(R.id.startBtn);
        table = findViewById(R.id.table);
        nameText.setText(sharedPreferences.getString("name", "Tom"));
        scoreTextView.setText("Score: " + sharedPreferences.getInt("score", 0));
        startBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}