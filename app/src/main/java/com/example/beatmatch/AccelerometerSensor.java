//package com.example.beatmatch;
//
//import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Chronometer;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.Timer;
//
//
//public class AccelerometerSensor extends AppCompatActivity implements SensorEventListener {
//
//    private View myView;
//    private Sensor mySensor;
//    private SensorManager mySM;
//    private TextView xValue, yValue, zValue, timeValue, infoText, LastStepValue, TempoValue, LoopTimeValue;
//    private WindowManager myWM;
//    private Timer timer;
//    private Chronometer chronometer;
//    private long pauseOffset;
//    private boolean running;
//    private boolean firstValue;
//    private Long stepTimes[];
//    private int step = 0;
//    private Long startTime;
//    private boolean readyForStep = true;
//    private long firstStep;
//    private long BPM;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_accelerometer_sensor);
//        stepTimes = new Long[5];
//
//        myWM = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        xValue = (TextView)findViewById(R.id.xValueId);
//        yValue = (TextView)findViewById(R.id.yValueId);
//        zValue = (TextView)findViewById(R.id.zValueId);
//        timeValue = (TextView)findViewById(R.id.timeValueId);
//        infoText = (TextView)findViewById(R.id.infoTextId);
//        LastStepValue = (TextView)findViewById(R.id.LastStepValueId);
//        TempoValue = (TextView)findViewById(R.id.TempoValueId);
//        LoopTimeValue = (TextView)findViewById(R.id.LoopTimeValueId);
//
//
//        mySM = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
//
//        if(mySM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null){
//            Toast myToast = Toast.makeText(getApplicationContext(), "Accelerometer Not Present!", Toast.LENGTH_SHORT);
//            myToast.show();
//        }
//
//        else{
//            mySensor = mySM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        }
//
//
//
//    }
//
//
//    @Override
//    public String onSensorChanged(SensorEvent sensorEvent) {
//        //chronometer.setBase(SystemClock.elapsedRealtime());
//        //chronometer.start();
//        if(firstValue == true){
//            firstValue = false;
//            startTime = System.currentTimeMillis();
//        }
//        Long currentTime = System.currentTimeMillis();
//        //shows current time
//        timeValue.setText("Time: " + currentTime);
//        //accelerometer things
//        xValue.setText("X: " + sensorEvent.values[0]);
//        yValue.setText("Y: " + sensorEvent.values[1]);
//        zValue.setText("Z: " + sensorEvent.values[2]);
//        //time of last step detected
//
//
//        if (readyForStep == true) {
//            //Log.e("scrubs", "for sensorevent.values[1]");
//
//            if(sensorEvent.values[1] > 12.0){
//
//                Log.e("scrubs", "Before lastsetpvalue");
//
//
//
//                Log.e("scrubs", "after lastsetpvalue");
//
//                stepTimes[step % 5] = currentTime;
//
//                Log.e("scrubs", "after step times");
//                if (step == 0) {
//                    firstStep = currentTime;
//                }
//                step++;
//                readyForStep = false;
//            }
//            else {
//            }
//        }
//        else {
//            if(sensorEvent.values[1] > 12.0){
//
//            }
//            else {
//                readyForStep = true;
//            }
//        }
//
//
//        if(step > 6) {
//            long fiveStepAvg = stepTimes[(step-1) % 5] - stepTimes[step % 5];
//            BPM = 60000/(fiveStepAvg/5);
//            LastStepValue.setText("Last Steps: " + (stepTimes[step % 5] - firstStep) + ", " + (stepTimes[(step-1) % 5] - firstStep) + ", " + (stepTimes[(step-2) % 5] - firstStep) + ", " + (stepTimes[(step-3) % 5] - firstStep) + ", " + (stepTimes[(step-4) % 5] - firstStep));
//            //LastStepValue.setText("Last Steps: " + (stepTimes[step % 5] - firstStep));
//            TempoValue.setText("Tempo: " + BPM);
//        }
//
//        if(sensorEvent.values[2] > 9.0){
//            infoText.setText("Device is Facing Upwards");
//        }
//        else if(sensorEvent.values[2] > -11.0 && sensorEvent.values[2] < -5.0){
//            infoText.setText("Device is Upside Down");
//        }
//        else if(sensorEvent.values[1] > 7.0){
//            infoText.setText("Device is Upright");
//        }
//        else if((sensorEvent.values[0] > 7.0) || (sensorEvent.values[0] > -10.0 && sensorEvent.values[0] < -7.0)){
//            infoText.setText("Device is in Landscape Orientation");
//        }
//
//        Long currentTime2 = System.currentTimeMillis();
//        LoopTimeValue.setText("Loop time: " + (currentTime2 - currentTime));
//        return Long.toString(BPM);
//
//        //colorChange(sensorEvent);
//
//    }
//
////    private void colorChange(SensorEvent event){
////        float [] values = event.values;
////        float x = values[0];
////        float y = values[1];
////        float z = values[2];
////
////        xValue.setText("X: " + x);
////        yValue.setText("Y: "+ y);
////        zValue.setText("Z: "+ z);
////
////    }
//
////    @Override
////    public void onAccuracyChanged(Sensor sensor, int i) {
////        //not used
////    }
////
////    @Override
////    protected void onResume() {
////        super.onResume();
////        mySM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
////    }
////
////    @Override
////    protected void onStop() {
////        super.onStop();
////        mySM.unregisterListener(this);
////    }
////
////    @Override
////    protected void onPause() {
////        super.onPause();
////        mySM.unregisterListener(this);
//    }
//
////    public void startChronometer(View v) {
////        if (!running) {
////            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
////            chronometer.start();
////            running = true;
////        }
////    }
////
////    public void pauseChronometer(View v) {
////        if (running) {
////            chronometer.stop();
////            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
////            running = false;
////        }
////    }
////
////    public void resetChronometer(View v) {
////        chronometer.setBase(SystemClock.elapsedRealtime());
////        pauseOffset = 0;
////    }
//
//
//}
