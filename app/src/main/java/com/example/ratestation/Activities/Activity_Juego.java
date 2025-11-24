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

        // Referencias
        TextView txtTitulo = findViewById(R.id.txtTituloDetalle);
        ImageView imgPortada = findViewById(R.id.imgPortadaDetalle);
        TextView txtPuntuacion = findViewById(R.id.txtPuntuacionDetalle);
        TextView txtFecha = findViewById(R.id.txtFechaDetalle);
        Button btnVolver = findViewById(R.id.btnVolver);

        // Recuperar extras
        String titulo = getIntent().getStringExtra("titulo");
        String portada = getIntent().getStringExtra("portada");
        String fecha = getIntent().getStringExtra("fecha");
        float puntuacion = getIntent().getFloatExtra("puntuacion", 0);

        txtTitulo.setText(titulo);
        txtFecha.setText("Lanzamiento: " + fecha);
        txtPuntuacion.setText("⭐ " + puntuacion);
        Glide.with(this).load(portada).into(imgPortada);

        btnVolver.setOnClickListener(v -> finish());
    }
}