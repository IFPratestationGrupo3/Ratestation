package com.example.ratestation.Apis;

import com.example.ratestation.BuildConfig;
import com.example.ratestation.Models.Pelicula;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TMDB_API {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static String fetchPopularMovies() {
        return fetchFromUrl(BASE_URL + "movie/popular");
    }

    public static String fetchMoviesByGenre(int genreId) {
        String url = BASE_URL + "discover/movie?with_genres=" + genreId;
        return fetchFromUrl(url);
    }

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