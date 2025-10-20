package com.example.ratestation.Apis;

import com.example.ratestation.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RAWG_API {

    private static final String BASE_URL = "https://api.rawg.io/api/";

    public static String fetchPopularGames() {
        String url = BASE_URL + "games?key=" + BuildConfig.RAWG_API_KEY + "&ordering=-rating";
        return fetchFromUrl(url);
    }

    public static String fetchGamesByGenre(String genreSlug) {
        String url = BASE_URL + "games?key=" + BuildConfig.RAWG_API_KEY + "&genres=" + genreSlug;
        return fetchFromUrl(url);
    }

    private static String fetchFromUrl(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
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