package com.example.ratestation.Models;

import com.google.gson.annotations.SerializedName;

public class Pelicula {

    @SerializedName("id")
    private int id;

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
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getFechaEstreno() { return fechaEstreno; }
    public String getSinopsis() { return sinopsis; }
    public String getPosterPath() { return posterPath; }
    public float getPuntuacion() { return puntuacion; }
    public String getPosterUrl() { return "https://image.tmdb.org/t/p/w500" + posterPath; }

    // Campos adicionales que se obtienen de detalles
    private String director;
    private String generos;

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getGeneros() { return generos; }
    public void setGeneros(String generos) { this.generos = generos; }
}