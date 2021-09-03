package com.jafritech.chinesetakeawayjava;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_TABLE = "ORDER_LIST";
    public static final String COLUMN1 = "slno";
    public static final String COLUMN2 = "item";
    public static final String COLUMN3 = "qty";
    public static final String COLUMN4 = "price";

    private static final String SCRIPT_CREATE_DATABASE = "create table "
            + DATABASE_TABLE + " " + "(" + COLUMN1
            + " integer primary key autoincrement, " + COLUMN2
            + " text not null, " + COLUMN3 + " text not null, " + COLUMN4
            + " text not null);";

    public SqlDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                       int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);

    }

}