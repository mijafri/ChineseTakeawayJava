package com.jafritech.chinesetakeawayjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class AboutActivity extends AppCompatActivity implements SensorEventListener {

    // Sensor variable declaration
    private Sensor sensor;
    //  Sensor manager variable declaration
    private SensorManager sensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // initialize sensor manager
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE) ;
        // initialize sensor
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    // Associate xml menu file on create
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // executes when the menu options selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // when back selected
        if (item.getItemId() == R.id.action_about_back) {
            goBack();
        }
        return super.onOptionsItemSelected(item);
    }

    // block of codes to run on back button pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // call local function goBack()
        goBack();
    }

    // local fun goBack() to launch MainActivity
    private void goBack(){
        Intent intentAbout = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(intentAbout);
        // close the current activity
        finish();
    }

    // Ambient light sensor start
    // override onSensorChanged
    @Override
    public void  onSensorChanged(SensorEvent event) {
        // checking if the light value is less than 30
        if(event.values[0] <= 30){
            // if value less than or equal to 30, activate night mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            // if value greater than 30, deactivate night mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    // override onAccuracyChanged
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // override onResume. Register sensor manager
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // override onPause. unregister sensor manager
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}