package com.example.market;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataBase extends SQLiteOpenHelper {

    public static final String NAME = "Products";
    public static final int DB_VERSION = 10;

    public DataBase(Context context) {
        super(context, NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ ProductsTable.TABLE_NAME+" ("+ ProductsTable.COLUMN_ID+" integer primary key not null," +
                ProductsTable.COLUMN_NAME + " text not null, " +
                ProductsTable.COLUMN_VALUE+" int not null"
                +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + ProductsTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(Product product){
        ContentValues rows = new ContentValues();
        rows.put(ProductsTable.COLUMN_NAME, product.getName());
        rows.put(ProductsTable.COLUMN_VALUE, product.getValue());
        if(getWritableDatabase().insert(ProductsTable.TABLE_NAME, null, rows) == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean update(Product product){
        String[] id = new String[]{Integer.toString(product.getId())};
        ContentValues rows = new ContentValues();
        rows.put(ProductsTable.COLUMN_NAME, product.getName());
        rows.put(ProductsTable.COLUMN_VALUE, product.getValue());
        try {
            if(getWritableDatabase().update(ProductsTable.TABLE_NAME, rows,"rowid = ?", id) == -1){
                return false;
            }
            else {
                return true;
            }
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean delete(Product product){
        String[] id = new String[]{Integer.toString(product.getId())};
        ContentValues rows = new ContentValues();
        if(getWritableDatabase().delete(ProductsTable.TABLE_NAME, "rowid = ?", id) == -1){
            return false;
        }
        else {
            return true;
        }
    }
}
