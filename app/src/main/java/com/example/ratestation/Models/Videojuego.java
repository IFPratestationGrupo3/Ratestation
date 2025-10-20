package com.example.ratestation.Models;

import com.google.gson.annotations.SerializedName;

public class Videojuego {

    @SerializedName("name")
    private String titulo;

    @SerializedName("released")
    private String fechaLanzamiento;

    @SerializedName("background_image")
    private String imagenPortada;

    @SerializedName("rating")
    private float puntuacion;

    @SerializedName("genres")
    private Genre[] generos;

    public String getTitulo() {
        return titulo;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public String getImagenPortada() {
        return imagenPortada;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    public Genre[] getGeneros() {
        return generos;
    }

    public String getImagenUrl() {
        return imagenPortada;
    }

    public static class Genre {
        @SerializedName("name")
        private String nombre;

        public String getNombre() {
            return nombre;
        }
    }
}