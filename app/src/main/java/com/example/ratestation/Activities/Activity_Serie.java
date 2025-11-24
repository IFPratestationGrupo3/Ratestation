package com.example.ratestation.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ratestation.R;

public class Activity_Serie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);

        ImageView imgPoster = findViewById(R.id.imgPosterSerieDetalle);
        TextView txtTitulo = findViewById(R.id.txtTituloSerieDetalle);
        TextView txtFecha = findViewById(R.id.txtFechaSerie);
        TextView txtDirector = findViewById(R.id.txtDirectorSerie);
        TextView txtGenero = findViewById(R.id.txtGeneroSerie);
        TextView txtSinopsis = findViewById(R.id.txtSinopsisSerie);
        Button btnVolver = findViewById(R.id.btnVolverSerie);

        // Recuperar datos del Intent
        String titulo = getIntent().getStringExtra("titulo");
        String poster = getIntent().getStringExtra("poster");
        String anio = getIntent().getStringExtra("anio"); // clave corregida
        String director = getIntent().getStringExtra("director");
        String genero = getIntent().getStringExtra("generos");
        String sinopsis = getIntent().getStringExtra("sinopsis");

        // Mostrar datos
        txtTitulo.setText(titulo != null ? titulo : "Título no disponible");
        txtFecha.setText(anio != null && !anio.isEmpty() ? "Año: " + anio : "Año no disponible");
        txtDirector.setText(director != null && !director.isEmpty() ? "Director: " + director : "Director no disponible");
        txtGenero.setText(genero != null && !genero.isEmpty() ? "Género: " + genero : "Género no disponible");
        txtSinopsis.setText(sinopsis != null && !sinopsis.isEmpty() ? sinopsis : "Sinopsis no disponible");

        Glide.with(this)
                .load(poster != null ? poster : R.drawable.ratelogo)
                .placeholder(R.drawable.ratelogo)
                .into(imgPoster);

        btnVolver.setOnClickListener(v -> finish());
    }
}