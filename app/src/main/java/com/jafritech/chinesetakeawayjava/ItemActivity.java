package com.jafritech.chinesetakeawayjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.jafritech.chinesetakeawayjava.helper.Helper;

import java.text.DecimalFormat;

public class ItemActivity extends AppCompatActivity {

    // declare view (Button) variables
    private Button btnCancel, btnAdd;
    // declare view (TextView) variables
    private TextView tvItemName, tvItemPrice, tvMinus, tvQty, tvPlus, tv_total_price;
    // declare view (ImageView) variables
    private ImageView ivFoodPicture;
    // declare Helper class object
    private Helper helper;
    // declare context
    private Context context;
    // declare variable to track order item quantity
    private int itemPc = 1;
    // declare variable to receive bundle information item name
    private String name;
    // declare variable to receive bundle information item price
    private double price;
    // declare variable to receive bundle information item image
    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflating the UI
        setContentView(R.layout.activity_item);
        // initialise context as this activity context
        context = this;
        // initialising all variables
        init();

        // if the is value in saveInstance
        if (savedInstanceState != null) {
            // assign the respective value to the variables
            itemPc = savedInstanceState.getInt("PC");
            price = savedInstanceState.getDouble("PRICE");
            img = savedInstanceState.getString("IMG");
            name = savedInstanceState.getString("NAME");

        } else {
            // if Bundle is empty call extractBundle local function to get value from Bundle
            // local function call
            extractBundle();
        }

        // calling local function
        initializeScreenDisplay();

        // code block to execute when Add button is clicked
        btnAdd.setOnClickListener(v -> callAddItemToBasket());

        // code block to execute when Cancel button is clicked
        btnCancel.setOnClickListener(v -> callCancel());

        // code block to execute when minus clicked
        tvMinus.setOnClickListener(v -> callMinus());

        // code block to execute when plus clicked
        tvPlus.setOnClickListener(v -> callPlus());
    }

    // local function to add item quantity
    private void callPlus() {
        // check if order quantity is less than 50
        if (itemPc < 50) {
            // if less than 50
            // increment quantity by 1
            itemPc += 1;
            // local function call to populate UI
            displayDescription();
        } else {
            // if order quantity is more than 50
            // display user message
            Toast.makeText(this, "Quantity can not be more than 50", Toast.LENGTH_SHORT).show();
        }
    }

    // local function to reduce item quantity
    private void callMinus() {
        // check if order quantity is greater than 1
        if (itemPc > 1) {
            // if order quantity is greater than 1
            // decrement order quantity by 1
            itemPc -= 1;
            // local function call to populate UI
            displayDescription();
        } else {
            // if order quantity is not more than 1
            // display user message
            Toast.makeText(this, "Quantity can not be less than 1", Toast.LENGTH_SHORT).show();
        }
    }

    // local function to close current activity
    private void callCancel() {
        // close current activity
        finish();
    }


    // local function to initialise UI
    @SuppressLint("SetTextI18n")
    private void initializeScreenDisplay() {
        // populate item name
        tvItemName.setText(name);
        // populate item price
        tvItemPrice.setText(helper.formatPrice(price) + "/pc");
        // prepare item image resources

        try {
            // get package name
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            // get image id
            int imgId = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + img, null, null);
            // decode resource
            ivFoodPicture.setImageBitmap(BitmapFactory.decodeResource(getResources(), imgId));
            // set image background
            ivFoodPicture.setImageDrawable(ResourcesCompat.getDrawable(getResources(), imgId, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // calling local function to populate description
        displayDescription();
    }

    // local function to populate description
    @SuppressLint("SetTextI18n")
    private void displayDescription() {

        // calculate total price
        // declare variable to track total order price
        double totalItemPrice = (itemPc * price);
        // declare description variable and populate value
        String description =
                itemPc + " Pc(s) of " + name + " = " + helper.formatPrice(totalItemPrice);
        // populate description value on the screen
        tv_total_price.setText(description);
        // populate item quantity on the screen
        tvQty.setText(Integer.toString(itemPc));
    }

    // local function init() to initialise all variables
    private void init() {
        // initialize Cancel button
        btnCancel = findViewById(R.id.cancel_item_btn);
        // initialize Add button
        btnAdd = findViewById(R.id.add_item_btn);
        // initialize ItemName textView
        tvItemName = findViewById(R.id.tv_itemname);
        // initialize ItemPrice textView
        tvItemPrice = findViewById(R.id.tv_item_price);
        // initialize Minus textView
        tvMinus = findViewById(R.id.tv_minus);
        // initialize ItemQuantity textView
        tvQty = findViewById(R.id.tv_qty);
        // initialize Plus textView
        tvPlus = findViewById(R.id.tv_plus);
        // initialize totalPrice textView
        tv_total_price = findViewById(R.id.tv_total_price);
        // initialize foodPicture imageView
        ivFoodPicture = findViewById(R.id.imageView);
        // initialize Helper class object
        helper = new Helper();

    }

    // local function to extract saveInstateState Bundle
    private void extractBundle() {
        Intent intent = getIntent();
        //declare bundle variable
        Bundle bundle = intent.getExtras();
        // extract information associated with "NAME" key
        name = bundle.getString("NAME");
        // extract information associated with "PRICE" key
        price = bundle.getDouble("PRICE");
        // extract information associated with "IMG" key
        img = bundle.getString("IMG");
    }


    // override method to save UI value
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // save itemPrice to savedInstanceState Bundle
        savedInstanceState.putDouble("PRICE", price);
        // save itemPc to savedInstanceState Bundle
        savedInstanceState.putInt("PC", itemPc);
        // save itemImage to savedInstanceState Bundle
        savedInstanceState.putString("IMG", img);
        // save itemName to savedInstanceState Bundle
        savedInstanceState.putString("NAME", tvItemName.getText().toString());
    }

    // local function to add order item to order list
    private void callAddItemToBasket() {
        DecimalFormat df = new DecimalFormat("#.00");
        // holding item price as a string
        String strPrice = df.format(itemPc * price);

        // holding quantity as a string
        String strQty = String.valueOf(itemPc);

        helper.insertOrder(name, strQty, strPrice, context);
        // display user notification
        Toast.makeText(
                getApplicationContext(), getResources().getString(R.string.item_added_notification) ,
                Toast.LENGTH_SHORT
        ).show();
        // close current activity
        finish();
    }

}