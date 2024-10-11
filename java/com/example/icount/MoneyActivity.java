package com.example.icount;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MoneyActivity extends AppCompatActivity {
    private ListView listView;
    private PayDAO payDAO;
    private SimpleCursorAdapter simpleCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoneyActivity.this, ItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("state", 1);   // 新增
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        payDAO = new PayDAO(MoneyActivity.this);
        listView = findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.itemEmpty));// 當 listView 無資料時，itemEmpty 會顯示
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //System.out.println("### onItemClick position：" + position);
                //System.out.println("### onItemClick id：" + id);
                Intent intent = new Intent(MoneyActivity.this, ItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("state", 2);  // 查詢修改
                bundle.putLong("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                // 單筆查詢
                Product product = payDAO.findId(id);
                AlertDialog.Builder builder = new AlertDialog.Builder(MoneyActivity.this);
                builder.setTitle("刪除紀錄");
                builder.setIcon(R.drawable.outline_close_black_48dp);
                builder.setMessage("項目：" + product.getItem() + "\n確定刪除這筆紀錄?");
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        payDAO.delete(id);
                        onResume();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sigin,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_total:
                Intent intent = new Intent(this, Totalactivity.class);
                startActivity(intent);
                break;
            case R.id.action_search:
                Intent intent1 = new Intent(this, SearchActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // listView 畫面更新
        Cursor cursor = payDAO.getAllPay(); // 查詢所有紀錄
        simpleCursorAdapter = new SimpleCursorAdapter(MoneyActivity.this,
                R.layout.item,
                cursor,
                new String[]{PayDAO.DATE_COLUMN, PayDAO.ITEM_COLUMN, PayDAO.PAY_COLUMN},
                new int[]{R.id.tvItemDate, R.id.tvItemItem, R.id.tvItemPay},
                0);
        listView.setAdapter(simpleCursorAdapter);
    }
}
