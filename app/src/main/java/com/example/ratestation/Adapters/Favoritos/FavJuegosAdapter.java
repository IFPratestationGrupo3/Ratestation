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

import com.example.ratestation.Activities.Activity_Juego;
import com.example.ratestation.Apis.RAWG_API;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class FavJuegosAdapter extends RecyclerView.Adapter<FavJuegosAdapter.ViewHolder> {

    private final Context context;
    private final List<String> titulos;

    public FavJuegosAdapter(Context context, List<String> titulos) {
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

        holder.itemView.setOnClickListener(v -> new Thread(() -> {
            try {
                // Buscar juego por título
                String searchJson = RAWG_API.searchGames(titulo);
                JSONObject root = new JSONObject(searchJson);
                JSONArray results = root.getJSONArray("results");

                if (results.length() == 0) {
                    holder.itemView.post(() ->
                            Toast.makeText(context, "No se encontró información para " + titulo, Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                JSONObject juegoJson = results.getJSONObject(0);
                int id = juegoJson.getInt("id");

                // Datos básicos
                String portada = juegoJson.optString("background_image", "");
                String fecha = juegoJson.optString("released", "Desconocida");
                String calificacion = String.valueOf(juegoJson.optDouble("rating", 0));

                // Obtener detalles completos
                String detallesJson = RAWG_API.fetchGameDetails(id);
                JSONObject detallesObj = new JSONObject(detallesJson);

                // Géneros
                JSONArray genresArray = detallesObj.getJSONArray("genres");
                StringBuilder sbGenero = new StringBuilder();
                for (int i = 0; i < genresArray.length(); i++) {
                    sbGenero.append(genresArray.getJSONObject(i).getString("name"));
                    if (i < genresArray.length() - 1) sbGenero.append(", ");
                }
                String generos = sbGenero.toString();

                // Plataformas
                JSONArray plataformasArray = detallesObj.getJSONArray("platforms");
                StringBuilder sbPlat = new StringBuilder();
                for (int i = 0; i < plataformasArray.length(); i++) {
                    JSONObject platObj = plataformasArray.getJSONObject(i).getJSONObject("platform");
                    sbPlat.append(platObj.getString("name"));
                    if (i < plataformasArray.length() - 1) sbPlat.append(", ");
                }
                String plataformas = sbPlat.toString();

                // Lanzar Activity en el hilo principal
                String finalPortada = portada;
                String finalFecha = fecha;
                String finalGenero = generos;
                String finalPlataformas = plataformas;
                String finalCalificacion = calificacion;

                holder.itemView.post(() -> {
                    Intent intent = new Intent(context, Activity_Juego.class);
                    intent.putExtra("titulo", titulo);
                    intent.putExtra("portada", finalPortada);
                    intent.putExtra("fecha", finalFecha);
                    intent.putExtra("genero", finalGenero);
                    intent.putExtra("plataformas", finalPlataformas);
                    intent.putExtra("calificacion", finalCalificacion);
                    context.startActivity(intent);
                });

            } catch (Exception e) {
                e.printStackTrace();
                holder.itemView.post(() ->
                        Toast.makeText(context, "Error al obtener información de " + titulo, Toast.LENGTH_SHORT).show()
                );
            }
        }).start());
    }

    @Override
    public int getItemCount() {
        return titulos.size();
    }

    // -------------------------
    // ViewHolder interno
    // -------------------------
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(android.R.id.text1);
        }
    }
}