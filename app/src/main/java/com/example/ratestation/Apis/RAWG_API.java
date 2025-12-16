package com.example.ratestation.Apis;

import com.example.ratestation.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Clase encargada de manejar todas las peticiones a la API de RAWG.
 * RAWG es una API de videojuegos que permite:
 *  - Buscar juegos
 *  - Obtener detalles
 *  - Filtrar por géneros o popularidad
 *
 * Todas las peticiones se realizan mediante OkHttp.
 */
public class RAWG_API {

    // URL base de la API
    private static final String BASE_URL = "https://api.rawg.io/api/";

    // Cliente HTTP reutilizable
    private static final OkHttpClient client = new OkHttpClient();


    //  JUEGOS POPULARES
    /**
     * Obtiene los juegos más populares ordenados por calificación.
     *
     * @return JSON como String con la respuesta de la API.
     */
    public static String fetchPopularGames() {
        String url = BASE_URL + "games?key=" + BuildConfig.RAWG_API_KEY + "&ordering=-rating";
        return fetchFromUrl(url);
    }

    //  JUEGOS POR GÉNERO
        /**
     * Obtiene una lista de juegos filtrados por género.
     *
     * @param genreSlug Slug del género (ej: "action", "adventure", "rpg")
     * @return JSON con los juegos correspondientes.
     */
    public static String fetchGamesByGenre(String genreSlug) {
        String url = BASE_URL + "games?key=" + BuildConfig.RAWG_API_KEY + "&genres=" + genreSlug;
        return fetchFromUrl(url);
    }

    //  DETALLES DE JUEGO POR ID
        /**
     * Obtiene información completa y detallada de un videojuego.
     *
     * @param gameId ID del juego en RAWG
     * @return JSON con los detalles completos del juego
     */
    public static String fetchGameDetails(int gameId) {
        String url = BASE_URL + "games/" + gameId + "?key=" + BuildConfig.RAWG_API_KEY;
        return fetchFromUrl(url);
    }


    //  BÚSQUEDA DE JUEGOS (POR TÍTULO)
       /**
     * Busca juegos por título.
     * Este método es el que utiliza el adapter `FavJuegosAdapter`.
     *
     * @param titulo Nombre del juego a buscar
     * @return JSON con los resultados de búsqueda
     */
    public static String buscarJuegoPorTitulo(String titulo) {
        // Reutiliza la función searchGames existente
        return searchGames(titulo);
    }

    /**
     * Método interno para búsqueda genérica por texto.
     *
     * @param query Texto a buscar
     * @return JSON con los resultados
     */
    public static String searchGames(String query) {
        String url = BASE_URL + "games?search=" + query + "&key=" + BuildConfig.RAWG_API_KEY;
        return fetchFromUrl(url);
    }


        /**
     * Método genérico que realiza la petición HTTP GET a la URL indicada.
     *
     * @param url URL completa del endpoint RAWG
     * @return JSON como String, o mensaje de error
     */
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