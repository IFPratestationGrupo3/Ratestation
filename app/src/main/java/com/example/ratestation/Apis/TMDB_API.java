package com.example.ratestation.Apis;

import com.example.ratestation.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TMDB_API {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    //========================PELICULAS====================================
    public static String fetchPopularMovies() {
        return fetchFromUrl(BASE_URL + "movie/popular?language=es-ES");
    }

    public static String fetchMoviesByGenre(int genreId) {
        String url = BASE_URL + "discover/movie?with_genres=" + genreId + "&language=es-ES";
        return fetchFromUrl(url);
    }

    // Detalles de película (por ID), incluyendo director y créditos
    public static String fetchMovieDetails(int movieId) {
        String url = BASE_URL + "movie/" + movieId + "?language=es-ES&append_to_response=credits";
        return fetchFromUrl(url);
    }

    //========================SERIES====================================
    public static String fetchPopularSeries() {
        return fetchFromUrl(BASE_URL + "tv/popular?language=es-ES");
    }
    public static String fetchSeriesByGenre(int genreId) {
        String url = BASE_URL + "discover/tv?with_genres=" + genreId + "&language=es-ES";
        return fetchFromUrl(url);
    }
    public static String fetchSeriesDetails(int tvId) {
        String url = BASE_URL + "tv/" + tvId + "?language=es-ES&append_to_response=credits";
        return fetchFromUrl(url);
    }
    //========================UTILIDADES====================================
    private static String fetchFromUrl(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + BuildConfig.TMDB_ACCESS_TOKEN)
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                return "Error: " + response.code();
            }
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }
}