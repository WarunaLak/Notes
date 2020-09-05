package com.waruna.notes2.ui.auth.login;

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
import com.waruna.notes2.ui.auth.register.RegisterActivity;
import com.waruna.notes2.ui.main.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;

    private Button btnLogin;
    private EditText etEmail, etPass;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        authViewModel.getLoggedInUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null && user.isLogin()){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });

        init();
    }

    private void init(){
        btnLogin = findViewById(R.id.btn_login);
        etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_password);
        tvRegister = findViewById(R.id.tv_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authViewModel.login(etEmail.getText().toString(), etPass.getText().toString());
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}