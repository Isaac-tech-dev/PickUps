package com.spotters.dispatch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class Show_Password extends AppCompatActivity {
    SessionManage sessionManage;
    TextView password;
    Button login;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_password);

        password = findViewById(R.id.pass);
        login = findViewById(R.id.login);

        sessionManage = new SessionManage(this);
        sessionManage.checkPassword();

        HashMap<String, String> user = sessionManage.getUserInfo();
        String mPassword = user.get(sessionManage.PASSWORD);

        password.setText(mPassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(Show_Password.this, Login.class);
                startActivity(login);
            }
        });
    }
}