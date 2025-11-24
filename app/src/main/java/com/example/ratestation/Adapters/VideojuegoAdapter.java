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
import com.example.ratestation.Activities.Activity_Juego;
import com.example.ratestation.Models.Videojuego;
import com.example.ratestation.R;

import java.util.List;

public class VideojuegoAdapter extends RecyclerView.Adapter<VideojuegoAdapter.ViewHolder> {

    private final Context context;
    private final List<Videojuego> juegos;

    public VideojuegoAdapter(Context context, List<Videojuego> juegos) {
        this.context = context;
        this.juegos = juegos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_videojuego, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Videojuego juego = juegos.get(position);

        // Imagen
        Glide.with(context)
                .load(juego.getImagenPortada())
                .placeholder(R.drawable.ratelogo)
                .into(holder.imgPortada);

        // Título
        holder.txtTitulo.setText(juego.getTitulo() != null ? juego.getTitulo() : "Sin título");

        // Puntuación
        holder.txtPuntuacion.setText("⭐ " + juego.getPuntuacion());

        // Click -> abrir detalles
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Activity_Juego.class);

            intent.putExtra("titulo", juego.getTitulo());
            intent.putExtra("portada", juego.getImagenPortada());
            intent.putExtra("fecha", juego.getFechaLanzamiento());
            intent.putExtra("genero", juego.getGenerosString());
            intent.putExtra("plataformas", juego.getPlataformasString());
            intent.putExtra("calificacion", juego.getCalidadTag());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return juegos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPortada;
        TextView txtTitulo, txtPuntuacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPortada = itemView.findViewById(R.id.imgPortada);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtPuntuacion = itemView.findViewById(R.id.txtPuntuacion);
        }
    }
}