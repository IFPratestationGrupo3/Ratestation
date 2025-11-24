package com.example.ratestation.Models;

import com.google.gson.annotations.SerializedName;

public class Serie {

    @SerializedName("name")
    private String titulo;

    @SerializedName("first_air_date")
    private String fechaEstreno;

    @SerializedName("overview")
    private String sinopsis;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("vote_average")
    private float puntuacion;

    @SerializedName("id")
    private int id;

    private String director; // puede ser showrunner o creador
    private String generos;  // nombres separados por coma

    // Getters
    public String getTitulo() { return titulo; }
    public String getFechaEstreno() { return fechaEstreno; }
    public String getSinopsis() { return sinopsis; }
    public String getPosterPath() { return posterPath; }
    public float getPuntuacion() { return puntuacion; }
    public String getDirector() { return director; }
    public String getGeneros() { return generos; }

    public int getId() { return id; }


    // Setters para director y generos
    public void setDirector(String director) { this.director = director; }
    public void setGeneros(String generos) { this.generos = generos; }

    public void setId(int id) { this.id = id; }

    // URL completa del póster
    public String getPosterUrl() {
        return posterPath != null ? "https://image.tmdb.org/t/p/w500" + posterPath : null;
    }
}