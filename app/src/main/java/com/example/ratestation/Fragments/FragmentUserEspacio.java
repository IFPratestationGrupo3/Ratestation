package com.example.ratestation.Fragments;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ratestation.Adapters.FavPeliculasAdapter;
import com.example.ratestation.Adapters.FavSeriesAdapter;
import com.example.ratestation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FragmentUserEspacio extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Películas
    private RecyclerView recyclerPeliculas;
    private FavPeliculasAdapter adapterPeliculas;
    private List<String> titulosPeliculasFav = new ArrayList<>();

    // Series
    private RecyclerView recyclerSeries;
    private FavSeriesAdapter adapterSeries;
    private List<String> titulosSeriesFav = new ArrayList<>();

    public FragmentUserEspacio() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_espacio, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Recycler de películas favoritas
        recyclerPeliculas = view.findViewById(R.id.recyclerPeliculasFavoritas);
        recyclerPeliculas.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterPeliculas = new FavPeliculasAdapter(getContext(), titulosPeliculasFav);
        recyclerPeliculas.setAdapter(adapterPeliculas);

        // Recycler de series favoritas
        recyclerSeries = view.findViewById(R.id.recyclerSeriesFavoritas);
        recyclerSeries.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterSeries = new FavSeriesAdapter(getContext(), titulosSeriesFav);
        recyclerSeries.setAdapter(adapterSeries);

        // Cargar datos
        cargarPeliculasFavoritas();
        cargarSeriesFavoritas();

        return view;
    }
    @SuppressWarnings("unchecked")
    private void cargarPeliculasFavoritas() {
        if (mAuth.getCurrentUser() == null) return;

        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(doc -> {
                    List<String> favoritas = (List<String>) doc.get("PeliculasFav");
                    if (favoritas != null) {
                        titulosPeliculasFav.clear();
                        titulosPeliculasFav.addAll(favoritas);
                        adapterPeliculas.notifyDataSetChanged();
                    }
                });
    }
    @SuppressWarnings("unchecked")
    private void cargarSeriesFavoritas() {
        if (mAuth.getCurrentUser() == null) return;

        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(doc -> {
                    List<String> favoritas = (List<String>) doc.get("SeriesFav");
                    if (favoritas != null) {
                        titulosSeriesFav.clear();
                        titulosSeriesFav.addAll(favoritas);
                        adapterSeries.notifyDataSetChanged();
                    }
                });
    }
}