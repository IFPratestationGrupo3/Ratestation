package com.example.ratestation.Activities;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ratestation.R;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Pelicula extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Handler toastHandler = new Handler();
    private Runnable toastRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Slider puntuacion
        Slider sliderPuntuacion = findViewById(R.id.sliderPuntuacion);
        sliderPuntuacion.setValue(3.0f); // puntuación por defecto

        // Views
        ImageView imgPoster = findViewById(R.id.imgPosterDetalle);
        TextView txtTitulo = findViewById(R.id.txtTituloDetalle);
        TextView txtAnio = findViewById(R.id.txtAnio);
        TextView txtDirector = findViewById(R.id.txtDirector);
        TextView txtGenero = findViewById(R.id.txtGenero);
        TextView txtSinopsis = findViewById(R.id.txtSinopsis);
        Button btnVolver = findViewById(R.id.btnVolver);
        Button btnFavorita = findViewById(R.id.btnFavorita);

        // Datos de la película
        String titulo = getIntent().getStringExtra("titulo");
        String poster = getIntent().getStringExtra("poster");
        String anio = getIntent().getStringExtra("anio");
        String director = getIntent().getStringExtra("director");
        String generos = getIntent().getStringExtra("generos");
        String sinopsis = getIntent().getStringExtra("sinopsis");

        // Mostrar datos
        txtTitulo.setText(titulo);
        txtAnio.setText("Año: " + anio);
        txtDirector.setText("Director: " + director);
        txtGenero.setText("Género: " + generos);
        txtSinopsis.setText(sinopsis);
        Glide.with(this).load(poster).into(imgPoster);

        // Botón volver
        btnVolver.setOnClickListener(v -> finish());

        // Comprobar si ya está en favoritas
        comprobarFavorita(titulo, btnFavorita);

        // Agregar a favoritas
        btnFavorita.setOnClickListener(v -> agregarAFavoritas(titulo, btnFavorita));

        // Listener del slider: guardar y mostrar Toast 3s después de soltar
        sliderPuntuacion.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(Slider slider) {
                // No hacemos nada al empezar a tocar
            }

            @Override
            public void onStopTrackingTouch(Slider slider) {
                float value = slider.getValue();

                // Guardamos la puntuación inmediatamente
                guardarPuntuacion(titulo, value);

                // Cancelamos cualquier Toast pendiente
                if (toastRunnable != null) {
                    toastHandler.removeCallbacks(toastRunnable);
                }

                // Creamos un Runnable para mostrar el Toast después de 3 segundos
                float puntuacionFinal = value;
                toastRunnable = () -> Toast.makeText(Activity_Pelicula.this,
                        "Puntuación guardada: " + puntuacionFinal, Toast.LENGTH_SHORT).show();

                toastHandler.postDelayed(toastRunnable, 3000);
            }
        });
    }

    private void guardarPuntuacion(String tituloPelicula, float puntuacion) {
        if (mAuth.getCurrentUser() == null) return;
        String currentUserId = mAuth.getCurrentUser().getUid();

        Map<String, Object> nuevaPuntuacion = new HashMap<>();
        nuevaPuntuacion.put(tituloPelicula, puntuacion);

        db.collection("usuarios").document(currentUserId)
                .set(Collections.singletonMap("Puntuaciones", nuevaPuntuacion), com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    // No mostramos Toast aquí, ya lo hace el handler del slider
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error guardando puntuación", Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressWarnings("unchecked")
    private void comprobarFavorita(String tituloPelicula, Button btnFavorita) {
        if (mAuth.getCurrentUser() == null) return;
        String currentUserId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(currentUserId).get()
                .addOnSuccessListener(doc -> {
                    List<String> favoritas = (List<String>) doc.get("PeliculasFav");
                    if (favoritas != null && favoritas.contains(tituloPelicula)) {
                        btnFavorita.setText("Eliminar de favoritas");
                        btnFavorita.setOnClickListener(v -> eliminarDeFavoritas(tituloPelicula, btnFavorita));
                    } else {
                        btnFavorita.setText("Agregar a favoritas");
                        btnFavorita.setOnClickListener(v -> agregarAFavoritas(tituloPelicula, btnFavorita));
                    }
                });
    }

    private void eliminarDeFavoritas(String tituloPelicula, Button btnFavorita) {
        if (mAuth.getCurrentUser() == null) return;
        String currentUserId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(currentUserId)
                .update("PeliculasFav", FieldValue.arrayRemove(tituloPelicula))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Película eliminada de favoritas", Toast.LENGTH_SHORT).show();
                    btnFavorita.setText("Agregar a favoritas");
                    btnFavorita.setOnClickListener(v -> agregarAFavoritas(tituloPelicula, btnFavorita));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error eliminando de favoritas", Toast.LENGTH_SHORT).show();
                });
    }

    private void agregarAFavoritas(String tituloPelicula, Button btnFavorita) {
        if (mAuth.getCurrentUser() == null) return;
        String currentUserId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(currentUserId)
                .update("PeliculasFav", FieldValue.arrayUnion(tituloPelicula))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Película agregada a favoritas", Toast.LENGTH_SHORT).show();
                    comprobarFavorita(tituloPelicula, btnFavorita);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error agregando a favoritas", Toast.LENGTH_SHORT).show();
                });
    }
}