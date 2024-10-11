package com.example.icount;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
public class SearchActivity extends AppCompatActivity {
    public Button btnSearch;
    public Spinner spinner5;
    public ListView listview1;

    private PayDAO payDAO;
    private SimpleCursorAdapter simpleCursorAdapter;

   // private String[] SYear = {"2020年","2021年","2022年","2023年","2024年","2025年","2026年"};
    private String[] SMonth = {"01月","02月","03月","04月","05月","06月","07月","08月","09月","10月","11月","12月"};
    private ArrayAdapter<String> sAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findView();
        payDAO = new PayDAO(SearchActivity.this);
        setTitle("查詢");
    }

    private void findView() {
        listview1 = findViewById(R.id.listview1);

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String M = spinner5.getSelectedItem().toString().replace("月", "");
                int M1 = Integer.parseInt(M);
                Cursor ML = payDAO.getSearch(M1);
                if (ML.getCount() == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    builder.setTitle("注意!!")
                            .setMessage("本月無資料")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    simpleCursorAdapter =new SimpleCursorAdapter(SearchActivity.this,R.layout.item,ML,new String[]{PayDAO.DATE_COLUMN, PayDAO.ITEM_COLUMN, PayDAO.PAY_COLUMN},
                            new int[]{R.id.tvItemDate, R.id.tvItemItem, R.id.tvItemPay},
                            0);
                    listview1.setAdapter(simpleCursorAdapter);
                }else {
                    simpleCursorAdapter =new SimpleCursorAdapter(SearchActivity.this,R.layout.item,ML,new String[]{PayDAO.DATE_COLUMN, PayDAO.ITEM_COLUMN, PayDAO.PAY_COLUMN},
                            new int[]{R.id.tvItemDate, R.id.tvItemItem, R.id.tvItemPay},
                            0);
                    listview1.setAdapter(simpleCursorAdapter);
                }

            }
        });
        spinner5 = findViewById(R.id.spinner5);
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sAdapter = new ArrayAdapter<>(SearchActivity.this,R.layout.spinner_item2,SMonth);
        spinner5.setAdapter(sAdapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
