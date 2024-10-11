package com.example.icount;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PayDAO {
    public static final String DATABASE_NAME = "mydb";
    public static final String TABLE_NAME = "pay_table";
    public static final String KEY_ID = "_id";
    public static final String DATE_COLUMN = "date";
    public static final String ITEM_COLUMN = "item";
    public static final String PAY_COLUMN = "pay";
    private static final String CATEGORY_COLUM = "category";
    // 建立資料表
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DATE_COLUMN + " TEXT NOT NULL," +
            ITEM_COLUMN + " TEXT NOT NULL,"+
            PAY_COLUMN + " DOUBLE NOT NULL, "+
            CATEGORY_COLUM + " TEXT NOT NULL" +
            ")";
    private SQLiteDatabase sqLiteDatabase;
    // 建構子
    public PayDAO(Context context){
        sqLiteDatabase = MyDBHelp.getSqLiteDatabase(context);
    }
    // 新增紀錄
    public Product insert(Product product){
        // 建立 ContentValues 物件
        ContentValues contentValues = new ContentValues();
        // 在 contentValues 物件中以 k(資料表欄位) - v(資料) 放入新增資料
        contentValues.put(DATE_COLUMN, product.getDate());
        contentValues.put(CATEGORY_COLUM, product.getCategory());
        contentValues.put(ITEM_COLUMN, product.getItem());
        contentValues.put(PAY_COLUMN, product.getPay());
        // 新增一筆紀錄，若新增成功會回傳 id
        //                              資料表名稱,   有沒有制定欄位預設值,  新增的資料 ContentValues 物件
        long id = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        product.setId(id);
        return product;
    }

    // 查詢所有紀錄
    public  Cursor getAllPay(){
        String[] columns = {KEY_ID, DATE_COLUMN, ITEM_COLUMN, PAY_COLUMN};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    public int getTotal(String category, int date)
    {
        String[] columns = { "SUM(pay)" };
        String selection = "category = ? AND strftime('%m', date) = ?";
        String[] selectionArgs = { category,  String.format("%02d", date) };
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int totalAmount = 0;
        if (cursor.moveToFirst()) {
            totalAmount = cursor.getInt(0);
        }
        cursor.close();
        return totalAmount;
    }
    public Cursor getSearch(int date)
    {
        String selection = " strftime('%m', date) = ?";
        String[] selectionArgs = { String.format("%02d", date) };
        String orderBy = "date ASC";
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, selection, selectionArgs, null, null, orderBy);
        return cursor;
    }
    public Product findId(long id){
        Product product = null;
        // 設定查詢條件
        String where = KEY_ID + "=" + id;
        // 查詢的欄位
        String[] columns = {KEY_ID, DATE_COLUMN, ITEM_COLUMN, PAY_COLUMN};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, where, null, null, null, null);
        if(cursor.moveToFirst()){
            System.out.println("cursor.moveToFirst()...");
            product = new Product();
            product.setId(cursor.getLong(0));
            product.setDate(cursor.getString(1));
            product.setItem(cursor.getString(2));
            product.setPay(cursor.getDouble(3));
        }
        cursor.close();
        return product;
    }
    // 修改紀錄
    public boolean update(Product product){
        // 建立 ContentValues 物件
        ContentValues contentValues = new ContentValues();
        // 在 contentValues 物件中以 k(資料表欄位) - v(資料) 放入新增資料
        contentValues.put(DATE_COLUMN, product.getDate());
        contentValues.put(ITEM_COLUMN, product.getItem());
        contentValues.put(PAY_COLUMN, product.getPay());
        // 設定條件
        String where = KEY_ID + "=" + product.getId();
        return sqLiteDatabase.update(TABLE_NAME, contentValues, where,null) > 0;
    }
    public boolean delete(long id){
        // 設定條件
        String where = KEY_ID + "=" + id;
        return sqLiteDatabase.delete(TABLE_NAME, where, null) > 0;
    }
}
// https://developer.android.com/training/data-storage/sqlite?hl=zh-tw&authuser=1