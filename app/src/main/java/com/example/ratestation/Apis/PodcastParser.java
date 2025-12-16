package com.example.ratestation.Apis;


import android.util.Log;

import com.example.ratestation.Models.Podcast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PodcastParser {

    public static List<Podcast> parsePodcasts(String jsonResponse) {
        List<Podcast> podcasts = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray results = root.getJSONArray("results"); // Array de resultados de iTunes

            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonItem = results.getJSONObject(i);

                Podcast podcast = new Podcast();

                // getId() usa optInt() para evitar una excepción
                //   si el campo falta o es null, devolviendo 0 o un valor seguro.
                podcast.setId(jsonItem.optInt("collectionId"));

                if (jsonItem.has("trackName"))
                    podcast.setTitulo(jsonItem.getString("trackName"));
                if (jsonItem.has("artistName"))
                    podcast.setCreador(jsonItem.getString("artistName"));
                if (jsonItem.has("artworkUrl600"))
                    podcast.setArtworkPath(jsonItem.getString("artworkUrl600"));
                if (jsonItem.has("primaryGenreName"))
                    podcast.setGeneroPrincipal(jsonItem.getString("primaryGenreName"));
                if (jsonItem.has("feedUrl"))
                    podcast.setFeedUrl(jsonItem.getString("feedUrl"));

                //  Esto nos dirá si el bucle está funcionando
                Log.d("PodcastParser", "Podcast ID " + podcast.getId() + " parseado.");
                podcasts.add(podcast);
            }
        } catch (Exception e) {
            //  Imprime el tipo de error en caso de que salte la excepción
            Log.e("PodcastParserError", "Error al parsear JSON: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
        return podcasts;
    }
}