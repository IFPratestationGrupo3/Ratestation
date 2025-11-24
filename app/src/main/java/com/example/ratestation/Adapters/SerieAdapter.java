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
import com.example.ratestation.Activities.Activity_Serie;
import com.example.ratestation.Apis.TMDB_API;
import com.example.ratestation.Models.Serie;
import com.example.ratestation.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SerieAdapter extends RecyclerView.Adapter<SerieAdapter.ViewHolder> {

    private Context context;
    private List<Serie> series;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public SerieAdapter(Context context, List<Serie> series) {
        this.context = context;
        this.series = series;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_serie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Serie serie = series.get(position);

        // Cargar poster y datos básicos
        Glide.with(context)
                .load(serie.getPosterUrl())
                .placeholder(R.drawable.ratelogo)
                .into(holder.imgPoster);

        holder.txtTitulo.setText(serie.getTitulo());
        holder.txtPuntuacion.setText("⭐ " + serie.getPuntuacion());

        holder.itemView.setOnClickListener(v -> loadSerieDetailsAndOpenActivity(serie));
    }

    private void loadSerieDetailsAndOpenActivity(Serie serie) {
        executor.execute(() -> {
            try {
                // Obtener detalles de la serie desde TMDB
                String detallesJson = TMDB_API.fetchSeriesDetails(serie.getId());
                JSONObject json = new JSONObject(detallesJson);

                // Director / showrunner
                JSONArray creators = json.getJSONArray("created_by");
                String director = "";
                if (creators.length() > 0) {
                    director = creators.getJSONObject(0).getString("name");
                }
                serie.setDirector(director);

                // Géneros
                JSONArray genresArray = json.getJSONArray("genres");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < genresArray.length(); i++) {
                    sb.append(genresArray.getJSONObject(i).getString("name"));
                    if (i < genresArray.length() - 1) sb.append(", ");
                }
                serie.setGeneros(sb.toString());

                // Lanzar Activity en UI thread
                ((android.app.Activity) context).runOnUiThread(() -> {
                    Intent intent = new Intent(context, Activity_Serie.class);
                    intent.putExtra("titulo", serie.getTitulo());
                    intent.putExtra("poster", serie.getPosterUrl());
                    intent.putExtra("anio", serie.getFechaEstreno() != null && serie.getFechaEstreno().length() >= 4 ?
                            serie.getFechaEstreno().substring(0, 4) : "");
                    intent.putExtra("sinopsis", serie.getSinopsis());
                    intent.putExtra("director", serie.getDirector());
                    intent.putExtra("generos", serie.getGeneros());
                    context.startActivity(intent);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return series.size();
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