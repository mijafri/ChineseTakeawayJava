package com.jafritech.chinesetakeawayjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import com.jafritech.chinesetakeawayjava.helper.Helper;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // declare view (Button) variables
    private Button btnContact, btnAbout, btnMenu;
    // declare view (ImageView) variables
    private ImageView ivShare;
    // declare helper class object to use helper functions
    private Helper helper;
    // Sensor variable declaration
    private Sensor sensor;
    //  Sensor manager variable declaration
    private SensorManager sensorManager;

    // starting point
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflating the UI
        setContentView(R.layout.activity_main);

        // initialising all variables
        init();

        // deleting previous order if left incomplete
        helper.deleteAllRecords(this);


        // code block to execute when about button is clicked
        btnAbout.setOnClickListener(v -> {
            // local function callAbout() called
            callAbout();
            // finish current activity.
            finish();
        });

        // code block to execute when contact button is clicked
        btnContact.setOnClickListener(v -> {
            // local function callContact() called
            callContact();
            // finish current activity.
            finish();
        });

        // code block to execute when menu button is clicked
        btnMenu.setOnClickListener(v -> {
            // local function callMenu() called
            callMenu();
            // finish current activity.
            finish();
        });

        // code block to execute when menu button is clicked
        ivShare.setOnClickListener(v -> callShare());
    }

    // local function to launch ContactActivity
    private void callContact(){
        Intent intentContact = new Intent(this, ContactActivity.class);
        startActivity(intentContact);
    }

    // local function to launch MenuActivity
    private void callMenu() {
        Intent intentMenu = new Intent(this, MenuActivity.class);
        startActivity(intentMenu);
    }


    // local function to launch AboutActivity
    private void callAbout() {
        Intent intentAbout = new Intent(this, AboutActivity.class);
        startActivity(intentAbout);
    }

    // local function to share application link
    private void callShare() {
        // declare Intent
        Intent sharingIntent = new Intent(
                android.content.Intent.ACTION_SEND);
        // add intent type
        sharingIntent.setType("text/plain");
        // prepare message body
        String shareBody = getResources().getString(R.string.share_body);
        // add message subject
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.chinese_takeaway));
        // add message body
        sharingIntent
                .putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        // start Intent
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    // initializing the option menu xml file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // execute block of codes based on option selected
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            // Executes when Menu option selected
            case R.id.action_menu :
                callMenu();
                break;
            // Executes when Contact option selected
            case R.id.action_contact:
                callContact();
                break;
            // Executes when About option selected
            case R.id.action_about:
                callAbout();
                break;
            // Executes when Close option selected
            case R.id.action_close:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // local function init() to initialise all variables
    private void init(){
    // initialize contact button
    btnContact = findViewById(R.id.contact_main_btn);
    // initialize about button
    btnAbout = findViewById(R.id.about_main_btn);
    // initialize menu button
    btnMenu = findViewById(R.id.menu_main_btn);
    // initialize share imageview
    ivShare = findViewById(R.id.share_home_iv);
    // initialize Helper class object
    helper = new Helper();
    // initialize sensor manager
    sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE) ;
    // initialize sensor
    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
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