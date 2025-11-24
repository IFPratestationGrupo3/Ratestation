package com.example.ratestation.Activities;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ratestation.R;

public class Activity_Pelicula extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        ImageView imgPoster = findViewById(R.id.imgPosterDetalle);
        TextView txtTitulo = findViewById(R.id.txtTituloDetalle);
        TextView txtAnio = findViewById(R.id.txtAnio);
        TextView txtDirector = findViewById(R.id.txtDirector);
        TextView txtGenero = findViewById(R.id.txtGenero);
        TextView txtSipnosis = findViewById(R.id.txtSipnosis);
        Button btnVolver = findViewById(R.id.btnVolver);

        String titulo = getIntent().getStringExtra("titulo");
        String poster = getIntent().getStringExtra("poster");
        String anio = getIntent().getStringExtra("anio");
        String director = getIntent().getStringExtra("director");
        String generos = getIntent().getStringExtra("generos");
        String sinopsis = getIntent().getStringExtra("sinopsis");

        txtTitulo.setText(titulo);
        txtAnio.setText("Año: " + anio);
        txtDirector.setText("Director: " + director);
        txtGenero.setText("Género: " + generos);
        txtSipnosis.setText("Sinopsis: " + sinopsis);

        Glide.with(this).load(poster).into(imgPoster);

        btnVolver.setOnClickListener(v -> finish());
    }
}