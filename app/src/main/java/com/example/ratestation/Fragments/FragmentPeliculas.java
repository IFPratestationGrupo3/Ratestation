package com.example.ratestation.Fragments;

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
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.ratestation.Adapters.GeneroAdapter;
import com.example.ratestation.Adapters.PeliculaAdapter;
import com.example.ratestation.Apis.TMDB_API;
import com.example.ratestation.Models.Pelicula;
import com.example.ratestation.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentPeliculas extends Fragment {

    private RecyclerView rvGeneros, rvValoradas, rvFavoritas;

    public FragmentPeliculas() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_peliculas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvGeneros = view.findViewById(R.id.rvGeneros);
        rvValoradas = view.findViewById(R.id.rvValoradas);
        rvFavoritas = view.findViewById(R.id.rvFavoritas);

        // Géneros
        List<String> listaGeneros = Arrays.asList(
                "Acción", "Drama", "Comedia", "Terror", "Romance",
                "Ciencia Ficción", "Aventura", "Musical", "Documental",
                "Fantasía", "Suspenso", "Animación", "Histórica", "Crimen"
        );

        GeneroAdapter adapterGeneros = new GeneroAdapter(listaGeneros, genero -> {
            Log.d("Filtro", "Género seleccionado: " + genero);
            Toast.makeText(getContext(), "Filtrando por: " + genero, Toast.LENGTH_SHORT).show();
        });

        rvGeneros.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGeneros.setAdapter(adapterGeneros);

        // Películas más valoradas
        List<Pelicula> listaValoradas = new ArrayList<>();
        PeliculaAdapter valoradasAdapter = new PeliculaAdapter(getContext(), listaValoradas);
        rvValoradas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvValoradas.setAdapter(valoradasAdapter);


        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvValoradas);


        cargarPeliculasValoradas(listaValoradas, valoradasAdapter);
    }

    private void cargarPeliculasValoradas(List<Pelicula> lista, PeliculaAdapter adapter) {
        new Thread(() -> {
            String json = TMDB_API.fetchPopularMovies();
            try {
                JSONObject root = new JSONObject(json);
                JSONArray results = root.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    Pelicula pelicula = new Gson().fromJson(obj.toString(), Pelicula.class);
                    lista.add(pelicula);
                }

                requireActivity().runOnUiThread(adapter::notifyDataSetChanged);

            } catch (Exception e) {
                Log.e("PELIS_ERROR", "Error al parsear JSON", e);
            }
        }).start();
    }
}