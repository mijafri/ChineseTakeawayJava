package com.jafritech.chinesetakeawayjava;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ContactActivity extends AppCompatActivity implements SensorEventListener {
    // declare view (Button) variables
    private Button btnNav, btnCall, btnEmail;
    // Sensor variable declaration
    private Sensor sensor;
    //  Sensor manager variable declaration
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflating the UI
        setContentView(R.layout.activity_contact);

        // initialising all variables
        init();

        // code block to execute when Nav button is clicked
        btnNav.setOnClickListener(v -> callNav());

        // code block to execute when Call button is clicked
        btnCall.setOnClickListener(v -> callCall());

        // code block to execute when Email button is clicked
        btnEmail.setOnClickListener(v -> callEmail());
    }

    // local function callEmail() to send email
    private void callEmail() {
        // check if device has internet connection
        if(isConnected()){
            // if connected
            // create intent and email receiver
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",getResources().getString(R.string.rest_email), null));
            // specify email subject
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));
            // start intent
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }else {
            // if device has no internet connection
            // display no connection message
            showError();
        }
    }

    // local function callCall() to call
    private void callCall() {
        // create intent
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        // specify the number to be called.
        callIntent.setData(Uri.parse("tel:"
                + Uri.encode(getResources().getString(R.string.contactNo).trim())));
        // specify intent as FLAG_ACTIVITY_NEW_TASK
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // start intent
        startActivity(callIntent);
    }

    // local function callNav() to show map and navigate with Google map
    private void callNav() {
        // check if device has internet connection
        if(isConnected()){
            // if connected
            // specify lat and long
            String strUri = "http://maps.google.com/maps?q=loc:" + getResources().getString(R.string.conLatitude) + "," + getResources().getString(R.string.conLongitude) + " (" + getResources().getString(R.string.chinese_takeaway) + ")";
            // create intent
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
            // set Google map for the intent
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            // Start intent
            startActivity(intent);
        } else {
            // if device has no internet connection
            // display no connection message
            showError();
        }
    }

    // local function init() to initialise all variables
    private void init(){
        // initialize Nav button
        btnNav = findViewById(R.id.nav_contact_btn);
        // initialize call button
        btnCall = findViewById(R.id.call_contact_btn);
        // initialize email button
        btnEmail = findViewById(R.id.email_contact_btn);
        // initialize sensor manager
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE) ;
        // initialize sensor
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    // block of codes to run on back button pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // call local function goBack()
       goBack();
    }

    // Associate xml menu file on create
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // executes when the menu options selected
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            // Executes when Nav option selected
            case R.id.action_contact_nav :
                callNav();
                break;
            // Executes when Call option selected
            case R.id.action_contact_call:
                callCall();
                break;
            // Executes when Email option selected
            case R.id.action_contact_email:
                callEmail();
                break;
            // Executes when Back option selected
            case R.id.action_contact_back:
                // local function called
                goBack();
        }
        return super.onOptionsItemSelected(item);
    }

    // local function to check if device has internet connection and return true or false
    public Boolean isConnected() {
        // declare and initialise connectivity manager
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // check if device has internet connection.
        // return true if has internet connection
        // return false if has no internet connection
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();
    }

    // local fun goBack() to launch MainActivity
    private void goBack(){
        Intent intentAbout = new Intent(ContactActivity.this, MainActivity.class);
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

    // local function showError() to show error message if device is no internet connection.
    private void showError(){
        Toast.makeText(
                this,
                getResources().getString(R.string.con_error_msg),
                Toast.LENGTH_LONG).show();
    }
}