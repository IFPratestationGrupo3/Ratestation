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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.List;

public class Activity_Pelicula extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Views
        ImageView imgPoster = findViewById(R.id.imgPosterDetalle);
        TextView txtTitulo = findViewById(R.id.txtTituloDetalle);
        TextView txtAnio = findViewById(R.id.txtAnio);
        TextView txtDirector = findViewById(R.id.txtDirector);
        TextView txtGenero = findViewById(R.id.txtGenero);
        TextView txtSipnosis = findViewById(R.id.txtSinopsis);
        Button btnVolver = findViewById(R.id.btnVolver);

        // Botón favoritas
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
        txtSipnosis.setText(sinopsis);
        Glide.with(this).load(poster).into(imgPoster);

        // Botón volver
        btnVolver.setOnClickListener(v -> finish());

        // Comprobar si ya está en favoritas
        comprobarFavorita(titulo, btnFavorita);

        // Agregar a favoritas
        btnFavorita.setOnClickListener(v -> agregarAFavoritas(titulo, btnFavorita));
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
                    // Actualiza el botón
                    comprobarFavorita(tituloPelicula, btnFavorita);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error agregando a favoritas", Toast.LENGTH_SHORT).show();
                });
    }
}