package com.example.icount;

import static com.example.icount.PayDAO.TABLE_NAME;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

// 資料庫版本，資料庫結構改變時數字要遞增

public class MyDBHelp extends SQLiteOpenHelper {
    // 資料庫版本，資料庫結構改變時數字要遞增
    private static final int VERSION = 1;
    private static SQLiteDatabase sqLiteDatabase;
    //               內容物件                      資料庫名稱                  複雜查詢時使用                               資料庫版本(整數、遞增)
    public MyDBHelp(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    // 取得資料庫物件
    public static SQLiteDatabase getSqLiteDatabase(Context context) {
        if(sqLiteDatabase == null || !sqLiteDatabase.isOpen()){
            sqLiteDatabase = new MyDBHelp(context, PayDAO.DATABASE_NAME, null, VERSION).getWritableDatabase();
        }
        return sqLiteDatabase;
    }
    // 在第一次執行時得到 SQLiteDatabase 物件時會被呼叫，資料表不存在時會建立資料表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(PayDAO.CREATE_TABLE);
    }
    // 資料表欄位變更時
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            // 建立交易
            sqLiteDatabase.beginTransaction();
            // 判斷參數
            boolean success = false;
            // 由之前不同版本，可做不同動作
            switch (oldVersion){
                case 1:
                    // 相關異動操作...
                    oldVersion++;
                    success = true;
                    break;
            }
            if (success){
                // 正確交易才能成功
                sqLiteDatabase.setTransactionSuccessful();
                // 在 etTransactionSuccessful() 和 endTransaction() 之間不進行任何資料庫操作
            }
            sqLiteDatabase.endTransaction();
        }else{
            onCreate(sqLiteDatabase);
        }
    }

}
