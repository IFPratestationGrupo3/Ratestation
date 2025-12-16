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
import com.example.ratestation.Activities.Activity_Pelicula;
import com.example.ratestation.R;

import java.util.List;

public class FavPeliculasAdapter extends RecyclerView.Adapter<FavPeliculasAdapter.ViewHolder> {

    private Context context;
    private List<PeliculaFavorita> peliculas;

    // Clase para almacenar TODOS los datos de cada película favorita
    public static class PeliculaFavorita {
        private final String titulo;
        private final String posterUrl;
        private final String anio;
        private final String sinopsis;
        private final String director;
        private final String generos;


        public PeliculaFavorita(String titulo, String posterUrl, String anio, String sinopsis, String director, String generos) {
            this.titulo = titulo;
            this.posterUrl = posterUrl;
            this.anio = anio;
            this.sinopsis = sinopsis;
            this.director = director;
            this.generos = generos;
        }

        // Getters
        public String getTitulo() { return titulo; }
        public String getPosterUrl() { return posterUrl; }
        public String getAnio() { return anio; }
        public String getSinopsis() { return sinopsis; }
        public String getDirector() { return director; }
        public String getGeneros() { return generos; }
    }

    public FavPeliculasAdapter(Context context, List<PeliculaFavorita> peliculas) {
        this.context = context;
        this.peliculas = peliculas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorito_pelicula, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PeliculaFavorita pelicula = peliculas.get(position);
        holder.txtTitulo.setText(pelicula.getTitulo());

        Glide.with(context)
                .load(pelicula.getPosterUrl())
                .into(holder.imgPoster);

        // Lógica de clic simplificada: todos los datos ya están cargados
        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            PeliculaFavorita clickedPelicula = peliculas.get(currentPosition);

            Intent intent = new Intent(context, Activity_Pelicula.class);
            intent.putExtra("titulo", clickedPelicula.getTitulo());
            intent.putExtra("poster", clickedPelicula.getPosterUrl());
            intent.putExtra("anio", clickedPelicula.getAnio());
            intent.putExtra("sinopsis", clickedPelicula.getSinopsis());
            intent.putExtra("director", clickedPelicula.getDirector());
            intent.putExtra("generos", clickedPelicula.getGeneros());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
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
