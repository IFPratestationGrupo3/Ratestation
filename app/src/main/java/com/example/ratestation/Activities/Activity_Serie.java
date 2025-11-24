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

public class Activity_Serie extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Views
        ImageView imgPoster = findViewById(R.id.imgPosterSerieDetalle);
        TextView txtTitulo = findViewById(R.id.txtTituloSerieDetalle);
        TextView txtFecha = findViewById(R.id.txtFechaSerie);
        TextView txtDirector = findViewById(R.id.txtDirectorSerie);
        TextView txtGenero = findViewById(R.id.txtGeneroSerie);
        TextView txtSinopsis = findViewById(R.id.txtSinopsisSerie);
        Button btnVolver = findViewById(R.id.btnVolverSerie);
        Button btnFavorita = findViewById(R.id.btnFavorita);

        // Recuperar datos del Intent
        String titulo = getIntent().getStringExtra("titulo");
        String poster = getIntent().getStringExtra("poster");
        String anio = getIntent().getStringExtra("anio");
        String director = getIntent().getStringExtra("director");
        String generos = getIntent().getStringExtra("generos");
        String sinopsis = getIntent().getStringExtra("sinopsis");

        // Mostrar datos
        txtTitulo.setText(titulo != null ? titulo : "Título no disponible");
        txtFecha.setText(anio != null ? "Año: " + anio : "Año no disponible");
        txtDirector.setText(director != null ? "Director: " + director : "Director no disponible");
        txtGenero.setText(generos != null ? "Género: " + generos : "Género no disponible");
        txtSinopsis.setText(sinopsis != null ? sinopsis : "Sinopsis no disponible");

        Glide.with(this)
                .load(poster != null ? poster : R.drawable.ratelogo)
                .placeholder(R.drawable.ratelogo)
                .into(imgPoster);

        btnVolver.setOnClickListener(v -> finish());

        // Comprobar si ya está en favoritas
        comprobarFavorita(titulo, btnFavorita);

        // Agregar a favoritas
        btnFavorita.setOnClickListener(v -> agregarAFavoritas(titulo, btnFavorita));
    }

    @SuppressWarnings("unchecked")
    private void comprobarFavorita(String tituloSerie, Button btnFavorita) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(doc -> {
                    List<String> favoritas = (List<String>) doc.get("SeriesFav");
                    if (favoritas != null && favoritas.contains(tituloSerie)) {
                        btnFavorita.setText("Eliminar de favoritas");
                        btnFavorita.setOnClickListener(v -> eliminarDeFavoritas(tituloSerie, btnFavorita));
                    } else {
                        btnFavorita.setText("Agregar a favoritas");
                        btnFavorita.setOnClickListener(v -> agregarAFavoritas(tituloSerie, btnFavorita));
                    }
                });
    }

    private void eliminarDeFavoritas(String tituloSerie, Button btnFavorita) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId)
                .update("SeriesFav", FieldValue.arrayRemove(tituloSerie))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Serie eliminada de favoritas", Toast.LENGTH_SHORT).show();
                    btnFavorita.setText("Agregar a favoritas");
                    btnFavorita.setOnClickListener(v -> agregarAFavoritas(tituloSerie, btnFavorita));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error eliminando de favoritas", Toast.LENGTH_SHORT).show();
                });
    }

    private void agregarAFavoritas(String tituloSerie, Button btnFavorita) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId)
                .update("SeriesFav", FieldValue.arrayUnion(tituloSerie))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Serie agregada a favoritas", Toast.LENGTH_SHORT).show();
                    comprobarFavorita(tituloSerie, btnFavorita);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error agregando a favoritas", Toast.LENGTH_SHORT).show();
                });
    }
}