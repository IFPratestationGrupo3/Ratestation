package com.example.ratestation.Fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratestation.Adapters.Favoritos.FavPeliculasAdapter;
import com.example.ratestation.Adapters.Favoritos.FavSeriesAdapter;
import com.example.ratestation.Adapters.Favoritos.FavJuegosAdapter;
import com.example.ratestation.Adapters.Rate.RatePeliculasAdapter;
import com.example.ratestation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentUserEspacio extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // ===== FAVORITOS =====
    private RecyclerView recyclerPeliculas, recyclerSeries, recyclerJuegos;
    private FavPeliculasAdapter adapterPeliculas;
    private FavSeriesAdapter adapterSeries;
    private FavJuegosAdapter adapterJuegos;

    private List<String> titulosPeliculasFav = new ArrayList<>();
    private List<String> titulosSeriesFav = new ArrayList<>();
    private List<String> titulosJuegosFav = new ArrayList<>();

    // ===== PUNTUACIONES =====
    private RecyclerView recyclerPuntuacionesPeliculas;
    private RatePeliculasAdapter adapterPuntuacionesPeliculas;
    private Map<String, Float> puntuacionesPeliculas = new HashMap<>();

    public FragmentUserEspacio() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_espacio, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // ===== FAVORITOS =====
        recyclerPeliculas = view.findViewById(R.id.recyclerPeliculasFavoritas);
        recyclerPeliculas.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterPeliculas = new FavPeliculasAdapter(getContext(), titulosPeliculasFav);
        recyclerPeliculas.setAdapter(adapterPeliculas);

        recyclerSeries = view.findViewById(R.id.recyclerSeriesFavoritas);
        recyclerSeries.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterSeries = new FavSeriesAdapter(getContext(), titulosSeriesFav);
        recyclerSeries.setAdapter(adapterSeries);

        recyclerJuegos = view.findViewById(R.id.recyclerJuegosFavoritos);
        recyclerJuegos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterJuegos = new FavJuegosAdapter(getContext(), titulosJuegosFav);
        recyclerJuegos.setAdapter(adapterJuegos);

        // ===== PUNTUACIONES PELÍCULAS =====
        recyclerPuntuacionesPeliculas = view.findViewById(R.id.recyclerPuntuacionesPeliculas);
        recyclerPuntuacionesPeliculas.setLayoutManager(new LinearLayoutManager(getContext()));

        // ===== CARGAR DATOS =====
        cargarPeliculasFavoritas();
        cargarSeriesFavoritas();
        cargarJuegosFavoritos();
        cargarPuntuacionesPeliculas();

        return view;
    }

    // ================= FAVORITOS =================

    @SuppressWarnings("unchecked")
    private void cargarPeliculasFavoritas() {
        if (mAuth.getCurrentUser() == null) return;

        db.collection("usuarios")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    List<String> list = (List<String>) doc.get("PeliculasFav");
                    if (list != null) {
                        titulosPeliculasFav.clear();
                        titulosPeliculasFav.addAll(list);
                        adapterPeliculas.notifyDataSetChanged();
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private void cargarSeriesFavoritas() {
        if (mAuth.getCurrentUser() == null) return;

        db.collection("usuarios")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    List<String> list = (List<String>) doc.get("SeriesFav");
                    if (list != null) {
                        titulosSeriesFav.clear();
                        titulosSeriesFav.addAll(list);
                        adapterSeries.notifyDataSetChanged();
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private void cargarJuegosFavoritos() {
        if (mAuth.getCurrentUser() == null) return;

        db.collection("usuarios")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    List<String> list = (List<String>) doc.get("JuegosFav");
                    if (list != null) {
                        titulosJuegosFav.clear();
                        titulosJuegosFav.addAll(list);
                        adapterJuegos.notifyDataSetChanged();
                    }
                });
    }

    // ================= PUNTUACIONES =================

    @SuppressWarnings("unchecked")
    private void cargarPuntuacionesPeliculas() {
        if (mAuth.getCurrentUser() == null) return;

        db.collection("usuarios")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(doc -> {

                    Map<String, Object> map =
                            (Map<String, Object>) doc.get("Puntuaciones");

                    if (map != null) {
                        puntuacionesPeliculas.clear();

                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if (entry.getValue() instanceof Number) {
                                puntuacionesPeliculas.put(
                                        entry.getKey(),
                                        ((Number) entry.getValue()).floatValue()
                                );
                            }
                        }

                        adapterPuntuacionesPeliculas =
                                new RatePeliculasAdapter(getContext(), puntuacionesPeliculas);

                        recyclerPuntuacionesPeliculas
                                .setAdapter(adapterPuntuacionesPeliculas);
                    }
                });
    }
}