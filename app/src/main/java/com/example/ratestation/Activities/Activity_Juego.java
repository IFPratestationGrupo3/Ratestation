package com.example.ratestation.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.ratestation.R;
import com.bumptech.glide.Glide;

public class Activity_Juego extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        // Referencias UI
        TextView txtTitulo = findViewById(R.id.txtTituloDetalle);
        ImageView imgPortada = findViewById(R.id.imgPortadaDetalle);
        TextView txtFecha = findViewById(R.id.txtFechaDetalle);
        TextView txtGenero = findViewById(R.id.txtGeneroDetalle);
        TextView txtPlataformas = findViewById(R.id.txtPlataformasDetalle);
        TextView txtCalificacion = findViewById(R.id.txtCalificacionDetalle);

        Button btnVolver = findViewById(R.id.btnVolver);

        // Extras desde el intent
        String titulo = getIntent().getStringExtra("titulo");
        String portada = getIntent().getStringExtra("portada");
        String fecha = getIntent().getStringExtra("fecha");
        String genero = getIntent().getStringExtra("genero");
        String plataformas = getIntent().getStringExtra("plataformas");
        String calificacion = getIntent().getStringExtra("calificacion");

        // Asignar datos
        txtTitulo.setText(titulo);
        txtFecha.setText("Fecha de lanzamiento: " + fecha);
        txtGenero.setText("Género: " + genero);
        txtPlataformas.setText("Plataformas: " + plataformas);
        txtCalificacion.setText("Calificación: " + calificacion);

        Glide.with(this).load(portada).into(imgPortada);

        // Botón volver
        btnVolver.setOnClickListener(v -> finish());
    }
}