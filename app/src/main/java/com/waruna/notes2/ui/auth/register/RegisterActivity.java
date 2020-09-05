package com.waruna.notes2.ui.auth.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.waruna.notes2.R;
import com.waruna.notes2.ui.auth.AuthViewModel;
import com.waruna.notes2.ui.auth.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;

    private Button btnRegister;
    private EditText etEmail, etPass, etCPass;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        init();
    }

    private void init(){
        btnRegister = findViewById(R.id.btn_register);
        etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_password);
        etCPass = findViewById(R.id.et_confirm_password);
        tvLogin = findViewById(R.id.tv_login);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authViewModel.register(
                        etEmail.getText().toString(),
                        etPass.getText().toString(),
                        etCPass.getText().toString()
                );
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}