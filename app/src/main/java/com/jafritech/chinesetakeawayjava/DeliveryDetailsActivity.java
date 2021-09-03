package com.jafritech.chinesetakeawayjava;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.jafritech.chinesetakeawayjava.helper.Helper;
import java.util.ArrayList;

public class DeliveryDetailsActivity extends AppCompatActivity {

    // declare view (EditText) variables
    private EditText nameDdEt, contactNoDdEt, addLine1DdEt, addLine2DdEt, delInstructionDdEt, latDdEt, longDdEt;
    // declare view (Button) variables
    private Button pickLocationDdBtn, clearDdBtn, cancelDdBtn, submitDdBtn;
    // declare constant for sharedPreferences
    public static final String MyPREFERENCES = "myPrefs";
    public static final String PrName = "nameKey";
    public static final String PrContact = "contactKey";
    public static final String PrAddress1 = "address1Key";
    public static final String PrAddress2 = "address2Key";
    public static final String PrDelIns = "delInsKey";
    public static final String PrLatitude = "latitudeKey";
    public static final String PrLongitude = "longitudeKey";


    // declare SharedPreferences
    private SharedPreferences sharedpreferences;
    // declare Helper object
    private Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflating the UI
        setContentView(R.layout.activity_delivery_details);
        // initialising all variables
        init();
        // check if previous value available, if available populate it to UI
        fillDetailsIfAvailable();

        // code block to execute when PickLocation button is clicked
        pickLocationDdBtn.setOnClickListener(v -> callPickLocation());

        // code block to execute when Clear button is clicked
        clearDdBtn.setOnClickListener(v -> callClear());

        // code block to execute when Cancel button is clicked
        cancelDdBtn.setOnClickListener(v -> goBack());

        // code block to execute when Submit button is clicked
        submitDdBtn.setOnClickListener(v -> callSubmit());
    }

    // local function to fetch and populate saved delivery address
    private void fillDetailsIfAvailable() {
        if (sharedpreferences.contains(PrName)) {
            nameDdEt.setText(sharedpreferences.getString(PrName, ""));
            contactNoDdEt.setText(sharedpreferences.getString(PrContact, ""));
            addLine1DdEt.setText(sharedpreferences.getString(PrAddress1, ""));
            addLine2DdEt.setText(sharedpreferences.getString(PrAddress2, ""));
            latDdEt.setText(sharedpreferences.getString(PrLatitude, ""));
            longDdEt.setText(sharedpreferences.getString(PrLongitude, ""));
        }
        if(sharedpreferences.contains(PrDelIns)){
            delInstructionDdEt.setText(sharedpreferences.getString(PrDelIns, ""));
        }
    }

    // local function to submit delivery request
    private void callSubmit() {
        // check if all the required information is provided on UI
        if(checkET()){
            // if all necessary information is provided
            // save details to sharedPreference
            saveDeliveryDetails();
            // Check if internet is connected
            if(isConnected()){
                // calling local method prepareOrderText()
                String address = String.valueOf(prepareDelAddress());
                // calling local method prepareOrderHeader()
                String orderHead =  prepareOrderHeader().toString();
                // calling local method prepareOrderBody()
                String orderBody = prepareOrderBody();
                // prepare final order
                StringBuilder order = new StringBuilder();
                order.append(orderHead);
                order.append("\n" + orderBody);
                order.append("\n");
                order.append("\n" + address );
                order.append("\n----------------------------------------------");
                // check if device has internet connection
                if(isConnected()){
                    // if connected
                    // create intent and email receiver
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto",getResources().getString(R.string.rest_email), null));
                    // specify email subject
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_sub_order));
                    // specify email body
                    emailIntent.putExtra(Intent.EXTRA_TEXT, order.toString());
                    // start intent
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }else {
                    Toast.makeText(
                            this,
                            getResources().getString(R.string.con_error_msg),
                            Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.con_error_msg), Toast.LENGTH_SHORT).show();
            }
            // call method from helper class to delete all records from order_list table
            helper.deleteAllRecords(this);
            // close the current activity and move to active activity on activity stack
            finish();
        }else{
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.emptyFields), Toast.LENGTH_SHORT).show();
        }
    }

    // local function to prepare delivery address.
    private StringBuilder prepareDelAddress() {
        StringBuilder customerAddress = new StringBuilder();
        customerAddress.append("Delivery Details \n");
        customerAddress.append("-------------------- \n");
        customerAddress.append("Name: ").append(nameDdEt.getText().toString());
        customerAddress.append("\n");
        customerAddress.append("Contact: ").append(contactNoDdEt.getText().toString());
        customerAddress.append("\n");
        customerAddress.append("House no: ").append(addLine1DdEt.getText().toString());
        customerAddress.append("\n");
        customerAddress.append("Postcode: ").append(addLine2DdEt.getText().toString());
        customerAddress.append("\n");
        customerAddress.append("Latitude: ").append(latDdEt.getText().toString());
        customerAddress.append("\n");
        customerAddress.append("Longitude: ").append(longDdEt.getText().toString());
        customerAddress.append("\n");
        customerAddress.append("\n");
        customerAddress.append("Delivery Instruction: ").append(delInstructionDdEt.getText().toString());
        customerAddress.append("\n");
        // return prepared delivery address
        return customerAddress;
    }

    // local function to prepare order header
    private StringBuilder prepareOrderHeader() {
        StringBuilder ordHead = new StringBuilder();
        ordHead.append(getResources().getString(R.string.app_name) + "\n\n");
        ordHead.append(getResources().getString(R.string.order_list_header) + "\n\n");
        // calling function from helper class to arrange gap between header items
        ordHead.append(helper.arrangeOrderItem("SN.", 3, "Items", 15, "Qty", 4, "Price"));
        ordHead.append("\n");
        return ordHead;

    }

    // local function to prepare delivery order
    private String prepareOrderBody() {
        // declare variable to prepare order body
        StringBuilder strOrder = new StringBuilder();
        // declare variable to count total order item
        int totalQty  = 0;
        // declare variable to count total order price
        double totalPrice  = 0.0;
        // check if order list is not empty by calling function from helper class.
        if(helper.checkOrderList(this)){
            // if order available
            // get order list by calling method from helper class
            ArrayList<ItemsListDataSource> orders = helper.getOrderList(this);
            // loop to read all order items
            for (ItemsListDataSource order : orders){
                // add one line of order in arranged fashion with the help of function from helper classes
                strOrder.append(helper.arrangeOrderItem(
                        String.valueOf(order.itemSno),
                        4 - String.valueOf(order.itemSno).length(),
                        order.itemName,
                        20 - String.valueOf(order.itemName).length(),
                        String.valueOf(order.itemQty),
                        6 - String.valueOf(order.itemQty).length(),
                        helper.formatPrice(order.itemPrice)));
                // add new line
                strOrder.append("\n");
                // count total order item quantity
                totalQty += order.itemQty;
                // count total order price
                totalPrice +=  order.itemPrice;
            }
            // add total quantity to the order body
            strOrder.append("\nTotal Item(s):").append(totalQty).append("\n");
            // add total price to order body
            strOrder.append("Total Bill: ").append(helper.formatPrice(totalPrice));
        }
        // return prepared order body
        return strOrder.toString();
    }


    // local function to save delivery address
    private void saveDeliveryDetails() {
        // fetch information from UI and save in local variable
        String pName = nameDdEt.getText().toString();
        String pContact = contactNoDdEt.getText().toString();
        String pAddress1 = addLine1DdEt.getText().toString();
        String pAddress2 = addLine2DdEt.getText().toString();
        String pDelIns = delInstructionDdEt.getText().toString();
        String pLatitude = latDdEt.getText().toString();
        String pLongitude = longDdEt.getText().toString();
        // declare and initialise sharedPreference
        SharedPreferences.Editor editor = sharedpreferences.edit();
        // save value to sharedPreference
        editor.putString(PrName, pName);
        editor.putString(PrContact, pContact);
        editor.putString(PrAddress1, pAddress1);
        editor.putString(PrAddress2, pAddress2);
        editor.putString(PrDelIns, pDelIns);
        editor.putString(PrLatitude, pLatitude);
        editor.putString(PrLongitude, pLongitude);
        // commit changes
        editor.apply();
    }


    // local function to clear delivery details form
    private void callClear() {
        nameDdEt.setText("");
        contactNoDdEt.setText("");
        addLine1DdEt.setText("");
        addLine2DdEt.setText("");
        delInstructionDdEt.setText("");
        latDdEt.setText("");
        longDdEt.setText("");
    }

    // Local function, picks device current latitude and longitude
    private void callPickLocation() {
        GpsLocationTracker mGpsLocationTracker = new GpsLocationTracker(
                DeliveryDetailsActivity.this);
         // Set GPS Location fetched address
        if (mGpsLocationTracker.canGetLocation()) {
            double latitude = mGpsLocationTracker.getLatitude();
            double longitude = mGpsLocationTracker.getLongitude();
            latDdEt.setText(String.valueOf(latitude));
            longDdEt.setText(String.valueOf(longitude));
        } else {
            mGpsLocationTracker.showSettingsAlert();
        }
        checkStatus();
    }

    // local function to if Lat & Long data is populated
    private void checkStatus() {
        if((latDdEt.getText().toString().equals("")) || longDdEt.getText().toString().equals("0.0")){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.lat_long_alert_message), Toast.LENGTH_LONG).show();
        }
    }

    // local function init() to initialise all variables
    private void init() {
        // initialize name EditText
        nameDdEt = findViewById(R.id.name_dd_et);
        // initialize Contact EditText
        contactNoDdEt = findViewById(R.id.contactno_dd_et);
        // initialize address1 EditText
        addLine1DdEt = findViewById(R.id.add_line1_dd_et);
        // initialize address2 EditText
        addLine2DdEt = findViewById(R.id.add_line2_dd_et);
        // initialize delivery instruction EditText
        delInstructionDdEt = findViewById(R.id.del_instruction_dd_et);
        // initialize lat EditText
        latDdEt = findViewById(R.id.lat_dd_et);
        // initialize long EditText
        longDdEt = findViewById(R.id.long_dd_et);
        // initialize pickLocation Button
        pickLocationDdBtn = findViewById(R.id.picklocation_dd_btn);
        // initialize Clear Button
        clearDdBtn = findViewById(R.id.clear_dd_btn);
        // initialize Cancel Button
        cancelDdBtn = findViewById(R.id.cancel_dd_btn);
        // initialize Submit Button
        submitDdBtn = findViewById(R.id.submit_dd_btn);
       // initialize Helper
        helper = new Helper();
        // initialize sharedPreferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
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
        Intent intentAbout = new Intent(DeliveryDetailsActivity.this, MenuActivity.class);
        startActivity(intentAbout);
        // finish current activity
        finish();
    }

    //local function to check if all required information is provided
    public boolean checkET(){ // to check if required fields not empty in delivery details.

        // landmark
        return !nameDdEt.getText().toString().equals("") &&         // customer name
                !contactNoDdEt.getText().toString().equals("") &&      // contact no.
                !addLine1DdEt.getText().toString().equals("") &&      // address
                !addLine2DdEt.getText().toString().equals("") &&
                !latDdEt.getText().toString().equals("") &&
                !longDdEt.getText().toString().equals("");
    }

    // local function to check if device is connected to internet
    private Boolean isConnected() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();
    }
    // initializing the option menu xml file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delivery, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // execute block of codes based on option selected
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            // Executes when PickLocation option selected
            case R.id.action_menu_delivery_pickLocation :
                callPickLocation();
                break;
            // Executes when Submit option selected
            case R.id.action_menu_delivery_submit:
                callSubmit();
                break;
            // Executes when Clear option selected
            case R.id.action_menu_delivery_clear:
                callClear();
                break;
            // Executes when Cancel option selected
            case R.id.action_menu_delivery_cancel:
                goBack();
        }
        return super.onOptionsItemSelected(item);
    }
}