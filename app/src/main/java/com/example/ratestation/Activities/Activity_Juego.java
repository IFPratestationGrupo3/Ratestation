package com.example.ratestation.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ratestation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Juego extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private boolean isFavorito = false;
    private String tituloJuego;

    private Handler toastHandler = new Handler();
    private Runnable toastRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        // Inicialización Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referencias a elementos de UI
        RatingBar ratingBar = findViewById(R.id.ratingBarJuego);
        TextView txtTitulo = findViewById(R.id.txtTituloDetalle);
        ImageView imgPortada = findViewById(R.id.imgPortadaDetalle);
        TextView txtFecha = findViewById(R.id.txtFechaDetalle);
        TextView txtGenero = findViewById(R.id.txtGeneroDetalle);
        TextView txtPlataformas = findViewById(R.id.txtPlataformasDetalle);
        TextView txtCalificacion = findViewById(R.id.txtCalificacionDetalle);
        Button btnVolver = findViewById(R.id.btnVolverJuego);
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

        // Volver
        btnVolver.setOnClickListener(v -> finish());

        // Comprobar estado inicial del favorito
        comprobarEstadoFavorito(btnFavorito);

        // RatingBar
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            // Guardar la puntuación seleccionada
            guardarPuntuacionJuego(tituloJuego, rating);

            // Mostrar el Toast después de 2 segundos
            if (toastRunnable != null) {
                toastHandler.removeCallbacks(toastRunnable);
            }
            toastRunnable = () -> Toast.makeText(
                    Activity_Juego.this,
                    "Puntuación guardada: " + rating,
                    Toast.LENGTH_SHORT
            ).show();
            toastHandler.postDelayed(toastRunnable, 2000);
        });

        // Asignar listener al botón de favoritos
        btnFavorito.setOnClickListener(v -> {
            if (isFavorito) {
                eliminarDeFavoritos(btnFavorito);
            } else {
                agregarAFavoritos(btnFavorito);
            }
        });

        // Cargar la puntuación guardada en Firebase
        cargarPuntuacionGuardada(ratingBar);
    }

    // Cargar la puntuación guardada desde Firebase
    private void cargarPuntuacionGuardada(RatingBar ratingBar) {
        if (mAuth.getCurrentUser() == null || tituloJuego == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Map<String, Object> puntuaciones =
                                (Map<String, Object>) doc.get("PuntuacionesJuegos");

                        if (puntuaciones != null && puntuaciones.containsKey(tituloJuego)) {

                            Double puntuacionDouble =
                                    (Double) puntuaciones.get(tituloJuego);

                            if (puntuacionDouble != null) {
                                float puntuacionGuardada =
                                        puntuacionDouble.floatValue();

                                ratingBar.setRating(puntuacionGuardada);
                            }
                        }
                    }
                });
    }

    // Guardar la puntuación seleccionada en Firebase
    private void guardarPuntuacionJuego(String tituloJuego, float puntuacion) {
        if (mAuth.getCurrentUser() == null || tituloJuego == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> nuevaPuntuacion = new HashMap<>();
        nuevaPuntuacion.put(tituloJuego, puntuacion);

        db.collection("usuarios").document(userId)
                .set(Collections.singletonMap("PuntuacionesJuegos", nuevaPuntuacion),
                        SetOptions.merge())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error guardando puntuación", Toast.LENGTH_SHORT).show()
                );
    }

    // Favoritos
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

    private void agregarAFavoritos(Button btnFavorito) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId)
                .update("JuegosFav", FieldValue.arrayUnion(tituloJuego))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Juego añadido a favoritos", Toast.LENGTH_SHORT).show();
                    isFavorito = true;
                    btnFavorito.setText("Eliminar de favoritos");
                });
    }

    private void eliminarDeFavoritos(Button btnFavorito) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId)
                .update("JuegosFav", FieldValue.arrayRemove(tituloJuego))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Juego eliminado de favoritos", Toast.LENGTH_SHORT).show();
                    isFavorito = false;
                    btnFavorito.setText("Añadir a favoritos");
                });
    }
}