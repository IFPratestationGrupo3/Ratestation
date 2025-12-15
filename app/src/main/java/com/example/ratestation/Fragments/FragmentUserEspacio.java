package com.example.ratestation.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ratestation.Adapters.Favoritos.FavJuegosAdapter;
import com.example.ratestation.Adapters.Favoritos.FavPeliculasAdapter;
import com.example.ratestation.Adapters.Favoritos.FavSeriesAdapter;
import com.example.ratestation.Apis.RAWG_API;
import com.example.ratestation.Apis.TMDB_API;
import com.example.ratestation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FragmentUserEspacio extends Fragment {

    private static final String TAG = "FragmentUserEspacio";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Componentes de UI y datos
    private RecyclerView recyclerPeliculas, recyclerSeries, recyclerJuegos;
    private FavPeliculasAdapter adapterPeliculas;
    private FavSeriesAdapter adapterSeries;
    private FavJuegosAdapter adapterJuegos;
    private final List<FavPeliculasAdapter.PeliculaFavorita> listaPeliculasFav = new ArrayList<>();
    private final List<FavSeriesAdapter.SerieFavorita> listaSeriesFav = new ArrayList<>();
    private final List<FavJuegosAdapter.JuegoFavorito> listaJuegosFav = new ArrayList<>();

    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_espacio, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setupRecyclerViews(view);
        cargarFavoritos();
        return view;
    }

    private void setupRecyclerViews(View view) {
        recyclerPeliculas = view.findViewById(R.id.recyclerPeliculasFavoritas);
        recyclerPeliculas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapterPeliculas = new FavPeliculasAdapter(getContext(), listaPeliculasFav);
        recyclerPeliculas.setAdapter(adapterPeliculas);

        recyclerSeries = view.findViewById(R.id.recyclerSeriesFavoritas);
        recyclerSeries.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapterSeries = new FavSeriesAdapter(getContext(), listaSeriesFav);
        recyclerSeries.setAdapter(adapterSeries);

        recyclerJuegos = view.findViewById(R.id.recyclerJuegosFavoritos);
        recyclerJuegos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapterJuegos = new FavJuegosAdapter(getContext(), listaJuegosFav);
        recyclerJuegos.setAdapter(adapterJuegos);
    }

    @SuppressWarnings("unchecked")
    private void cargarFavoritos() {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(userId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                List<String> titulosPeliculas = (List<String>) doc.get("PeliculasFav");
                if (titulosPeliculas != null && !titulosPeliculas.isEmpty()) procesarPeliculasFavoritas(titulosPeliculas);

                List<String> titulosSeries = (List<String>) doc.get("SeriesFav");
                if (titulosSeries != null && !titulosSeries.isEmpty()) procesarSeriesFavoritas(titulosSeries);

                List<String> titulosJuegos = (List<String>) doc.get("JuegosFav");
                if (titulosJuegos != null && !titulosJuegos.isEmpty()) procesarJuegosFavoritos(titulosJuegos);
            }
        }).addOnFailureListener(e -> Log.w(TAG, "Error getting document", e));
    }

    private void procesarPeliculasFavoritas(List<String> titulos) {
        executor.execute(() -> {
            List<FavPeliculasAdapter.PeliculaFavorita> tempList = new ArrayList<>();
            for (String titulo : titulos) {
                try {
                    String searchJson = TMDB_API.buscarPeliculaPorTitulo(titulo);
                    JSONObject root = new JSONObject(searchJson);
                    JSONArray results = root.optJSONArray("results");
                    if (results != null && results.length() > 0) {
                        JSONObject peliculaJson = results.optJSONObject(0);
                        if (peliculaJson == null) continue;

                        int id = peliculaJson.optInt("id", -1);
                        if (id == -1) continue;

                        String posterPath = peliculaJson.optString("poster_path", "");
                        String posterUrl = (!posterPath.isEmpty() && !posterPath.equals("null")) ? "https://image.tmdb.org/t/p/w500" + posterPath : "";
                        String releaseDate = peliculaJson.optString("release_date", "");
                        String anio = (!releaseDate.isEmpty() && releaseDate.length() >= 4) ? releaseDate.substring(0, 4) : "N/A";
                        String sinopsis = peliculaJson.optString("overview", "No hay sinopsis disponible.");

                        String detallesJson = TMDB_API.fetchMovieDetails(id);
                        JSONObject detallesObj = new JSONObject(detallesJson);

                        String director = "Desconocido";
                        JSONObject credits = detallesObj.optJSONObject("credits");
                        if (credits != null) {
                            JSONArray crew = credits.optJSONArray("crew");
                            if (crew != null) {
                                for (int i = 0; i < crew.length(); i++) {
                                    JSONObject member = crew.optJSONObject(i);
                                    if (member != null && "Director".equals(member.optString("job"))) {
                                        director = member.optString("name", "Desconocido");
                                        break;
                                    }
                                }
                            }
                        }

                        String generos = "N/A";
                        JSONArray genresArray = detallesObj.optJSONArray("genres");
                        if (genresArray != null && genresArray.length() > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < genresArray.length(); i++) {
                                JSONObject genre = genresArray.optJSONObject(i);
                                if (genre != null) {
                                    sb.append(genre.optString("name"));
                                    if (i < genresArray.length() - 1) sb.append(", ");
                                }
                            }
                            generos = sb.toString();
                        }
                        tempList.add(new FavPeliculasAdapter.PeliculaFavorita(titulo, posterUrl, anio, sinopsis, director, generos));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error procesando película: " + titulo, e);
                }
            }
            handler.post(() -> {
                listaPeliculasFav.clear();
                listaPeliculasFav.addAll(tempList);
                adapterPeliculas.notifyDataSetChanged();
            });
        });
    }

    private void procesarSeriesFavoritas(List<String> titulos) {
        executor.execute(() -> {
            List<FavSeriesAdapter.SerieFavorita> tempList = new ArrayList<>();
            for (String titulo : titulos) {
                try {
                    String searchJson = TMDB_API.buscarSeriePorTitulo(titulo);
                    JSONObject root = new JSONObject(searchJson);
                    JSONArray results = root.optJSONArray("results");
                    if (results != null && results.length() > 0) {
                        JSONObject serieJson = results.optJSONObject(0);
                        if (serieJson == null) continue;
                        
                        int id = serieJson.optInt("id", -1);
                        if (id == -1) continue;

                        String posterPath = serieJson.optString("poster_path", "");
                        String posterUrl = (!posterPath.isEmpty() && !posterPath.equals("null")) ? "https://image.tmdb.org/t/p/w500" + posterPath : "";
                        String firstAirDate = serieJson.optString("first_air_date", "");
                        String anio = (!firstAirDate.isEmpty() && firstAirDate.length() >= 4) ? firstAirDate.substring(0, 4) : "N/A";
                        String sinopsis = serieJson.optString("overview", "No hay sinopsis disponible.");

                        String detallesJson = TMDB_API.fetchSeriesDetails(id);
                        JSONObject detallesObj = new JSONObject(detallesJson);

                        String creador = "Desconocido";
                        JSONArray creators = detallesObj.optJSONArray("created_by");
                        if (creators != null && creators.length() > 0) {
                            JSONObject creatorObj = creators.optJSONObject(0);
                            if (creatorObj != null) {
                                creador = creatorObj.optString("name", "Desconocido");
                            }
                        }

                        String generos = "N/A";
                        JSONArray genresArray = detallesObj.optJSONArray("genres");
                         if (genresArray != null && genresArray.length() > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < genresArray.length(); i++) {
                                JSONObject genre = genresArray.optJSONObject(i);
                                if (genre != null) {
                                    sb.append(genre.optString("name"));
                                    if (i < genresArray.length() - 1) sb.append(", ");
                                }
                            }
                            generos = sb.toString();
                        }
                        tempList.add(new FavSeriesAdapter.SerieFavorita(titulo, posterUrl, anio, sinopsis, creador, generos));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error procesando serie: " + titulo, e);
                }
            }
            handler.post(() -> {
                listaSeriesFav.clear();
                listaSeriesFav.addAll(tempList);
                adapterSeries.notifyDataSetChanged();
            });
        });
    }

    private void procesarJuegosFavoritos(List<String> titulos) {
        executor.execute(() -> {
            List<FavJuegosAdapter.JuegoFavorito> tempList = new ArrayList<>();
            for (String titulo : titulos) {
                try {
                    String searchJson = RAWG_API.searchGames(titulo);
                    JSONObject root = new JSONObject(searchJson);
                    JSONArray results = root.optJSONArray("results");
                    if (results != null && results.length() > 0) {
                        JSONObject juegoJson = results.optJSONObject(0);
                        if (juegoJson == null) continue;

                        int id = juegoJson.optInt("id", -1);
                        if (id == -1) continue;

                        String portada = juegoJson.optString("background_image", "");
                        String fecha = juegoJson.optString("released", "N/A");
                        String calificacion = String.valueOf(juegoJson.optDouble("rating", 0.0));

                        String detallesJson = RAWG_API.fetchGameDetails(id);
                        JSONObject detallesObj = new JSONObject(detallesJson);

                        String generos = "N/A";
                        JSONArray genresArray = detallesObj.optJSONArray("genres");
                        if (genresArray != null && genresArray.length() > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < genresArray.length(); i++) {
                                JSONObject genre = genresArray.optJSONObject(i);
                                if (genre != null) {
                                    sb.append(genre.optString("name"));
                                    if (i < genresArray.length() - 1) sb.append(", ");
                                }
                            }
                            generos = sb.toString();
                        }

                        String plataformas = "N/A";
                        JSONArray platformsArray = detallesObj.optJSONArray("platforms");
                        if (platformsArray != null && platformsArray.length() > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < platformsArray.length(); i++) {
                                JSONObject platObj = platformsArray.optJSONObject(i);
                                if (platObj != null) {
                                    JSONObject platform = platObj.optJSONObject("platform");
                                    if (platform != null) {
                                        sb.append(platform.optString("name"));
                                        if (i < platformsArray.length() - 1) sb.append(", ");
                                    }
                                }
                            }
                            plataformas = sb.toString();
                        }
                        tempList.add(new FavJuegosAdapter.JuegoFavorito(titulo, portada, fecha, generos, plataformas, calificacion));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error procesando juego: " + titulo, e);
                }
            }
            handler.post(() -> {
                listaJuegosFav.clear();
                listaJuegosFav.addAll(tempList);
                adapterJuegos.notifyDataSetChanged();
            });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
