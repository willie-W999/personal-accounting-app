package com.example.icount;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Totalactivity extends AppCompatActivity {
    private TextView tvTotal, tvWarm;
    private PayDAO payDAO;
    private Spinner spinnerMonth,spinnerCategory;
    private String[] menuCategory = {"服裝", "食物","交通","娛樂","雜費"};
    private String[] menuMonth = {"01月","02月","03月","04月","05月","06月","07月","08月","09月","10月","11月","12月"};
    private Button buttonC;
    private ArrayAdapter<String> categoryAdapter,monthAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);
        setTitle("統計");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findView();
        payDAO = new PayDAO(Totalactivity.this);

    }
    private void findView() {
        tvTotal = findViewById(R.id.tvTotal);
        tvWarm = findViewById(R.id.tvWarm);
        spinnerMonth = findViewById(R.id.spinnerMoth);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        monthAdapter = new ArrayAdapter<>(Totalactivity.this,R.layout.spinner_item1,menuMonth);
        spinnerMonth.setAdapter(monthAdapter);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categoryAdapter = new ArrayAdapter<>(Totalactivity.this,R.layout.spinner_item1,menuCategory);
        spinnerCategory.setAdapter(categoryAdapter);
        buttonC = findViewById(R.id.buttonC);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.buttonC)
                {
                    String monthString = spinnerMonth.getSelectedItem().toString().replace("月", "");
                    int month = Integer.parseInt(monthString);
                    String C = spinnerCategory.getSelectedItem().toString();
                    double totalPay = payDAO.getTotal(C,month);
                    if( totalPay == 0)
                    {
                        tvTotal.setText("本月無消費");
                    }else
                    {
                        tvTotal.setText(spinnerMonth.getSelectedItem().toString()+C+"總花費" +  String.format("%.0f", totalPay) + "元");
                    }

                    if (totalPay > 10000)
                    {
                        tvWarm.setText("這個月"+ C +"費花太多了!!");
                    }else{
                        tvWarm.setText("");
                    }
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
