package com.example.ratestation.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ratestation.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Activity_Podcast extends AppCompatActivity {

    // Interfaz de Callback para el parsing RSS
    private interface RSSCallback {
        void onSuccess(String description);
        void onFailure(String errorMessage);
    }

    private static final String TAG = "ActivityPodcastDebug";
    private static final String ITUNES_NAMESPACE = "http://www.itunes.com/dtds/podcast-1.0.dtd";

    private ImageView imgCover;
    private TextView txtTitulo, txtCreador, txtGenero, txtDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast); // Asegura que este layout existe

        // Obtengo las  referencias de la UI
        imgCover = findViewById(R.id.imgDetailCover); // Asegura IDs correctos
        txtTitulo = findViewById(R.id.txtDetailTitle);
        txtCreador = findViewById(R.id.txtDetailCreator);
        txtGenero = findViewById(R.id.txtDetailGenre);
        txtDescripcion = findViewById(R.id.txtDetailDescription);

        // Recupero datos del Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String titulo = extras.getString("titulo");
            String coverUrl = extras.getString("artworkPath"); // Usamos artworkPath
            String creador = extras.getString("creador");
            String genero = extras.getString("genero");
            String feedUrl = extras.getString("feedUrl"); // URL del RSS/XML

            // Muestro los datos estáticos
            txtTitulo.setText(titulo);
            txtCreador.setText("Creador: " + creador);
            txtGenero.setText("Género: " + (genero != null ? genero : "N/D"));

            // Cargo la imagen
            Glide.with(this)
                    .load(coverUrl)
                    .placeholder(R.drawable.ratelogo)
                    .into(imgCover);

            // Inicio la descarga de la descripción si la URL existe
            if (feedUrl != null && !feedUrl.isEmpty()) {
                txtDescripcion.setText("Cargando descripción detallada...");

                loadDescriptionFromFeed(feedUrl, new RSSCallback() {

                    @Override
                    public void onSuccess(String description) {
                        runOnUiThread(() -> {
                            // Limpio y formateo al final del texto
                            String cleanDescription = description.trim();

                            if (cleanDescription.startsWith("<![CDATA[") && cleanDescription.endsWith("]]>")) {
                                cleanDescription = cleanDescription.substring(9, cleanDescription.length() - 3).trim();
                            }
                            // Reemplazo saltos de línea HTML
                            cleanDescription = cleanDescription.replaceAll("(?i)<br\\s*/?>", "\n");

                            txtDescripcion.setText(cleanDescription);
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        runOnUiThread(() -> {
                            Log.e(TAG, "FALLO en RSS: " + errorMessage);
                            txtDescripcion.setText(errorMessage);
                        });
                    }
                });

            } else {
                txtDescripcion.setText("No hay una URL de feed RSS disponible.");
            }
        }
    }


    private void loadDescriptionFromFeed(String feedUrl, RSSCallback callback) {
        //Se ejecuta el hilo secundario para intentar evitar el bloqueo de la UI
        new Thread(() -> {
            String description;
            try {
                description = parseRSSFeed(feedUrl);
                callback.onSuccess(description);
            } catch (Exception e) {
                // Capturamos cualquier excepción de red o parsing
                Log.e(TAG, "Error fatal al parsear el feed RSS: " + e.getMessage(), e);
                callback.onFailure("Error al cargar la descripción: " + e.getMessage());
            }
        }).start();
    }


    /**
     * Descarga y analiza el feed RSS (XML) buscando el campo de descripción/resumen del show (<channel>).
     * @param feedUrl La URL del feed RSS.
     * @return La descripción del podcast o un mensaje de no encontrado.
     */
    private String parseRSSFeed(String feedUrl) throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(feedUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            // Configuración de la conexión HTTP
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setInstanceFollowRedirects(true); // Manejo de redireccionamientos
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                // Lanza una excepción si el código HTTP no es 200
                throw new Exception("Error HTTP al descargar el feed: Código " + responseCode);
            }

            inputStream = urlConnection.getInputStream();
            parser.setInput(inputStream, null);

            // Lógica de parsing con manejo de namespace y búsqueda en <channel>
            int eventType = parser.getEventType();
            boolean inChannel = false; // Bandera para buscar solo metadatos del show

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        String namespace = parser.getNamespace();

                        if ("channel".equalsIgnoreCase(tagName)) {
                            inChannel = true;
                        }

                        if (inChannel) {
                            // Descripción estándar de RSS
                            if ("description".equalsIgnoreCase(tagName) && "".equals(namespace)) {
                                if (parser.next() == XmlPullParser.TEXT) {
                                    return parser.getText();
                                }
                            }

                            // Opción 2: Resumen de iTunes (usa namespace ITUNES_NAMESPACE)
                            if ("summary".equalsIgnoreCase(tagName) && ITUNES_NAMESPACE.equalsIgnoreCase(namespace)) {
                                if (parser.next() == XmlPullParser.TEXT) {
                                    return parser.getText();
                                }
                            }

                            // Se detiene la búsqueda de la descripción del show al encontrar el primer episodio
                            if ("item".equalsIgnoreCase(tagName)) {
                                inChannel = false;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if ("channel".equalsIgnoreCase(parser.getName())) {
                            inChannel = false;
                        }
                        break;
                }
                eventType = parser.next();
            }

        } finally {
            // Se cierran los recursos
            if (inputStream != null) {
                inputStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return "Descripción no encontrada en la sección principal del feed.";
    }
}