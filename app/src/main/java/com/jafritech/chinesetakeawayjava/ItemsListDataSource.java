package com.jafritech.chinesetakeawayjava;

import java.io.Serializable;

public class ItemsListDataSource implements Serializable {
    final int itemSno;
    final String itemName;
    final int itemQty;
    final double itemPrice;
    public ItemsListDataSource(int itemSno, String itemName, int itemQty, double itemPrice) {
        this.itemSno = itemSno;
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.itemPrice = itemPrice;
    }

    public int getItemSno() {
        return itemSno;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemQty() {
        return itemQty;
    }

    public double getItemPrice() {
        return itemPrice;
    }
}
