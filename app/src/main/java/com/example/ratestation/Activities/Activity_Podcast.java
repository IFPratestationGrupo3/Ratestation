package com.example.ratestation.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ratestation.R;

public class Activity_Podcast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);

        // Referencias a las vistas
        ImageView imgPortada = findViewById(R.id.imgPortadaDetallePodcast);
        TextView txtTitulo = findViewById(R.id.txtTituloDetallePodcast);
        TextView txtCreador = findViewById(R.id.txtCreadorDetallePodcast);
        TextView txtGenero = findViewById(R.id.txtGeneroDetallePodcast);
        Button btnVolver = findViewById(R.id.btnVolverPodcast);

        // Recibir datos del Intent
        String titulo = getIntent().getStringExtra("titulo");
        String creador = getIntent().getStringExtra("creador");
        String artworkUrl = getIntent().getStringExtra("artworkUrl");
        String genero = getIntent().getStringExtra("genero");

        // Mostrar datos en las vistas
        txtTitulo.setText(titulo);
        txtCreador.setText(creador);
        txtGenero.setText("Género: " + genero);

        Glide.with(this)
                .load(artworkUrl)
                .placeholder(R.drawable.ic_launcher_background) // Placeholder mientras carga
                .into(imgPortada);

        // Configurar botón para volver
        btnVolver.setOnClickListener(v -> finish());
    }
}
