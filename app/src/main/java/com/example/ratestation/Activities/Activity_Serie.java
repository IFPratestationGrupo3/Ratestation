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

        // Referencias UI
        TextView txtTitulo = findViewById(R.id.txtTituloSerieDetalle);
        ImageView imgPoster = findViewById(R.id.imgPosterSerieDetalle);
        TextView txtDescripcion = findViewById(R.id.txtDescripcionSerie);
        TextView txtFecha = findViewById(R.id.txtFechaSerie);
        Button btnVolver = findViewById(R.id.btnVolverSerie);

        // Recuperar los datos desde el intent
        String titulo = getIntent().getStringExtra("titulo");
        String poster = getIntent().getStringExtra("poster");
        String descripcion = getIntent().getStringExtra("descripcion");
        String fecha = getIntent().getStringExtra("fecha");

        // Mostrar datos
        txtTitulo.setText(titulo);
        txtDescripcion.setText(descripcion != null ? descripcion : "Sin descripción disponible");
        txtFecha.setText(fecha != null ? "Fecha: " + fecha : "Fecha no disponible");

        Glide.with(this)
                .load(poster)
                .placeholder(R.drawable.ic_logo_user) // opcional: imagen por defecto
                .into(imgPoster);

        // Botón para volver atrás
        btnVolver.setOnClickListener(v -> finish());
    }
}
