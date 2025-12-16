package com.example.ratestation.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ratestation.Adapters.PeliculaAdapter;
import com.example.ratestation.Apis.TMDB_API;
import com.example.ratestation.Models.Pelicula;
import com.example.ratestation.R;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentPeliculas extends Fragment {

    private Spinner spinnerGeneros;
    private RecyclerView rvValoradas, rvFavoritas;
    private List<Pelicula> listaPeliculas = new ArrayList<>();
    private PeliculaAdapter valoradasAdapter;

    private boolean spinnerInicializado = false;

    private static final Map<String, Integer> MAPA_GENEROS = new HashMap<String, Integer>() {{
        put("Acción", 28);
        put("Drama", 18);
        put("Comedia", 35);
        put("Terror", 27);
        put("Romance", 10749);
        put("Ciencia Ficción", 878);
        put("Aventura", 12);
        put("Musical", 10402);
        put("Documental", 99);
        put("Fantasía", 14);
        put("Suspenso", 53);
        put("Animación", 16);
        put("Histórica", 36);
        put("Crimen", 80);
    }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_peliculas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerGeneros = view.findViewById(R.id.spinnerGeneros);
        rvValoradas = view.findViewById(R.id.rvValoradas);

        // Lista de géneros + "Novedades" inicial
        List<String> listaGeneros = new ArrayList<>();
        listaGeneros.add("Novedades");
        listaGeneros.addAll(MAPA_GENEROS.keySet());

        // Configurar el spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                listaGeneros
        );
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGeneros.setAdapter(adapterSpinner);

        spinnerGeneros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spinnerInicializado) {
                    spinnerInicializado = true;
                    cargarPeliculasValoradas();
                    return;
                }

                String genero = listaGeneros.get(position);
                if (genero.equals("Novedades")) {
                    cargarPeliculasValoradas();
                } else {
                    int genreId = MAPA_GENEROS.get(genero);
                    cargarPeliculasPorGenero(genreId);
                }

                Toast.makeText(getContext(), "Filtrando por: " + genero, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Adaptador de películas
        valoradasAdapter = new PeliculaAdapter(getContext(), listaPeliculas);
        rvValoradas.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvValoradas.setAdapter(valoradasAdapter);

    }

    private void cargarPeliculasValoradas() {
        new Thread(() -> {
            String json = TMDB_API.fetchPopularMovies();
            procesarPeliculas(json);
        }).start();
    }

    private void cargarPeliculasPorGenero(int genreId) {
        new Thread(() -> {
            String json = TMDB_API.fetchMoviesByGenre(genreId);
            procesarPeliculas(json);
        }).start();
    }

    private void procesarPeliculas(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONArray results = root.getJSONArray("results");

            listaPeliculas.clear();
            for (int i = 0; i < results.length(); i++) {
                JSONObject obj = results.getJSONObject(i);
                Pelicula pelicula = new Gson().fromJson(obj.toString(), Pelicula.class);
                listaPeliculas.add(pelicula);
            }

            requireActivity().runOnUiThread(() -> valoradasAdapter.notifyDataSetChanged());

        } catch (Exception e) {
            Log.e("PELIS_ERROR", "Error al parsear JSON", e);
        }
    }
}