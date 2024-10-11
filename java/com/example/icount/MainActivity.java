package com.example.icount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout tilUserName, tilUserPassword;
    private TextInputEditText tietUserName, tietUserPassword;
    private Button btnLogin;
    private TextView tvNewUser, tvForgetPassword;
    private FirebaseAuth firebaseAuth;
    private String userName, userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        firebaseAuth = FirebaseAuth.getInstance();
    }
    private void findView() {
        tilUserName = findViewById(R.id.tilUserName);
        tilUserPassword = findViewById(R.id.tilUserPassword);
        tietUserName = findViewById(R.id.tietUserName);
        tietUserPassword = findViewById(R.id.tietUserPassword);
        tvNewUser = findViewById(R.id.tvNewUser);
        tvNewUser.setOnClickListener(this);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        tvForgetPassword.setOnClickListener(this);

        tilUserName.setErrorEnabled(true);
        tilUserPassword.setErrorEnabled(true);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tvNewUser:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.btnLogin:
                userName = tietUserName.getText().toString();
                userPassword = tietUserPassword.getText().toString();
                if (TextUtils.isEmpty(userName)) {
                    tilUserName.setError("請輸入帳號");
                    tilUserPassword.setError(null);
                    return;
                }
                if (TextUtils.isEmpty(userPassword)) {
                    tilUserName.setError(null);
                    tilUserPassword.setError("請輸入密碼");
                    return;
                }
                tilUserName.setError(null);
                tilUserPassword.setError(null);
                firebaseAuth.signInWithEmailAndPassword(userName, userPassword)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(MainActivity.this, MoneyActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.tvForgetPassword:
                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
                break;
        }

    }
}