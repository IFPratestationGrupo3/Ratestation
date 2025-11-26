package com.example.ratestation.Activities.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratestation.Activities.Activity_Main;
import com.example.ratestation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_CreateAccount extends AppCompatActivity {

    EditText nameInput, emailInput, passwordInput;
    Button registerButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                verificarNombreUnicoYRegistrar(name, email, password);
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verificarNombreUnicoYRegistrar(String name, String email, String password) {
        // Verifica si ya existe un usuario con ese nombre
        db.collection("usuarios")
                .whereEqualTo("nombre", name)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Ya existe un usuario con ese nombre
                        Toast.makeText(Activity_CreateAccount.this, "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show();
                    } else {
                        // Nombre libre → podemos registrar
                        registrarUsuarioEnFirebase(name, email, password);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Activity_CreateAccount.this, "Error al verificar nombre: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void registrarUsuarioEnFirebase(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();
                            guardarDatosAdicionales(uid, name, email);
                        } else {
                            Toast.makeText(Activity_CreateAccount.this, "No se pudo obtener el ID del usuario.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Activity_CreateAccount.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void guardarDatosAdicionales(String uid, String name, String email) {
        Map<String, Object> nuevoUsuario = new HashMap<>();
        nuevoUsuario.put("uid", uid);
        nuevoUsuario.put("nombre", name);
        nuevoUsuario.put("email", email);
        nuevoUsuario.put("friends", new ArrayList<String>()); // Array vacío de amigos

        db.collection("usuarios")
                .document(uid)
                .set(nuevoUsuario)
                .addOnSuccessListener(aVoid -> {
                    Intent intent = new Intent(Activity_CreateAccount.this, Activity_Main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Activity_CreateAccount.this, "Error al guardar datos de usuario: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}