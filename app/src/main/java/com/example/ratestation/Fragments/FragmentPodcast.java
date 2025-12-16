package com.example.ratestation.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ratestation.Adapters.PodcastAdapter;
import com.example.ratestation.Apis.PODCAST_API;
import com.example.ratestation.Models.Podcast;
import com.example.ratestation.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentPodcast extends Fragment {

    private static final String TAG = "FragmentPodcast";

    private RecyclerView recyclerView;
    private PodcastAdapter adapter;
    private final List<Podcast> podcastList = new ArrayList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_podcast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewPodcasts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PodcastAdapter(podcastList);
        recyclerView.setAdapter(adapter);

        cargarPodcasts();
    }

    private void cargarPodcasts() {
        // La llamada a la API ya se ejecuta en un hilo de fondo dentro de fetchPodcasts
        PODCAST_API.fetchPodcasts(new PODCAST_API.ApiCallback() {
            @Override
            public void onSuccess(List<Podcast> podcasts) {
                // La respuesta ya viene parseada, actualizamos la UI en el hilo principal
                handler.post(() -> {
                    if (podcasts != null && !podcasts.isEmpty()) {
                        podcastList.clear();
                        podcastList.addAll(podcasts);
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, podcasts.size() + " podcasts cargados.");
                    } else {
                        Log.d(TAG, "No se recibieron podcasts.");
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                // Mostramos un error al usuario en el hilo principal
                handler.post(() -> {
                    Log.e(TAG, "Error al cargar podcasts: " + errorMessage);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error al cargar podcasts", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
