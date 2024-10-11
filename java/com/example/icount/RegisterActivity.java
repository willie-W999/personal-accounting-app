package com.example.icount;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout tilRegDisplayName, tilRegUserName, tilRegUserPassword, tilRegConfirmPassword;
    private TextInputEditText tietRegDisplayName, tietRegUserName, tietRegUserPassword, tietRegConfirmPassword;
    private Button btnRegister;
    private TextView tvLoginUser;
    private FirebaseAuth firebaseAuth;
    private String displayName, userName, userPassword, userConfirmPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findView();
        firebaseAuth = FirebaseAuth.getInstance();
    }
    private void findView() {
        tilRegDisplayName = findViewById(R.id.tilRegDisplayName);
        tilRegUserName = findViewById(R.id.tilRegUserName);
        tilRegUserPassword = findViewById(R.id.tilRegUserPassword);
        tilRegConfirmPassword = findViewById(R.id.tilRegConfirmPassword);
        tietRegDisplayName = findViewById(R.id.tietRegDisplayName);
        tietRegUserName = findViewById(R.id.tietRegUserName);
        tietRegUserPassword = findViewById(R.id.tietRegUserPassword);
        tietRegConfirmPassword = findViewById(R.id.tietRegConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        tvLoginUser = findViewById(R.id.tvLoginUser);
        tvLoginUser.setOnClickListener(this);

        tilRegDisplayName.setErrorEnabled(true);
        tilRegUserName.setErrorEnabled(true);
        tilRegUserPassword.setErrorEnabled(true);
        tilRegConfirmPassword.setErrorEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tvLoginUser:
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.btnRegister:
                displayName = tietRegDisplayName.getText().toString();
                userName = tietRegUserName.getText().toString();
                userPassword = tietRegUserPassword.getText().toString();
                userConfirmPassword = tietRegConfirmPassword.getText().toString();

                // 檢查輸入資料
                if(TextUtils.isEmpty(displayName)){
                    tilRegDisplayName.setError("請輸入用戶名稱");
                    tilRegUserName.setError(null);
                    tilRegUserPassword.setError(null);
                    tilRegConfirmPassword.setError(null);
                    return;
                }
                if(TextUtils.isEmpty(userName)){
                    tilRegDisplayName.setError(null);
                    tilRegUserName.setError("請輸入 Email 帳號");
                    tilRegUserPassword.setError(null);
                    tilRegConfirmPassword.setError(null);
                    return;
                }
                if(TextUtils.isEmpty(userPassword)){
                    tilRegDisplayName.setError(null);
                    tilRegUserName.setError(null);
                    tilRegUserPassword.setError("請輸入密碼");
                    tilRegConfirmPassword.setError(null);
                    return;
                }
                if(tietRegUserPassword.length() < 6 || tietRegUserPassword.length() > 12){
                    tilRegDisplayName.setError(null);
                    tilRegUserName.setError(null);
                    tilRegUserPassword.setError("密碼長度需要 6 ~ 12 個字");
                    tilRegConfirmPassword.setError(null);
                    return;
                }

                if(TextUtils.isEmpty(userConfirmPassword)){
                    tilRegDisplayName.setError(null);
                    tilRegUserName.setError(null);
                    tilRegUserPassword.setError(null);
                    tilRegConfirmPassword.setError("請輸入確認密碼");
                    return;
                }
                if(!userPassword.equals(userConfirmPassword)){
                    tilRegDisplayName.setError(null);
                    tilRegUserName.setError(null);
                    tilRegUserPassword.setError(null);
                    tilRegConfirmPassword.setError("確認密碼與密碼必須相同");
                    return;
                }
                tilRegDisplayName.setError(null);
                tilRegUserName.setError(null);
                tilRegUserPassword.setError(null);
                tilRegConfirmPassword.setError(null);
                firebaseAuth.createUserWithEmailAndPassword(userName, userPassword)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(displayName)
                                        .build();
                                FirebaseUser firebaseUser = authResult.getUser();
                                firebaseUser.updateProfile(userProfileChangeRequest);
                                Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                break;

        }
    }
}
