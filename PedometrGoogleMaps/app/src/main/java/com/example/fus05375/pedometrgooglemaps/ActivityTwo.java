package com.example.fus05375.pedometrgooglemaps;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityTwo extends AppCompatActivity  implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView tv_steps;
    boolean activityRunning = false;
    private Button btn_reset;

    private int stepsInSensor = 0;
    private int stepsAtReset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);


        SharedPreferences prefs = getSharedPreferences("", MODE_PRIVATE);
        stepsAtReset = prefs.getInt("stepsAtReset", 0);

        tv_steps = (TextView) findViewById(R.id.tv_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        btn_reset= (Button) findViewById(R.id.btn_reset);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepsAtReset = stepsInSensor;

                SharedPreferences.Editor editor =
                        getSharedPreferences("", MODE_PRIVATE).edit();
                editor.putInt("stepsAtReset", stepsAtReset);
                editor.commit();

                // you can now display 0:
                tv_steps.setText(String.valueOf(0));}
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_arrow:
                        Intent intent0 = new Intent(ActivityTwo.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_android:
                        Intent intent1 = new Intent(ActivityTwo.this, ActivityOne.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_books:

                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(ActivityTwo.this, ActivityThree.class);
                        startActivity(intent3);
                        break;


                }


                return false;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        activityRunning=true;
        Sensor countSensor= sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor!=null){
            sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_FASTEST);
        }else{
            Toast.makeText(this,"Sensor error!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning=false;
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(activityRunning){
            stepsInSensor = Integer.valueOf((int) event.values[0]);
            int stepsSinceReset = stepsInSensor - stepsAtReset;
            tv_steps.setText(String.valueOf(stepsSinceReset));
        }else{
            event.values[0] = 0;
        }
    }

//    private void resetStepCount() {
//
//        stepsAtReset = stepsInSensor;
//        Toast.makeText(this,"Wskaźnik zresetowany",Toast.LENGTH_SHORT).show();
//        tv_steps.setText(String.valueOf(stepsSinceReset));
//
//    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
