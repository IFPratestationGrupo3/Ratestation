package com.example.ratestation.Models;

import com.google.gson.annotations.SerializedName;

public class Pelicula {

    @SerializedName("title")
    private String titulo;

    @SerializedName("release_date")
    private String fechaEstreno;

    @SerializedName("overview")
    private String sinopsis;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("vote_average")
    private float puntuacion;

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public String getFechaEstreno() {
        return fechaEstreno;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    // Utilidad para obtener la URL completa del póster
    public String getPosterUrl() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }
}
