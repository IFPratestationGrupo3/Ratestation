package com.example.ratestation.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratestation.Activities.Activity_Podcast;
import com.example.ratestation.Adapters.PodcastAdapter;
import com.example.ratestation.Apis.PodcastData; // Importado para la carga estática
import com.example.ratestation.Apis.PodcastData;
        import com.example.ratestation.Models.Podcast;
import com.example.ratestation.R;

import java.util.ArrayList;
import java.util.List;

// 1. CRÍTICO: El Fragment debe implementar la interfaz del Adaptador
public class FragmentPodcast extends Fragment implements PodcastAdapter.PodcastClickListener {

    private RecyclerView recyclerView;
    private PodcastAdapter podcastAdapter;
    private List<Podcast> podcastList;

    public FragmentPodcast() {
        // Constructor público vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_podcast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        podcastList = new ArrayList<>();

        // 2. CRÍTICO: Inicialización correcta con 3 argumentos: Contexto, Lista, y el Listener ('this')
        podcastAdapter = new PodcastAdapter(getContext(), podcastList, this);

        recyclerView = view.findViewById(R.id.rvPodcastList); // Asegura que el ID sea correcto
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(podcastAdapter);

        // Inicia la carga de datos estáticos
        loadPodcasts("top");
    }

    /**
     * Carga la lista de podcasts usando datos estáticos (temporal).
     */
    private void loadPodcasts(String searchTerm) {

        Log.d("PodcastDebug", "Iniciando carga con datos estáticos.");

        List<Podcast> fakePodcasts = PodcastData.createFakePodcasts();

        if (getActivity() != null) {
            // Aseguramos la actualización de la UI en el Hilo Principal
            getActivity().runOnUiThread(() -> {
                if (!fakePodcasts.isEmpty()) {
                    podcastAdapter.updateData(fakePodcasts);
                    Log.d("PodcastDebug", "Carga Estática EXITOSA: " + fakePodcasts.size() + " podcasts cargados.");
                    Toast.makeText(getContext(), "Cargados " + fakePodcasts.size() + " podcasts estáticos.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("PodcastDebug", "ERROR: La lista de datos estáticos está vacía.");
                }
            });
        }
    }

    // 3. CRÍTICO: Implementación del método de la interfaz
    @Override
    public void onPodcastClick(Podcast podcast) {
        // Lógica de navegación a la Activity de detalle (Activity_Podcast)
        try {
            Intent intent = new Intent(getContext(), Activity_Podcast.class);
            // Pasamos los datos que Activity_Podcast necesita para mostrar la UI e iniciar la descarga del RSS
            intent.putExtra("id", podcast.getId());
            intent.putExtra("titulo", podcast.getTitulo());
            intent.putExtra("creador", podcast.getCreador());
            intent.putExtra("artworkPath", podcast.getArtworkPath());
            intent.putExtra("feedUrl", podcast.getFeedUrl()); // La URL real del RSS
            startActivity(intent);
        } catch (Exception e) {
            Log.e("FragmentPodcast", "Error al iniciar Activity_Podcast: " + e.getMessage());
            Toast.makeText(getContext(), "Error al abrir el detalle.", Toast.LENGTH_SHORT).show();
        }
    }
}