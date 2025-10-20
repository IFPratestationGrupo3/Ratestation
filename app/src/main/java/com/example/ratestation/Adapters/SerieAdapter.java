package com.example.ratestation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ratestation.Models.Serie;
import com.example.ratestation.R;

import java.util.List;

public class SerieAdapter extends RecyclerView.Adapter<SerieAdapter.ViewHolder> {

    private Context context;
    private List<Serie> series;

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

        String posterUrl = "https://image.tmdb.org/t/p/w500" + serie.getPosterPath();
        Glide.with(context)
                .load(posterUrl)
                .placeholder(R.drawable.ratelogo)
                .into(holder.imgPoster);

        holder.txtTitulo.setText(serie.getTitulo());
        holder.txtPuntuacion.setText("⭐ " + serie.getPuntuacion());
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