package com.example.icount;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spinner,spinner1;
    private String[] menu1 = {"服裝", "食物","交通","娛樂","雜費"};
    private int[] menu2 = {R.array.item,R.array.item1,R.array.item2,R.array.item3,R.array.item4};
    private EditText etDate,  etPay;
    private ImageButton ibDate;
    private Button btnCancel, btnOk;
    private int year, month, dayOfMonth;
    private int state;
    private PayDAO payDAO;
    private long id;
    private Product product;
    private ArrayAdapter<String> item1Adapter,item2Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        findView();
        Intent intent = getIntent();
        Bundle bundle =intent.getExtras();
        state = bundle.getInt("state");
        payDAO = new PayDAO(ItemActivity.this);
        if(state == 1){
            setTitle("新增 紀錄");
        }else{
            setTitle("查詢/修改 紀錄");
            id = bundle.getLong("id");
            System.out.println("ItemActivity onCreate() id：" + id);
            product = payDAO.findId(id);
            if(product != null){
                etDate.setText(product.getDate());
                etPay.setText(String.valueOf(product.getPay()));
            }
        }
    }
    private void findView() {
        etDate = findViewById(R.id.etDate);
        etDate.setFocusable(false);         // 禁用游標
        etPay = findViewById(R.id.etPay);
        ibDate = findViewById(R.id.ibDate);
        btnCancel = findViewById(R.id.btnCancel);
        btnOk = findViewById(R.id.btnOk);
        ibDate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item2Adapter = new ArrayAdapter<>(ItemActivity.this,
                        R.layout.spinner_item,
                        getResources().getStringArray(menu2[position]));
                spinner1.setAdapter(item2Adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        item1Adapter = new ArrayAdapter<>(ItemActivity.this,R.layout.spinner_item,menu1);
        spinner.setAdapter(item1Adapter);
        spinner1 = findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibDate:
                final Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MARCH);  // 0(一月) ~ 11(十二月)
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(ItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        String date = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                        etDate.setText(date);
                    }
                }, year, month, dayOfMonth).show();
                break;
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnOk:
                if(state == 1){
                    // 新增紀錄
                    if(etDate.length()==0 || etPay.length()==0){
                        Toast.makeText(ItemActivity.this, "紅色*號欄位必須填入資料", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String date = etDate.getText().toString();
                    String category = spinner.getSelectedItem().toString();
                    String item = spinner1.getSelectedItem().toString();
                    double pay = Double.parseDouble(etPay.getText().toString());
                    product = new Product(0, date ,item, pay,category);
                    product = payDAO.insert(product);
                    // 判斷是否新增成功
                    if(product.getId() >= 0){
                        //Toast.makeText(ItemActivity.this, "id：" + product.getId() + "\n新增成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        //Toast.makeText(ItemActivity.this, "新增失敗", Toast.LENGTH_SHORT).show();
                    }
                }else if(state == 2){
                    // 修改
                    if(etDate.length()==0 || etPay.length()==0){
                        Toast.makeText(ItemActivity.this, "紅色*號欄位必須填入資料", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String date = etDate.getText().toString();
                    String category = spinner.getSelectedItem().toString();
                    String item = spinner1.getSelectedItem().toString();
                    double pay = Double.parseDouble(etPay.getText().toString());
                    product = new Product(id, date, item, pay,category);
                    // update 修改
                    boolean result = payDAO.update(product);
                    /*Toast.makeText(ItemActivity.this,
                            "id：" + product.getId() + (result ? "\n更新成功" : "\n更新失敗"),
                            Toast.LENGTH_SHORT).show();*/
                    finish();
                }
                break;
        }
    }
}