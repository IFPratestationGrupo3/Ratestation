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

public class Activity_Juego extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        // -------------------------------
        // Inicialización Firebase
        // -------------------------------
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // -------------------------------
        // Referencias a elementos de UI
        // -------------------------------
        TextView txtTitulo = findViewById(R.id.txtTituloDetalle);
        ImageView imgPortada = findViewById(R.id.imgPortadaDetalle);
        TextView txtFecha = findViewById(R.id.txtFechaDetalle);
        TextView txtGenero = findViewById(R.id.txtGeneroDetalle);
        TextView txtPlataformas = findViewById(R.id.txtPlataformasDetalle);
        TextView txtCalificacion = findViewById(R.id.txtCalificacionDetalle);

        Button btnVolver = findViewById(R.id.btnVolver);
        Button btnFavorito = findViewById(R.id.btnFavoritoJuego);

        // -------------------------------
        // Recibir datos enviados desde el Adapter
        // -------------------------------
        String titulo = getIntent().getStringExtra("titulo");
        String portada = getIntent().getStringExtra("portada");
        String fecha = getIntent().getStringExtra("fecha");
        String genero = getIntent().getStringExtra("genero");
        String plataformas = getIntent().getStringExtra("plataformas");
        String calificacion = getIntent().getStringExtra("calificacion");

        // -------------------------------
        // Mostrar datos en pantalla
        // -------------------------------
        txtTitulo.setText(titulo);
        txtFecha.setText("Fecha de lanzamiento: " + fecha);
        txtGenero.setText("Género: " + genero);
        txtPlataformas.setText("Plataformas: " + plataformas);
        txtCalificacion.setText("Calificación: " + calificacion);

        // Cargar imagen con Glide
        Glide.with(this).load(portada).into(imgPortada);

        // -------------------------------
        // Botón para regresar a la pantalla anterior
        // -------------------------------
        btnVolver.setOnClickListener(v -> finish());

        // -------------------------------
        // Comprobar si el juego ya está en favoritos
        // -------------------------------
        comprobarFavorito(titulo, btnFavorito);

        // -------------------------------
        // Añadir a favoritos al hacer click
        // (temporal, luego se reemplaza dependiendo del estado)
        // -------------------------------
        btnFavorito.setOnClickListener(v -> agregarAFavoritos(titulo, btnFavorito));
    }

    /**
     * Comprueba si el juego está en la lista de favoritos del usuario.
     * Cambia el texto y funcionalidad del botón dependiendo de si está añadido o no.
     */
    @SuppressWarnings("unchecked")
    private void comprobarFavorito(String tituloJuego, Button btnFavorito) {

        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios")
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {

                    // Obtener lista de favoritos del documento
                    List<String> favoritos = (List<String>) doc.get("JuegosFav");

                    // Si contiene este juego → botón será "Eliminar"
                    if (favoritos != null && favoritos.contains(tituloJuego)) {

                        btnFavorito.setText("Eliminar de favoritos");
                        btnFavorito.setOnClickListener(v ->
                                eliminarDeFavoritos(tituloJuego, btnFavorito)
                        );

                    } else {

                        // Si NO está → botón será "Agregar"
                        btnFavorito.setText("Añadir a favoritos");
                        btnFavorito.setOnClickListener(v ->
                                agregarAFavoritos(tituloJuego, btnFavorito)
                        );
                    }
                });
    }

    /**
     * Añade el juego a la lista de favoritos del usuario.
     */
    private void agregarAFavoritos(String titulo, Button btnFavorito) {

        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios")
                .document(userId)
                .update("JuegosFav", FieldValue.arrayUnion(titulo))
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(this, "Juego añadido a favoritos", Toast.LENGTH_SHORT).show();

                    // Refresca el botón para mostrar "Eliminar"
                    comprobarFavorito(titulo, btnFavorito);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error añadiendo a favoritos", Toast.LENGTH_SHORT).show()
                );
    }

    /**
     * Elimina el juego de la lista de favoritos del usuario.
     */
    private void eliminarDeFavoritos(String titulo, Button btnFavorito) {

        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios")
                .document(userId)
                .update("JuegosFav", FieldValue.arrayRemove(titulo))
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(this, "Juego eliminado de favoritos", Toast.LENGTH_SHORT).show();

                    // Actualiza el botón para mostrar "Añadir a favoritos"
                    btnFavorito.setText("Añadir a favoritos");
                    btnFavorito.setOnClickListener(v ->
                            agregarAFavoritos(titulo, btnFavorito)
                    );

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error eliminando de favoritos", Toast.LENGTH_SHORT).show()
                );
    }
}