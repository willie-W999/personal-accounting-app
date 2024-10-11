package com.example.icount;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilForgetPassword;
    private TextInputEditText titeForgetPassword;
    private Button btnForgetPassword;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        findView();
        firebaseAuth = FirebaseAuth.getInstance();
    }
    private void findView() {
        tilForgetPassword = findViewById(R.id.tilForgetPassword);
        tilForgetPassword.setErrorEnabled(true);
        titeForgetPassword = findViewById(R.id.tietForgetPassword);
        btnForgetPassword = findViewById(R.id.btnForgetPassword);
        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = titeForgetPassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    tilForgetPassword.setError("請輸入 Email");
                    return;
                }
                tilForgetPassword.setError(null);
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "我們已經向您送出重置密碼的郵件!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, "發送重置密碼郵件失敗!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
