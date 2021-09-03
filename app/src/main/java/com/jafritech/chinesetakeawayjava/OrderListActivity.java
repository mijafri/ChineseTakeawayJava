package com.jafritech.chinesetakeawayjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import com.jafritech.chinesetakeawayjava.helper.Helper;
import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity implements SensorEventListener  {
    // declare view (Button) variables
    Button btnMenu, btnCheckOut;
    // declare RecyclerView
    private RecyclerView myRecyclerView;
    // declare OrderList arrayList
    private ArrayList<ItemsListDataSource> orderList;
    // declare Helper class object
    private Helper helper;
    // declare context
    Context context;
    // Sensor variable declaration
    private Sensor sensor;
    //  Sensor manager variable declaration
    private SensorManager sensorManager;

    // starting point
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflating the UI
        setContentView(R.layout.activity_order_list);
        // initialise context as this activity context
        context = this;
        // initialising all variables
        init();

        // fetch orderList from SQLite with the help of helper function
        orderList = helper.getOrderList(context);
        // declare and initialise LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        // assign linearLayoutManager to recyclerView
        myRecyclerView.setLayoutManager(linearLayoutManager);
        // provide data to recyclerMenuAdapter
        RecyclerOrderAdapter recyclerOrderAdapter = new RecyclerOrderAdapter(orderList, this);
        // assign recyclerViewAdapter to recyclerView
        myRecyclerView.setAdapter(recyclerOrderAdapter);

        // code block to execute when Menu button is clicked
        btnMenu.setOnClickListener(v -> callMenu());
        // code block to execute when checkout button is clicked
        btnCheckOut.setOnClickListener(v -> callCheckout());

    }

    // local function to launch DeliveryDetailsActivity
    private void callCheckout() {
        Intent intentAbout = new Intent(OrderListActivity.this, DeliveryDetailsActivity.class);
        startActivity(intentAbout);
        // finish current activity
        finish();
    }

    // local function to launch MenuActivityActivity
    private void callMenu() {
        // local variable call
        goBack();
    }

    // local function init() to initialise all variables
    public void init(){
        // initialize Checkout button
        btnCheckOut = findViewById(R.id.checkout_orderlist_btn);
        // initialize Menu button
        btnMenu = findViewById(R.id.menu_orderlist_btn);
        // initialize recyclerView
        myRecyclerView = findViewById(R.id.orderlist_rv);
        // initialize OrderList ArrayList
        orderList = new ArrayList<>();
        // initialize Helper class object
        helper = new Helper();
        // initialize sensor manager
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE) ;
        // initialize sensor
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    // initializing the option menu xml file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_orderlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // execute block of codes based on option selected
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            // Executes when Checkout option selected
            case R.id.action_menu_orderList_checkOut :
                callCheckout();
                break;
            // Executes when Menu option selected
            case R.id.action_menu_orderList_menu:
                callMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    // block of code to be executed when back button pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // local function call
       goBack();
    }

    // local function to launch MenuActivity
    public void goBack(){
        Intent intentAbout = new Intent(OrderListActivity.this, MenuActivity.class);
        startActivity(intentAbout);
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