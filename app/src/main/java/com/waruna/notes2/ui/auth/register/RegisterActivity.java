package com.waruna.notes2.ui.auth.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.waruna.notes2.R;
import com.waruna.notes2.data.db.entities.User;
import com.waruna.notes2.ui.auth.AuthViewModel;
import com.waruna.notes2.ui.auth.login.LoginActivity;
import com.waruna.notes2.ui.main.main.MainActivity;

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

        authViewModel.getLoggedInUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null && user.isLogin()){
                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });

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