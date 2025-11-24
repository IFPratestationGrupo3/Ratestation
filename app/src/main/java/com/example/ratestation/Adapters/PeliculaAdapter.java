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
import com.example.ratestation.Apis.TMDB_API;
import com.example.ratestation.Models.Pelicula;
import com.example.ratestation.R;

import org.json.JSONArray;
import org.json.JSONObject;

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

        String posterUrl = pelicula.getPosterUrl();
        Glide.with(context)
                .load(posterUrl)
                .placeholder(R.drawable.ratelogo)
                .into(holder.imgPoster);

        holder.txtTitulo.setText(pelicula.getTitulo());
        holder.txtPuntuacion.setText("⭐ " + pelicula.getPuntuacion());

        holder.itemView.setOnClickListener(v -> {
            // Cargar detalles en un hilo separado
            new Thread(() -> {
                try {
                    String detallesJson = TMDB_API.fetchMovieDetails(pelicula.getId());
                    JSONObject json = new JSONObject(detallesJson);

                    // Director
                    JSONArray crew = json.getJSONObject("credits").getJSONArray("crew");
                    String director = "";
                    for (int i = 0; i < crew.length(); i++) {
                        JSONObject c = crew.getJSONObject(i);
                        if ("Director".equals(c.getString("job"))) {
                            director = c.getString("name");
                            break;
                        }
                    }
                    pelicula.setDirector(director);

                    // Géneros
                    JSONArray genresArray = json.getJSONArray("genres");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < genresArray.length(); i++) {
                        sb.append(genresArray.getJSONObject(i).getString("name"));
                        if (i < genresArray.length() - 1) sb.append(", ");
                    }
                    pelicula.setGeneros(sb.toString());

                    // Lanzar Activity en hilo principal
                    holder.itemView.post(() -> {
                        Intent intent = new Intent(context, Activity_Pelicula.class);
                        intent.putExtra("titulo", pelicula.getTitulo());
                        intent.putExtra("poster", posterUrl);
                        intent.putExtra("anio", pelicula.getFechaEstreno() != null ? pelicula.getFechaEstreno().substring(0,4) : "");
                        intent.putExtra("sinopsis", pelicula.getSinopsis());
                        intent.putExtra("director", pelicula.getDirector());
                        intent.putExtra("generos", pelicula.getGeneros());
                        context.startActivity(intent);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    @Override
    public int getItemCount() { return peliculas.size(); }

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