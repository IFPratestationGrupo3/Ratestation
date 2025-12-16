package com.example.ratestation.Apis;

import android.util.Log;
import com.example.ratestation.Models.Podcast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PODCAST_API {


    private static final String TAG = "PODCAST_API";
    // URL de ejemplo para buscar los "top" podcasts en España
    private static final String ITUNES_API_URL = "https://itunes.apple.com/search?term=top%20podcasts&country=ES&media=podcast&limit=50";

    public interface ApiCallback {
        void onSuccess(List<Podcast> podcasts);
        void onFailure(String errorMessage);
    }

    /**
     * Realiza la llamada a la API de iTunes en un hilo de fondo.
     */
    public static void fetchPodcasts(ApiCallback callback) {
        new Thread(() -> {
            String jsonResponse = null;
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(ITUNES_API_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Lee la respuesta
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    reader.close();
                    jsonResponse = stringBuilder.toString();
                    inputStream.close();

                    // Parseo el JSON usando la clase existente
                    List<Podcast> podcasts = PodcastParser.parsePodcasts(jsonResponse);
                    callback.onSuccess(podcasts);

                } else {
                    callback.onFailure("Error HTTP: Código " + responseCode);
                }

            } catch (Exception e) {
                Log.e(TAG, "Error en la petición API: " + e.getMessage(), e);
                callback.onFailure("Error de red: " + e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }).start();
    }
}