package com.example.ratestation.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratestation.Activities.Activity_Podcast;
import com.example.ratestation.Models.Podcast;
import com.example.ratestation.R;

import java.util.List;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.ViewHolder> {

    private final List<Podcast> podcastList;

    public PodcastAdapter(List<Podcast> podcastList) {
        this.podcastList = podcastList;
    }

    @NonNull
    @Override
    public PodcastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_podcast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PodcastAdapter.ViewHolder holder, int position) {
        Podcast podcast = podcastList.get(position);
        if (podcast != null && podcast.getTitulo() != null) {
            holder.title.setText(podcast.getTitulo());
        }

        // Configurar el OnClickListener
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, Activity_Podcast.class);

            // Poner los datos del podcast en el Intent
            intent.putExtra("titulo", podcast.getTitulo());
            intent.putExtra("creador", podcast.getCreador());
            intent.putExtra("artworkUrl", podcast.getArtworkPath());
            intent.putExtra("genero", podcast.getGeneroPrincipal());
            // Podrías pasar más datos aquí si los necesitaras

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return podcastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.podcast_title);
        }
    }
}
