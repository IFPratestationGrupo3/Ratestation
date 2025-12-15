package com.example.ratestation.Adapters.Favoritos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ratestation.Activities.Activity_Juego;
import com.example.ratestation.R;

import java.util.List;

public class FavJuegosAdapter extends RecyclerView.Adapter<FavJuegosAdapter.ViewHolder> {

    private final Context context;
    private final List<JuegoFavorito> juegos;

    // Clase para almacenar todos los datos del juego
    public static class JuegoFavorito {
        private final String titulo;
        private final String portadaUrl;
        private final String fecha;
        private final String genero;
        private final String plataformas;
        private final String calificacion;

        public JuegoFavorito(String titulo, String portadaUrl, String fecha, String genero, String plataformas, String calificacion) {
            this.titulo = titulo;
            this.portadaUrl = portadaUrl;
            this.fecha = fecha;
            this.genero = genero;
            this.plataformas = plataformas;
            this.calificacion = calificacion;
        }

        // Getters
        public String getTitulo() { return titulo; }
        public String getPortadaUrl() { return portadaUrl; }
        public String getFecha() { return fecha; }
        public String getGenero() { return genero; }
        public String getPlataformas() { return plataformas; }
        public String getCalificacion() { return calificacion; }
    }

    public FavJuegosAdapter(Context context, List<JuegoFavorito> juegos) {
        this.context = context;
        this.juegos = juegos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorito_juego, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JuegoFavorito juego = juegos.get(position);
        holder.txtTitulo.setText(juego.getTitulo());

        Glide.with(context)
                .load(juego.getPortadaUrl())
                .placeholder(R.drawable.ic_launcher_background) 
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgPoster);

        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            JuegoFavorito clickedJuego = juegos.get(currentPosition);

            Intent intent = new Intent(context, Activity_Juego.class);
            intent.putExtra("titulo", clickedJuego.getTitulo());
            intent.putExtra("portada", clickedJuego.getPortadaUrl());
            intent.putExtra("fecha", clickedJuego.getFecha());
            intent.putExtra("genero", clickedJuego.getGenero());
            intent.putExtra("plataformas", clickedJuego.getPlataformas());
            intent.putExtra("calificacion", clickedJuego.getCalificacion());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return juegos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo;
        ImageView imgPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            imgPoster = itemView.findViewById(R.id.imgPoster);
        }
    }
}
