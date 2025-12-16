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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Pelicula extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private boolean isFavorita = false;
    private String tituloPelicula;

    private Handler toastHandler = new Handler();
    private Runnable toastRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        // Inicialización Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referencias UI
        RatingBar ratingBar = findViewById(R.id.ratingBarPelicula);
        ImageView imgPoster = findViewById(R.id.imgPosterDetalle);
        TextView txtTitulo = findViewById(R.id.txtTituloDetalle);
        TextView txtAnio = findViewById(R.id.txtAnio);
        TextView txtDirector = findViewById(R.id.txtDirector);
        TextView txtGenero = findViewById(R.id.txtGenero);
        TextView txtSinopsis = findViewById(R.id.txtSinopsis);
        Button btnVolver = findViewById(R.id.btnVolver);
        Button btnFavorita = findViewById(R.id.btnFavorita);

        // Datos de la película
        tituloPelicula = getIntent().getStringExtra("titulo");
        String poster = getIntent().getStringExtra("poster");
        String anio = getIntent().getStringExtra("anio");
        String director = getIntent().getStringExtra("director");
        String generos = getIntent().getStringExtra("generos");
        String sinopsis = getIntent().getStringExtra("sinopsis");

        // Mostrar datos
        txtTitulo.setText(tituloPelicula != null ? tituloPelicula : "Título no disponible");
        txtAnio.setText(anio != null ? "Año: " + anio : "Año no disponible");
        txtDirector.setText(director != null ? "Director: " + director : "Director no disponible");
        txtGenero.setText(generos != null ? "Género: " + generos : "Género no disponible");
        txtSinopsis.setText(sinopsis != null ? sinopsis : "Sinopsis no disponible");

        Glide.with(this)
                .load(poster != null ? poster : R.drawable.ratelogo)
                .placeholder(R.drawable.ratelogo)
                .into(imgPoster);

        // Volver
        btnVolver.setOnClickListener(v -> finish());

        // Comprobar estado inicial de favorita
        comprobarFavorita(btnFavorita);

        // RatingBar
        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            guardarPuntuacion(tituloPelicula, rating);

            if (toastRunnable != null) toastHandler.removeCallbacks(toastRunnable);

            float puntuacionFinal = rating;
            toastRunnable = () -> Toast.makeText(
                    Activity_Pelicula.this,
                    "Puntuación guardada: " + puntuacionFinal,
                    Toast.LENGTH_SHORT
            ).show();

            toastHandler.postDelayed(toastRunnable, 2000);
        });

        // Cargar puntuación guardada en Firebase
        cargarPuntuacionGuardada(ratingBar);
    }

    // Guardar la puntuación en Firestore
    private void guardarPuntuacion(String tituloPelicula, float puntuacion) {
        if (mAuth.getCurrentUser() == null || tituloPelicula == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> nuevaPuntuacion = new HashMap<>();
        nuevaPuntuacion.put(tituloPelicula, puntuacion);

        db.collection("usuarios").document(userId)
                .set(Collections.singletonMap("PuntuacionesPeliculas", nuevaPuntuacion), SetOptions.merge())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error guardando puntuación", Toast.LENGTH_SHORT).show()
                );
    }

    // Cargar puntuación guardada desde Firebase
    private void cargarPuntuacionGuardada(RatingBar ratingBar) {
        if (mAuth.getCurrentUser() == null || tituloPelicula == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Map<String, Object> puntuaciones =
                                (Map<String, Object>) doc.get("PuntuacionesPeliculas");

                        if (puntuaciones != null && puntuaciones.containsKey(tituloPelicula)) {
                            Double puntuacionDouble = (Double) puntuaciones.get(tituloPelicula);
                            if (puntuacionDouble != null) {
                                ratingBar.setRating(puntuacionDouble.floatValue());
                            }
                        }
                    }
                });
    }

    // Favoritas
    @SuppressWarnings("unchecked")
    private void comprobarFavorita(Button btnFavorita) {
        if (mAuth.getCurrentUser() == null || tituloPelicula == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        List<String> favoritas = (List<String>) doc.get("PeliculasFav");
                        if (favoritas != null && favoritas.contains(tituloPelicula)) {
                            isFavorita = true;
                            btnFavorita.setText("Eliminar de favoritas");
                            btnFavorita.setOnClickListener(v -> eliminarDeFavoritas(btnFavorita));
                        } else {
                            isFavorita = false;
                            btnFavorita.setText("Agregar a favoritas");
                            btnFavorita.setOnClickListener(v -> agregarAFavoritas(btnFavorita));
                        }
                    }
                });
    }

    private void agregarAFavoritas(Button btnFavorita) {
        if (mAuth.getCurrentUser() == null || tituloPelicula == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId)
                .update("PeliculasFav", FieldValue.arrayUnion(tituloPelicula))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Película agregada a favoritas", Toast.LENGTH_SHORT).show();
                    isFavorita = true;
                    btnFavorita.setText("Eliminar de favoritas");
                });
    }

    private void eliminarDeFavoritas(Button btnFavorita) {
        if (mAuth.getCurrentUser() == null || tituloPelicula == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId)
                .update("PeliculasFav", FieldValue.arrayRemove(tituloPelicula))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Película eliminada de favoritas", Toast.LENGTH_SHORT).show();
                    isFavorita = false;
                    btnFavorita.setText("Agregar a favoritas");
                });
    }
}