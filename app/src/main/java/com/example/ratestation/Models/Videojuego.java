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

    @SerializedName("platforms")
    private PlatformWrapper[] plataformas;

    @SerializedName("tags")
    private Tag[] tags;


    public String getTitulo() { return titulo; }

    public String getFechaLanzamiento() {
        return fechaLanzamiento != null ? fechaLanzamiento : "No disponible";
    }

    public String getImagenPortada() { return imagenPortada; }

    public float getPuntuacion() { return puntuacion; }

    public Genre[] getGeneros() { return generos; }

    public PlatformWrapper[] getPlataformas() { return plataformas; }

    public Tag[] getTags() { return tags; }

    public String getImagenUrl() { return imagenPortada; }


    // Géneros
    public String getGenerosString() {
        if (generos == null || generos.length == 0)
            return "No disponible";

        StringBuilder sb = new StringBuilder();
        for (Genre g : generos) {
            if (g != null && g.nombre != null)
                sb.append(g.nombre).append(", ");
        }

        if (sb.length() == 0) return "No disponible";

        return sb.substring(0, sb.length() - 2);
    }

    // Plataformas
    public String getPlataformasString() {
        if (plataformas == null || plataformas.length == 0)
            return "No disponible";

        StringBuilder sb = new StringBuilder();
        for (PlatformWrapper pw : plataformas) {
            if (pw != null && pw.platform != null && pw.platform.nombre != null)
                sb.append(pw.platform.nombre).append(", ");
        }

        if (sb.length() == 0) return "No disponible";

        return sb.substring(0, sb.length() - 2);
    }

    // Calificación
    public String getCalidadTag() {
        if (puntuacion >= 4.0f) return "Excelente";
        if (puntuacion >= 3.0f) return "Muy Bueno";
        if (puntuacion >= 2.0f) return "Aceptable";
        if (puntuacion > 0) return "Flojo";
        return "Sin clasificar";
    }



    public static class Genre {
        @SerializedName("name")
        String nombre;

        public String getNombre() { return nombre; }
    }

    public static class PlatformWrapper {
        @SerializedName("platform")
        Platform platform;
    }

    public static class Platform {
        @SerializedName("name")
        String nombre;
    }

    public static class Tag {
        @SerializedName("name")
        String nombre;
    }
}