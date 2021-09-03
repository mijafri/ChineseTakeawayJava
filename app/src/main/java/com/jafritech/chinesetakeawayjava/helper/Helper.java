package com.jafritech.chinesetakeawayjava.helper;

import android.content.Context;
import android.database.Cursor;

import com.jafritech.chinesetakeawayjava.ItemsListDataSource;
import com.jafritech.chinesetakeawayjava.SqlHandler;

import java.text.NumberFormat;
import java.util.ArrayList;

public class Helper {

    // declare SqlHandler instance object
    private SqlHandler sqlHandler;

    // format price
    public String formatPrice(Double price) {
        // format price as per local currency rules
        return NumberFormat.getCurrencyInstance().format(price);
    }

    // check if order list is empty
    public Boolean checkOrderList(Context context) {
        sqlHandler = new SqlHandler(context);

        String query = "SELECT * FROM ORDER_LIST";
        Cursor c1 = sqlHandler.selectQuery(query);
        c1.moveToFirst();
        return (c1.getCount() != 0);
    }

    // delete all records of order list
    public void insertOrder(String name, String strQty, String strPrice, Context context) {
        sqlHandler =new SqlHandler(context);

        // preparing query to inset row in the order list table
        String query = ("INSERT INTO ORDER_LIST (item,qty,price) values ('"
                + name + "','" + strQty + "','" + strPrice + "')");
        // executing query to inset row in the order list table
        sqlHandler.executeQuery(query);
    }

    // delete all records of order list
    public void deleteAllRecords(Context context) {
        sqlHandler =new SqlHandler(context);
        String query = "DELETE FROM ORDER_LIST;\n";
        query += "DELETE FROM SQLITE_SEQUENCE WHERE name='ORDER_LIST';";
        sqlHandler.deleteRecords(query);
    }

    // arrange order list row
    public String arrangeOrderItem(
            String slno,
            int a,
            String items,
            int b,
            String qty,
            int c,
            String price
    ) {

        int sl = (int)(a * 1.6);
        int its = (int) (b * 1.6);
        int qt = (int) (c * 1.6);
        StringBuilder str = new StringBuilder(slno);
        for (int i=0; i < sl ; i++) {
            str.append(" ");
        }
        str.append(items);
        for (int i=0; i < its ; i++) {
            str.append(" ");
        }
        str.append(qty);
        for (int i=0; i < qt ; i++) {
            str.append(" ");
        }
        str.append(price);
        return str.toString();
    }

    // return order list
    public ArrayList<ItemsListDataSource> getOrderList(Context context){
        // declare OrderAdapterDataSource order list
        ArrayList<ItemsListDataSource> orderList = new ArrayList<>();
        // declare SqlHandler instance
        SqlHandler sqlHandler = new SqlHandler(context);
        // declare variable to manage item s. no.
        int Sno = 0;
        // create query
        String query = "SELECT * FROM ORDER_LIST ";
        // execute query
        Cursor c1 = sqlHandler.selectQuery(query);
        // check if result is not empty
        if (c1 != null && c1.getCount() != 0) {
            // if data exist, goto first row
            if(c1.moveToFirst()){
                // fetch all the records while looping
                do{
                    Sno += 1;
                    // fetch row data
                    String name = c1.getString(c1.getColumnIndex("item"));
                    int qty = Integer.parseInt(c1.getString(c1.getColumnIndex("qty")));
                    double price = Double.parseDouble(c1.getString(c1.getColumnIndex("price")));

                    // create object and add to array list
                    orderList.add( new ItemsListDataSource(Sno, name, qty, price));

                }while (c1.moveToNext());
            }
        }
        // return orderList
        return orderList;
    }

}
