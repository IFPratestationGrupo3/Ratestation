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
import com.example.ratestation.Activities.Activity_Pelicula; // Asumiendo que se reutiliza la misma activity
import com.example.ratestation.R;

import java.util.List;

public class FavSeriesAdapter extends RecyclerView.Adapter<FavSeriesAdapter.ViewHolder> {

    private Context context;
    private List<SerieFavorita> series;

    public static class SerieFavorita {
        private final String titulo;
        private final String posterUrl;
        private final String anio;
        private final String sinopsis;
        private final String creador;
        private final String generos;

        public SerieFavorita(String titulo, String posterUrl, String anio, String sinopsis, String creador, String generos) {
            this.titulo = titulo;
            this.posterUrl = posterUrl;
            this.anio = anio;
            this.sinopsis = sinopsis;
            this.creador = creador;
            this.generos = generos;
        }

        // Getters
        public String getTitulo() { return titulo; }
        public String getPosterUrl() { return posterUrl; }
        public String getAnio() { return anio; }
        public String getSinopsis() { return sinopsis; }
        public String getCreador() { return creador; }
        public String getGeneros() { return generos; }
    }

    public FavSeriesAdapter(Context context, List<SerieFavorita> series) {
        this.context = context;
        this.series = series;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorito_serie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SerieFavorita serie = series.get(position);
        holder.txtTitulo.setText(serie.getTitulo());

        Glide.with(context)
                .load(serie.getPosterUrl())
                .placeholder(R.drawable.ic_launcher_background) // <-- Imagen de placeholder
                .error(R.drawable.ic_launcher_background) // <-- Imagen de error
                .into(holder.imgPoster);

        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            SerieFavorita clickedSerie = series.get(currentPosition);

            Intent intent = new Intent(context, Activity_Pelicula.class);
            intent.putExtra("titulo", clickedSerie.getTitulo());
            intent.putExtra("poster", clickedSerie.getPosterUrl());
            intent.putExtra("anio", clickedSerie.getAnio());
            intent.putExtra("sinopsis", clickedSerie.getSinopsis());
            intent.putExtra("director", clickedSerie.getCreador()); // Pasando el creador como "director"
            intent.putExtra("generos", clickedSerie.getGeneros());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return series.size();
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
