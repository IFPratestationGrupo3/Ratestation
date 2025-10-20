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
import com.example.ratestation.Models.Videojuego;
import com.example.ratestation.R;

import java.util.List;

public class VideojuegoAdapter extends RecyclerView.Adapter<VideojuegoAdapter.ViewHolder> {

    private Context context;
    private List<Videojuego> videojuegos;

    public VideojuegoAdapter(Context context, List<Videojuego> videojuegos) {
        this.context = context;
        this.videojuegos = videojuegos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_videojuego, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Videojuego juego = videojuegos.get(position);

        // Cargar imagen con Glide
        Glide.with(context)
                .load(juego.getImagenUrl())
                .placeholder(R.drawable.ratelogo)
                .into(holder.imgPoster);

        holder.txtTitulo.setText(juego.getTitulo());
        holder.txtPuntuacion.setText("⭐ " + juego.getPuntuacion());
    }

    @Override
    public int getItemCount() {
        return videojuegos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView txtTitulo, txtPuntuacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPortada);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtPuntuacion = itemView.findViewById(R.id.txtPuntuacion);
        }
    }
}