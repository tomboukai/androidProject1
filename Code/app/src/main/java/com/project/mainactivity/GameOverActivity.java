//Tom Boukai
//307929075
package com.project.mainactivity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class GameOverActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Map
    private GoogleMap mMap;
    private MapView mMapView;
    LatLng location = new LatLng(0, 0);
    private double lon;
    private double lat;

    private SharedPreferences sharedPreferences;
    private TextView nameText;
    private TextView scoreTextView;
    private Button startBtn;
    private TableLayout table;


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
                            location = new LatLng(documentSnapshot.getDouble("lon"), documentSnapshot.getDouble("lat"));
                            mMap.addMarker(new MarkerOptions().position(location)
                                    .title(name.getText().toString() + "'s Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        });
                        table.addView(tableRow);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show();
                });
    }

    private void init() {
        location = new LatLng(0, 0);
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

        initMap();
    }

    private void initMap() {

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(null);
        mMapView.onResume();
        mMapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}