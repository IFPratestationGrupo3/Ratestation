package com.example.ratestation.Activities.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratestation.Activities.Activity_Main;
import com.example.ratestation.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Sign extends AppCompatActivity {

    EditText emailLogin, passwordLogin;
    Button loginConfirmButton, createAccountButton;
    TextView forgotPasswordLink;
    private FirebaseAuth mAuth; // Variable para poder conectar con Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        // Se inicializa Firebase Authentication.
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        loginConfirmButton = findViewById(R.id.loginConfirmButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);

      /*
        // INICIO DE SESIÓN SIMULADO AUTOMÁTICO
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Activity_Sign.this, Activity_Main.class);
            startActivity(intent);
            finish();
        }, 2000); // Espera de 2 segundos
        */

        // CÓDIGO ORIGINAL

        loginConfirmButton.setOnClickListener(v -> {
            String email = emailLogin.getText().toString().trim();
            String password = passwordLogin.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                // Autenticaciones y demás

                iniciarSesionConFirebase(email,password);

            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });


        forgotPasswordLink.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Sign.this, Activity_PasswordReset.class);
            startActivity(intent);
        });

        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Sign.this, Activity_Login.class);
            startActivity(intent);
        });
    }
    private void iniciarSesionConFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Si el inicio de sesión tiene éxito, vamos a la pantalla principal
                        Toast.makeText(this, "Inicio de sesión correcto.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(this, Activity_Main.class); // he pasado el Intent que tenía el CÓDIGO ORIGINAL aquí

                        // Limpiamos el historial para que no pueda volver atrás
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                        finish(); // Se cierra esta actividad (ActivitySign)

                    } else {
                        // Si el inicio de sesión falla (contraseña incorrecta, usuario no existe), mostramos un error
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}