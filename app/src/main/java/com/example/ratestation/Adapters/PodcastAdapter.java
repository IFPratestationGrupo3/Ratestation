package com.example.ratestation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratestation.Models.Podcast;
import com.example.ratestation.R;

import java.util.List;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.ViewHolder> {

    private final Context context;
    private final List<Podcast> podcasts;
    private final PodcastClickListener listener;

    // Interfaz public para comunicar clics
    public interface PodcastClickListener {
        void onPodcastClick(Podcast podcast);
    }


    public PodcastAdapter(Context context, List<Podcast> podcasts, PodcastClickListener listener) {
        this.context = context;
        this.podcasts = podcasts;
        this.listener = listener;
    }

    /**
     * Método para actualizar la lista de podcasts y refrescar la vista.
     */
    public void updateData(List<Podcast> newPodcasts) {
        this.podcasts.clear();
        this.podcasts.addAll(newPodcasts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_podcast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Podcast podcast = podcasts.get(position);

        holder.txtTitulo.setText(podcast.getTitulo());
        holder.txtCreador.setText("Creador: " + podcast.getCreador());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPodcastClick(podcast);
            }
        });
    }

    @Override
    public int getItemCount() { return podcasts.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitulo, txtCreador;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.txtPodcastTitle);
            txtCreador = itemView.findViewById(R.id.txtCreator);
        }
    }
}