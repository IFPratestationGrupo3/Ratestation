package com.example.ratestation.Adapters.Rate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class RatePeliculasAdapter extends RecyclerView.Adapter<RatePeliculasAdapter.ViewHolder> {

    private Context context;
    private List<String> titulos = new ArrayList<>();
    private List<Float> puntuaciones = new ArrayList<>();

    public RatePeliculasAdapter(Context context, Map<String, Float> puntuacionesMap) {
        this.context = context;
        for (Map.Entry<String, Float> entry : puntuacionesMap.entrySet()) {
            titulos.add(entry.getKey());
            puntuaciones.add(entry.getValue());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String titulo = titulos.get(position);
        Float puntuacion = puntuaciones.get(position);

        holder.title.setText(titulo);
        holder.score.setText("Puntuación: " + puntuacion);
    }

    @Override
    public int getItemCount() {
        return titulos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView score;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(android.R.id.text1);
            score = itemView.findViewById(android.R.id.text2);
        }
    }
}