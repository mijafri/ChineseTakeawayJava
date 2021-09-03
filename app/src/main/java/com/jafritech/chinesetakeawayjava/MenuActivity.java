package com.jafritech.chinesetakeawayjava;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jafritech.chinesetakeawayjava.helper.Helper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity implements SensorEventListener {

    // declare view (Button) variables
    private Button btnOrderList, btnCheckOut;
    // declare RecyclerView
    private RecyclerView myRecyclerView;
    // declare Menu arrayList
    private ArrayList<MenuDataSource> menuList;
    // declare Helper class object
    private Helper helper;
    // declare context
    private Context context;
    // Sensor variable declaration
    private Sensor sensor;
    //  Sensor manager variable declaration
    private SensorManager sensorManager;

    // start point
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflating the UI
        setContentView(R.layout.activity_menu);
        // initialise context as this activity context
        context = this;
        // initialising all variables
        init();
        // fetch data from json file and assign to menu ArrayList
        getMenuFromJson();
        // declare and initialise LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        // assign linearLayoutManager to recyclerView
        myRecyclerView.setLayoutManager(linearLayoutManager);
        // provide data to recyclerMenuAdapter
        RecyclerMenuAdapter recyclerMenuAdapter = new RecyclerMenuAdapter(menuList, this);
        // assign recyclerViewAdapter to recyclerView
        myRecyclerView.setAdapter(recyclerMenuAdapter);

        // code block to execute when OrderList button is clicked
        btnOrderList.setOnClickListener(v -> callOrderList());
        // code block to execute when Checkout button is clicked
        btnCheckOut.setOnClickListener(v -> callCheckout());
    }

    // local function to launch DeliveryDetailsActivity
    private void callCheckout() {
        // check if orderList is not empty
        if(helper.checkOrderList(context)){
            // if not empty
            // declare intent
            Intent intentCheckout = new Intent(MenuActivity.this, DeliveryDetailsActivity.class);
            // start intent
            startActivity(intentCheckout);
            // close current activity
            finish();
        }else {
            // if empty, show user message
            showEmptyErrorMessage();
        }
    }

    // local function to launch OrderListActivity
    private void callOrderList() {
        // check if orderList is not empty
        if(helper.checkOrderList(context)){
            // if not empty
            // declare intent
            Intent intentOL = new Intent(MenuActivity.this, OrderListActivity.class);
            // start intent
            startActivity(intentOL);
            // finish current activity
            finish();
        }else {
            // if empty, show user message
            showEmptyErrorMessage();
        }
    }

    // block of codes to run on back button pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // call local function goBack()
        goBack();
    }

    // local function init() to initialise all variables
    private void init(){
        // initialize Checkout button
        btnCheckOut = findViewById(R.id.checkout_menu_btn);
        // initialize OrderList button
        btnOrderList = findViewById(R.id.orderlist_menu_btn);
        // initialize recyclerView
        myRecyclerView = findViewById(R.id.menulist_menu_rv);
        // initialize Menu ArrayList
        menuList = new ArrayList<>();
        // initialize Helper class object
        helper = new Helper();
        // initialize sensor manager
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE) ;
        // initialize sensor
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    // local function to fetch data from json file and assign to MenuListArray
    private void getMenuFromJson() {
        try {
            // declare and initialise jsonObject
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(JsonDataFromAsset()));
            // declare and initialise jsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("foodmenu");
            // loop through each record
            for(int i=0; i<jsonArray.length();i++){
                // fetch one row
                JSONObject userdata = jsonArray.getJSONObject(i);
                // add a row to menuArrayList
                menuList.add(
                        new MenuDataSource(
                                userdata.getInt("itemId"),
                                userdata.getString("name"),
                                userdata.getDouble("price"),
                                userdata.getString("img")
                        )
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // local function to fetch data from json File
    private String JsonDataFromAsset() {
        // declare json String variable and assign null
        String json;
        try {
            // declare and initialise inputStream and open json file to read data
            InputStream inputStream = getAssets().open("foodmenu.json");
            // get the size of inputStream (no of rows)
            int sizeOfFile  = inputStream.available();
            // declare a byte buffer
            byte[] bufferData = new byte[sizeOfFile];
            // read bufferData
            inputStream.read(bufferData);
            // close input Stream
            inputStream.close();
            // convert data to string
            json = new String(bufferData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // return data
        return json;
    }

    // initializing the option menu xml file
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // execute block of codes based on option selected
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            // Executes when OrderList option selected
            case R.id.action_menu_menu_orderList :
                callOrderList();
                break;
            // Executes when Checkout option selected
            case R.id.action_menu_menu_checkOut:
                callCheckout();
                break;
            // Executes when Back option selected
            case R.id.action_menu_menu_back:
                goBack();
        }
        return super.onOptionsItemSelected(item);
    }

    // local function to show error message if orderList is empty
    private void showEmptyErrorMessage() {
        Toast.makeText(
                this,
                getResources().getString(R.string.empty_order_list_message),
                Toast.LENGTH_LONG
        ).show();
    }

    // local function to launch MainActivity
    private void goBack(){
        Intent intentAbout = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intentAbout);
        // close current activity
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