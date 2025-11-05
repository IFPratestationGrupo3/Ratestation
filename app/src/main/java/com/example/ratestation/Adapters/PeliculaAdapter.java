package com.example.ratestation.Adapters;

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
import com.example.ratestation.Models.Pelicula;
import com.example.ratestation.R;

import java.util.List;

public class PeliculaAdapter extends RecyclerView.Adapter<PeliculaAdapter.ViewHolder> {
    private Context context;
    private List<Pelicula> peliculas;

    public PeliculaAdapter(Context context, List<Pelicula> peliculas) {
        this.context = context;
        this.peliculas = peliculas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pelicula, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pelicula pelicula = peliculas.get(position);


        String posterUrl = "https://image.tmdb.org/t/p/w500" + pelicula.getPosterPath();
        Glide.with(context)
                .load(posterUrl)
                .placeholder(R.drawable.ratelogo)
                .into(holder.imgPoster);


        holder.txtTitulo.setText(pelicula.getTitulo());
        holder.txtPuntuacion.setText("⭐ " + pelicula.getPuntuacion());
        // ✅ Evento click en la película
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Activity_Pelicula.class);
            // puedes enviar datos básicos de la película
            intent.putExtra("titulo", pelicula.getTitulo());
            intent.putExtra("poster", pelicula.getPosterUrl());
            intent.putExtra("sinopsis", pelicula.getSinopsis());
            intent.putExtra("fecha", pelicula.getFechaEstreno());
            intent.putExtra("puntuacion", pelicula.getPuntuacion());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView txtTitulo, txtPuntuacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtPuntuacion = itemView.findViewById(R.id.txtPuntuacion);
        }
    }
}