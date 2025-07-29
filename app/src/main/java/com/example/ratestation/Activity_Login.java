package com.example.ratestation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_Login extends AppCompatActivity {
    EditText emailLogin, passwordLogin;
    Button loginConfirmButton, createAccountButton;
    TextView forgotPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        loginConfirmButton = findViewById(R.id.loginConfirmButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);

        loginConfirmButton.setOnClickListener(v -> {
            String email = emailLogin.getText().toString().trim();
            String password = passwordLogin.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                //aUTENTIFICACIONES YDEMAS
                Toast.makeText(this, "Inicio de sesión simulado", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Activity_Login.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
            //pASSWORD RESET
        forgotPasswordLink.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Login.this, Activity_PasswordReset.class);
            startActivity(intent);
        });

        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Login.this, Activity_Register.class);
            startActivity(intent);
        });
    }
}