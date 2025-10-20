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

import com.example.ratestation.Adapters.VideojuegoAdapter;
import com.example.ratestation.Apis.RAWG_API;
import com.example.ratestation.Models.Videojuego;
import com.example.ratestation.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentJuegos extends Fragment {

    private Spinner spinnerGeneros;
    private RecyclerView rvJuegos;
    private List<Videojuego> listaJuegos = new ArrayList<>();
    private VideojuegoAdapter juegosAdapter;
    private boolean spinnerInicializado = false;

    // RAWG usa "slug" en lugar de IDs numéricos
    private static final Map<String, String> MAPA_GENEROS = new HashMap<String, String>() {{
        put("Acción", "action");
        put("Aventura", "adventure");
        put("Rol (RPG)", "role-playing-games-rpg");
        put("Estrategia", "strategy");
        put("Shooter", "shooter");
        put("Deportes", "sports");
        put("Carreras", "racing");
        put("Simulación", "simulation");
        put("Puzzle", "puzzle");
        put("Arcade", "arcade");
        put("Indie", "indie");
        put("Terror", "horror");
    }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_juegos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerGeneros = view.findViewById(R.id.spinnerGeneros);
        rvJuegos = view.findViewById(R.id.rvJuegos);

        // Lista de géneros + “Novedades”
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
                    cargarJuegosPopulares(); // Carga inicial
                    return;
                }

                String genero = listaGeneros.get(position);
                if (genero.equals("Novedades")) {
                    cargarJuegosPopulares();
                } else {
                    String slug = MAPA_GENEROS.get(genero);
                    cargarJuegosPorGenero(slug);
                }

                Toast.makeText(getContext(), "Filtrando por: " + genero, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Configurar RecyclerView
        juegosAdapter = new VideojuegoAdapter(getContext(), listaJuegos);
        rvJuegos.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvJuegos.setAdapter(juegosAdapter);
    }

    private void cargarJuegosPopulares() {
        new Thread(() -> {
            String json = RAWG_API.fetchPopularGames();
            procesarJuegos(json);
        }).start();
    }

    private void cargarJuegosPorGenero(String genreSlug) {
        new Thread(() -> {
            String json = RAWG_API.fetchGamesByGenre(genreSlug);
            procesarJuegos(json);
        }).start();
    }

    private void procesarJuegos(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONArray results = root.getJSONArray("results");

            listaJuegos.clear();
            for (int i = 0; i < results.length(); i++) {
                JSONObject obj = results.getJSONObject(i);
                Videojuego juego = new Gson().fromJson(obj.toString(), Videojuego.class);
                listaJuegos.add(juego);
            }

            requireActivity().runOnUiThread(() -> juegosAdapter.notifyDataSetChanged());

        } catch (Exception e) {
            Log.e("JUEGOS_ERROR", "Error al parsear JSON", e);
        }
    }
}