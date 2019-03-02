package com.example.beatmatch;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import java.util.Timer;

public class MainActivity extends AppCompatActivity{

    private static final String CLIENT_ID = "0cdf3e0de4db494c8632548161915b48";
    private static final String REDIRECT_URI = "beatmatch://callback";
    private SpotifyAppRemote mSpotifyAppRemote;


    private Button mPause;
    private Button mPlay;
    private Button mStop;
    private Chronometer chronometer;
    private Button mPrev;
    private Button mNext;

    //I added this

    private FireBase mFireBase = new FireBase();
    private String url;

    private View myView;
    private Sensor mySensor;
    private SensorManager mySM;
    private TextView xValue, yValue, zValue, timeValue, infoText, LastStepValue, TempoValue, LoopTimeValue;
    private WindowManager myWM;
    private Timer timer;
    private long pauseOffset;
    private boolean running;
    private boolean firstValue;
    private Long stepTimes[];
    private int step = 0;
    private Long startTime;
    private boolean readyForStep = true;
    private long firstStep;
    private long BPM;




     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                url = mFireBase.getBPM(Long.toString(BPM));


            }

            @Override
            public void onFinish() {
//                url = mFireBase.getBPM("108");
//                Log.e("scrubs", "\""+url+"\"");
                if(url==null){
                    mSpotifyAppRemote.getPlayerApi().play(mFireBase.getBPM("108"));
                }



            }
        }.start();

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(MainActivity.this, "Bing!", Toast.LENGTH_SHORT).show();
                    new CountDownTimer(10000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {


                        }

                        @Override
                        public void onFinish() {
//                            url = mFireBase.getBPM("117");
//                            Log.e("scrubs", "\""+url+"\"");
//                            mSpotifyAppRemote.getPlayerApi().play("spotify:track:75aLTVBSGIquqzQ6AkmK3Q");

                            url = mFireBase.getBPM(Long.toString(BPM));

                            if(url!=null){
                                mSpotifyAppRemote.getPlayerApi().play(url);
                            }
                            else

                                mSpotifyAppRemote.getPlayerApi().play(mFireBase.getBPM("117"));


//

                        }
                    }.start();
                }
            }
        });




        mPlay = (Button) findViewById(R.id.play_button);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                mSpotifyAppRemote.getPlayerApi().resume();

                //I added this.
//                url = mFireBase.getBPM("102");
//                Log.e("scrubs", "\""+url+"\"");

            }
        });
        mPause = (Button) findViewById(R.id.pause_button);
        mPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mSpotifyAppRemote.getPlayerApi().pause();
            }
        });
        mPrev = (Button) findViewById(R.id.prevbtn);
        mPrev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mSpotifyAppRemote.getPlayerApi().skipPrevious();
            }
        });
        mNext = (Button) findViewById(R.id.nextbtn);
        mNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mSpotifyAppRemote.getPlayerApi().skipNext();
            }
        });
        mStop = (Button) findViewById(R.id.stopbtn);
        mStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mSpotifyAppRemote.getPlayerApi().pause();
                startActivity (new Intent(MainActivity.this, HomePage.class));
            }
        });
    }

    public void onSensorChanged(SensorEvent sensorEvent){

        if (firstValue == true) {
            firstValue = false;
            startTime = System.currentTimeMillis();
        }
        Long currentTime = System.currentTimeMillis();
        //shows current time
        timeValue.setText("Time: " + currentTime);
        //accelerometer things
        xValue.setText("X: " + sensorEvent.values[0]);
        yValue.setText("Y: " + sensorEvent.values[1]);
        zValue.setText("Z: " + sensorEvent.values[2]);
        //time of last step detected


        if (readyForStep == true) {
            //Log.e("scrubs", "for sensorevent.values[1]");

            if (sensorEvent.values[1] > 12.0) {

                Log.e("scrubs", "Before lastsetpvalue");


                Log.e("scrubs", "after lastsetpvalue");

                stepTimes[step % 5] = currentTime;

                Log.e("scrubs", "after step times");
                if (step == 0) {
                    firstStep = currentTime;
                }
                step++;
                readyForStep = false;
            } else {
            }
        } else {
            if (sensorEvent.values[1] > 12.0) {

            } else {
                readyForStep = true;
            }
        }


        if (step > 6) {
            long fiveStepAvg = stepTimes[(step - 1) % 5] - stepTimes[step % 5];
            BPM = 60000 / (fiveStepAvg / 5);
            LastStepValue.setText("Last Steps: " + (stepTimes[step % 5] - firstStep) + ", " + (stepTimes[(step - 1) % 5] - firstStep) + ", " + (stepTimes[(step - 2) % 5] - firstStep) + ", " + (stepTimes[(step - 3) % 5] - firstStep) + ", " + (stepTimes[(step - 4) % 5] - firstStep));
            //LastStepValue.setText("Last Steps: " + (stepTimes[step % 5] - firstStep));
            TempoValue.setText("Tempo: " + BPM);
        }

        url = mFireBase.getBPM("108");
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


}
