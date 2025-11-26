package com.example.ratestation.Adapters.Favoritos;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratestation.Activities.Activity_Pelicula;
import com.example.ratestation.Apis.TMDB_API;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class FavSeriesAdapter extends RecyclerView.Adapter<FavSeriesAdapter.ViewHolder> {

    private Context context;
    private List<String> titulos;

    public FavSeriesAdapter(Context context, List<String> titulos) {
        this.context = context;
        this.titulos = titulos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String titulo = titulos.get(position);
        holder.txtTitulo.setText(titulo);

        holder.itemView.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    String searchJson = TMDB_API.buscarSeriePorTitulo(titulo);
                    JSONObject root = new JSONObject(searchJson);
                    JSONArray results = root.getJSONArray("results");

                    if (results.length() == 0) {
                        holder.itemView.post(() ->
                                Toast.makeText(context, "No se encontró información para " + titulo, Toast.LENGTH_SHORT).show()
                        );
                        return;
                    }

                    JSONObject serieJson = results.getJSONObject(0);
                    int id = serieJson.getInt("id");
                    String poster = "https://image.tmdb.org/t/p/w500" + serieJson.getString("poster_path");
                    String anio = serieJson.getString("first_air_date").substring(0, 4);
                    String sinopsis = serieJson.getString("overview");

                    // Obtener detalles completos
                    String detallesJson = TMDB_API.fetchSeriesDetails(id);
                    JSONObject detallesObj = new JSONObject(detallesJson);

                    // Director / creador principal
                    JSONArray creators = detallesObj.getJSONArray("created_by");
                    String creador = creators.length() > 0 ? creators.getJSONObject(0).getString("name") : "";

                    // Géneros
                    JSONArray genresArray = detallesObj.getJSONArray("genres");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < genresArray.length(); i++) {
                        sb.append(genresArray.getJSONObject(i).getString("name"));
                        if (i < genresArray.length() - 1) sb.append(", ");
                    }
                    String generos = sb.toString();

                    // Lanzar Activity en hilo principal
                    String finalPoster = poster;
                    String finalCreador = creador;
                    String finalGeneros = generos;
                    String finalAnio = anio;
                    String finalSinopsis = sinopsis;

                    holder.itemView.post(() -> {
                        Intent intent = new Intent(context, Activity_Pelicula.class);
                        intent.putExtra("titulo", titulo);
                        intent.putExtra("poster", finalPoster);
                        intent.putExtra("anio", finalAnio);
                        intent.putExtra("sinopsis", finalSinopsis);
                        intent.putExtra("director", finalCreador);
                        intent.putExtra("generos", finalGeneros);
                        context.startActivity(intent);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    holder.itemView.post(() ->
                            Toast.makeText(context, "Error al obtener información de " + titulo, Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return titulos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(android.R.id.text1);
        }
    }
}