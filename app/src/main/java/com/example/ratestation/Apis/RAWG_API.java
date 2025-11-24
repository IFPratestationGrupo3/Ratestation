package com.example.ratestation.Apis;

import com.example.ratestation.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RAWG_API {

    private static final String BASE_URL = "https://api.rawg.io/api/";
    private static final OkHttpClient client = new OkHttpClient();

    // ---------------------------------------------------------
    // POPULARES
    // ---------------------------------------------------------
    public static String fetchPopularGames() {
        String url = BASE_URL + "games?key=" + BuildConfig.RAWG_API_KEY + "&ordering=-rating";
        return fetchFromUrl(url);
    }

    // ---------------------------------------------------------
    // JUEGOS POR GÉNERO
    // ---------------------------------------------------------
    public static String fetchGamesByGenre(String genreSlug) {
        String url = BASE_URL + "games?key=" + BuildConfig.RAWG_API_KEY + "&genres=" + genreSlug;
        return fetchFromUrl(url);
    }

    // ---------------------------------------------------------
    // DETALLES DE JUEGO POR ID
    // ---------------------------------------------------------
    public static String fetchGameDetails(int gameId) {
        String url = BASE_URL + "games/" + gameId + "?key=" + BuildConfig.RAWG_API_KEY;
        return fetchFromUrl(url);
    }

    // ---------------------------------------------------------
    // BÚSQUEDA DE JUEGOS
    // ---------------------------------------------------------
    public static String searchGames(String query) {
        String url = BASE_URL + "games?search=" + query + "&key=" + BuildConfig.RAWG_API_KEY;
        return fetchFromUrl(url);
    }

    // ---------------------------------------------------------
    // PETICIÓN GENERAL
    // ---------------------------------------------------------
    private static String fetchFromUrl(String url) {

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                return "Error: " + response.code();
            }

            if (response.body() == null) {
                return "Error: Respuesta vacía";
            }

            return response.body().string();

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }
}