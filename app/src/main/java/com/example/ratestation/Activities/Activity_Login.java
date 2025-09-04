package com.example.ratestation.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratestation.R;

public class Activity_Login extends AppCompatActivity {
    Button loginButton, createAccountButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);


        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Login.this, Activity_Sign.class);
            startActivity(intent);
        });


        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Login.this, Activity_CreateAccount.class);
            startActivity(intent);
        });
    }
}