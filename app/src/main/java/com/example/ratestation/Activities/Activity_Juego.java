package com.example.ratestation.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ratestation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Activity_Juego extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isFavorito = false; // Flag para saber el estado actual
    private String tituloJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        // Inicialización Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referencias a elementos de UI
        TextView txtTitulo = findViewById(R.id.txtTituloDetalle);
        ImageView imgPortada = findViewById(R.id.imgPortadaDetalle);
        TextView txtFecha = findViewById(R.id.txtFechaDetalle);
        TextView txtGenero = findViewById(R.id.txtGeneroDetalle);
        TextView txtPlataformas = findViewById(R.id.txtPlataformasDetalle);
        TextView txtCalificacion = findViewById(R.id.txtCalificacionDetalle);
        Button btnVolver = findViewById(R.id.btnVolver);
        Button btnFavorito = findViewById(R.id.btnFavoritoJuego);

        // Recibir datos enviados desde el Adapter
        tituloJuego = getIntent().getStringExtra("titulo");
        String portada = getIntent().getStringExtra("portada");
        String fecha = getIntent().getStringExtra("fecha");
        String genero = getIntent().getStringExtra("genero");
        String plataformas = getIntent().getStringExtra("plataformas");
        String calificacion = getIntent().getStringExtra("calificacion");

        // Mostrar datos en pantalla
        txtTitulo.setText(tituloJuego);
        txtFecha.setText("Fecha de lanzamiento: " + fecha);
        txtGenero.setText("Género: " + genero);
        txtPlataformas.setText("Plataformas: " + plataformas);
        txtCalificacion.setText("Calificación: " + calificacion);

        Glide.with(this).load(portada).into(imgPortada);

        btnVolver.setOnClickListener(v -> finish());

        // Comprueba el estado inicial del favorito y configura el botón
        comprobarEstadoFavorito(btnFavorito);

        // Asigna un ÚNICO listener al botón
        btnFavorito.setOnClickListener(v -> {
            if (isFavorito) {
                eliminarDeFavoritos(btnFavorito);
            } else {
                agregarAFavoritos(btnFavorito);
            }
        });
    }

    /**
     * Comprueba si el juego está en favoritos y actualiza la UI correspondientemente.
     */
    @SuppressWarnings("unchecked")
    private void comprobarEstadoFavorito(Button btnFavorito) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                List<String> favoritos = (List<String>) doc.get("JuegosFav");
                if (favoritos != null && favoritos.contains(tituloJuego)) {
                    isFavorito = true;
                    btnFavorito.setText("Eliminar de favoritos");
                } else {
                    isFavorito = false;
                    btnFavorito.setText("Añadir a favoritos");
                }
            }
        });
    }

    /**
     * Añade el juego a la lista de favoritos y actualiza la UI.
     */
    private void agregarAFavoritos(Button btnFavorito) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId)
                .update("JuegosFav", FieldValue.arrayUnion(tituloJuego))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Juego añadido a favoritos", Toast.LENGTH_SHORT).show();
                    isFavorito = true;
                    btnFavorito.setText("Eliminar de favoritos");
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error añadiendo a favoritos", Toast.LENGTH_SHORT).show()
                );
    }

    /**
     * Elimina el juego de la lista de favoritos y actualiza la UI.
     */
    private void eliminarDeFavoritos(Button btnFavorito) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId)
                .update("JuegosFav", FieldValue.arrayRemove(tituloJuego))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Juego eliminado de favoritos", Toast.LENGTH_SHORT).show();
                    isFavorito = false;
                    btnFavorito.setText("Añadir a favoritos");
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error eliminando de favoritos", Toast.LENGTH_SHORT).show()
                );
    }
}
