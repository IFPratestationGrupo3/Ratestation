package com.example.ratestation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_Register extends AppCompatActivity {
    Button loginButton, createAccountButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);


        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Register.this, Activity_Login.class);
            startActivity(intent);
        });


        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Register.this, Activity_CreateAccount.class);
            startActivity(intent);
        });
    }
}