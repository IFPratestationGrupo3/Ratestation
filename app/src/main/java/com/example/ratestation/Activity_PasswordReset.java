package com.example.ratestation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Activity_PasswordReset extends AppCompatActivity {
    EditText emailReset;
    Button sendResetButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        emailReset = findViewById(R.id.emailReset);
        sendResetButton = findViewById(R.id.sendResetButton);
        mAuth = FirebaseAuth.getInstance();

        sendResetButton.setOnClickListener(v -> {
            String email = emailReset.getText().toString().trim();

            if (!email.isEmpty()) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Correo enviado. Revisa tu bandeja.", Toast.LENGTH_LONG).show();
                                finish(); //volver atrás auto
                            } else {
                                Toast.makeText(this, "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Introduce un email válido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}