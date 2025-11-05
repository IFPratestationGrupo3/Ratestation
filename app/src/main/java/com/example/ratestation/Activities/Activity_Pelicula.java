package com.example.ratestation.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.ratestation.R;

import com.bumptech.glide.Glide;

public class Activity_Pelicula extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        // Referencias
        TextView txtTitulo = findViewById(R.id.txtTituloDetalle);
        ImageView imgPoster = findViewById(R.id.imgPosterDetalle);
        Button btnVolver = findViewById(R.id.btnVolver);

        // Recuperar los extras
        String titulo = getIntent().getStringExtra("titulo");
        String poster = getIntent().getStringExtra("poster");

        txtTitulo.setText(titulo);
        Glide.with(this).load(poster).into(imgPoster);

        btnVolver.setOnClickListener(v -> finish());
    }
}