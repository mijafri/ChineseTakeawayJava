package com.jafritech.chinesetakeawayjava;

public class MenuDataSource {
    private final int itemId;
    private  final String name;
    private final double price;
    private final String img;

    public MenuDataSource(int itemId, String name, double price, String img) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.img = img;
    }


    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImg() {
        return img;
    }
}
