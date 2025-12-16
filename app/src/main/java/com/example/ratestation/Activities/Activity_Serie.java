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

public class Activity_Serie extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private boolean isFavorita = false;
    private String tituloSerie;

    private Handler toastHandler = new Handler();
    private Runnable toastRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);

        // Inicialización Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referencias a elementos de UI
        RatingBar ratingBar = findViewById(R.id.ratingBarSerie);
        ImageView imgPoster = findViewById(R.id.imgPosterSerieDetalle);
        TextView txtTitulo = findViewById(R.id.txtTituloSerieDetalle);
        TextView txtFecha = findViewById(R.id.txtFechaSerie);
        TextView txtDirector = findViewById(R.id.txtDirectorSerie);
        TextView txtGenero = findViewById(R.id.txtGeneroSerie);
        TextView txtSinopsis = findViewById(R.id.txtSinopsisSerie);
        Button btnVolver = findViewById(R.id.btnVolverSerie);
        Button btnFavorita = findViewById(R.id.btnFavoritaSerie);

        // Recibir datos enviados desde el Adapter
        tituloSerie = getIntent().getStringExtra("titulo");
        String poster = getIntent().getStringExtra("poster");
        String anio = getIntent().getStringExtra("anio");
        String director = getIntent().getStringExtra("director");
        String generos = getIntent().getStringExtra("generos");
        String sinopsis = getIntent().getStringExtra("sinopsis");

        // Mostrar datos en pantalla
        txtTitulo.setText(tituloSerie != null ? tituloSerie : "Título no disponible");
        txtFecha.setText(anio != null ? "Año: " + anio : "Año no disponible");
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
            guardarPuntuacionSerie(tituloSerie, rating);

            if (toastRunnable != null) toastHandler.removeCallbacks(toastRunnable);

            float puntuacionFinal = rating;
            toastRunnable = () -> Toast.makeText(
                    Activity_Serie.this,
                    "Puntuación guardada: " + puntuacionFinal,
                    Toast.LENGTH_SHORT
            ).show();

            toastHandler.postDelayed(toastRunnable, 2000);
        });

        // Cargar puntuación guardada en Firebase
        cargarPuntuacionGuardada(ratingBar);
    }

    // Guardar la puntuación seleccionada en Firebase
    private void guardarPuntuacionSerie(String tituloSerie, float puntuacion) {
        if (mAuth.getCurrentUser() == null || tituloSerie == null) return;

        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> nuevaPuntuacion = new HashMap<>();
        nuevaPuntuacion.put(tituloSerie, puntuacion);

        db.collection("usuarios").document(userId)
                .set(Collections.singletonMap("PuntuacionesSeries", nuevaPuntuacion), SetOptions.merge())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error guardando puntuación", Toast.LENGTH_SHORT).show()
                );
    }

    // Cargar la puntuación guardada desde Firebase
    private void cargarPuntuacionGuardada(RatingBar ratingBar) {
        if (mAuth.getCurrentUser() == null || tituloSerie == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Map<String, Object> puntuaciones =
                                (Map<String, Object>) doc.get("PuntuacionesSeries");

                        if (puntuaciones != null && puntuaciones.containsKey(tituloSerie)) {
                            Double puntuacionDouble = (Double) puntuaciones.get(tituloSerie);
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
        if (mAuth.getCurrentUser() == null || tituloSerie == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        List<String> favoritas = (List<String>) doc.get("SeriesFav");
                        if (favoritas != null && favoritas.contains(tituloSerie)) {
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
        if (mAuth.getCurrentUser() == null || tituloSerie == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId)
                .update("SeriesFav", FieldValue.arrayUnion(tituloSerie))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Serie agregada a favoritas", Toast.LENGTH_SHORT).show();
                    isFavorita = true;
                    btnFavorita.setText("Eliminar de favoritas");
                });
    }

    private void eliminarDeFavoritas(Button btnFavorita) {
        if (mAuth.getCurrentUser() == null || tituloSerie == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId)
                .update("SeriesFav", FieldValue.arrayRemove(tituloSerie))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Serie eliminada de favoritas", Toast.LENGTH_SHORT).show();
                    isFavorita = false;
                    btnFavorita.setText("Agregar a favoritas");
                });
    }
}