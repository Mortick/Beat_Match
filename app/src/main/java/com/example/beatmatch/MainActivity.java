package com.example.beatmatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "0cdf3e0de4db494c8632548161915b48";
    private static final String REDIRECT_URI = "beatmatch://callback";
    private SpotifyAppRemote mSpotifyAppRemote;




    private Button mPause;
    private Button mPlay;
    private Button accelerometerButton;
    private Chronometer chronometer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        //chronometer.setBase(SystemClock.elapsedRealtime());
        /*
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(MainActivity.this, "Bing!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        new CountDownTimer(10000,1000) {
            Timer timer = new Timer();
            Long startTime = System.currentTimeMillis();
            @Override
            public void onTick(long millisUntilFinished) {

                chronometer.setFormat("Time: %s, " + startTime);
                startTime = System.currentTimeMillis();

            }

            @Override
            public void onFinish() {
                //mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

            }
        }.start();


        mPlay = (Button) findViewById(R.id.play_button);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                //mSpotifyAppRemote.getPlayerApi().resume();
                Intent accelerometerIntent = new Intent(MainActivity.this, AccelerometerSensor.class);
                startActivity(accelerometerIntent);
            }


        });
        mPause = (Button) findViewById(R.id.pause_button);
        mPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mSpotifyAppRemote.getPlayerApi().pause();
            }
        });
        accelerometerButton = (Button) findViewById(R.id.accelerometerButtonId);
        accelerometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accelerometerIntent = new Intent(MainActivity.this, AccelerometerSensor.class);
                startActivity(accelerometerIntent);
            }
        });
}

    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();


        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener()
                {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected()
    {
        // Play a playlist
        //mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    /******
     *
     *
     *
     *
     */

//    public void basicReadWrite() {
//        // [START write_message]
//        // Write a message to the database
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Hello, World!");
//        // [END write_message]
//
//        // [START read_message]
//        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("stuff", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("stuff", "Failed to read value.", error.toException());
//            }
//        });
//        // [END read_message]
//    }

}
