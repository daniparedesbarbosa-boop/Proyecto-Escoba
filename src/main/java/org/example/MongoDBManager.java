package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import java.util.concurrent.TimeUnit;

import java.util.ArrayList;
import java.util.List;

public class MongoDBManager {

    private final MongoClient mongoClient;
    private final SerializadorPartida serializador;
    private final MongoCollection<Document> coleccionPartidas;

    public MongoDBManager() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String uri = dotenv.get("MONGO_URI", "mongodb://localhost:27017");
        String dbName = dotenv.get("MONGO_DATABASE", "escoba");

        if (uri == null || uri.isBlank()) {
            throw new ExcepcionPersistenciaHistorial("MONGO_URI no está configurada");
        }
        if (dbName == null || dbName.isBlank()) {
            throw new ExcepcionPersistenciaHistorial("MONGO_DATABASE no está configurada");
        }

        this.serializador = new SerializadorPartida();

        MongoClient client = null;
        MongoCollection<Document> coll = null;

        try {
            // Construimos settings con timeouts cortos para no bloquear demasiado en caso de red inaccesible
            ConnectionString cs = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(cs)
                    .applyToClusterSettings(builder -> builder.serverSelectionTimeout(10, TimeUnit.SECONDS))
                    .applyToSocketSettings(builder -> builder.connectTimeout(10, TimeUnit.SECONDS))
                    .build();

            client = MongoClients.create(settings);
            MongoDatabase db = client.getDatabase(dbName);
            coll = db.getCollection("partidas_guardadas");

            // Intentamos un ping (fallará rápido si no hay conexión)
            try {
                db.runCommand(new Document("ping", 1));
            } catch (Exception pingEx) {
                System.err.println("[MongoDBManager] Ping a MongoDB falló: " + pingEx.getMessage());
                try { client.close(); } catch (Exception ignore) {}
                client = null;
                coll = null;
            }
        } catch (Exception e) {
            System.err.println("[MongoDBManager] No se pudo conectar a MongoDB: " + e.getMessage());
            // Dejamos los campos en null para que el resto del programa funcione sin persistencia
            client = null;
            coll = null;
        }

        this.mongoClient = client;
        this.coleccionPartidas = coll;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    /**
     * Guarda o actualiza el estado de una partida en la colección 'partidas_guardadas'.
     * Utiliza el ID de la partida para realizar un 'upsert'.
     * @param partida La partida a guardar.
     * @param objetivoPuntos El objetivo de puntos de la partida.
     */
    public void guardarPartida(Partida partida, int objetivoPuntos) {
        if (coleccionPartidas == null) {
            throw new ExcepcionPersistenciaHistorial("No hay conexión disponible con la base de datos");
        }

        Document partidaDoc = serializador.toDocument(partida, objetivoPuntos);
        coleccionPartidas.replaceOne(
                Filters.eq("idPartida", partida.getIdPartida()),
                partidaDoc,
                new ReplaceOptions().upsert(true)
        );
    }

    /**
     * Guarda o actualiza la partida usando un id explícito si se proporciona.
     * Esto evita crear un nuevo slot cuando la partida fue cargada y su id
     * en memoria difiere por alguna razón.
     */
    public void guardarPartida(Partida partida, int objetivoPuntos, String idOverride) {
        if (coleccionPartidas == null) {
            throw new ExcepcionPersistenciaHistorial("No hay conexión disponible con la base de datos");
        }

        String idToUse = (idOverride != null && !idOverride.isBlank()) ? idOverride : partida.getIdPartida();
        Document partidaDoc = serializador.toDocument(partida, objetivoPuntos);
        // Aseguramos que el documento contiene el id que vamos a usar para el upsert
        partidaDoc.put("idPartida", idToUse);

        coleccionPartidas.replaceOne(
                Filters.eq("idPartida", idToUse),
                partidaDoc,
                new ReplaceOptions().upsert(true)
        );
    }

    /**
     * Carga una lista de resúmenes de las partidas guardadas.
     * @return Una lista de documentos con información básica de cada partida.
     */
    public List<Document> cargarResumenPartidas() {
        List<Document> resumenes = new ArrayList<>();
        if (coleccionPartidas == null) {
            return resumenes; // devolvemos vacío si no hay conexión
        }

        // Proyección para obtener solo los datos necesarios para el resumen
        Document proyeccion = new Document("idPartida", 1)
                .append("fechaGuardado", 1)
                .append("jugadores.nombre", 1)
                .append("jugadores.puntosTotales", 1);

        for (Document doc : coleccionPartidas.find().projection(proyeccion)) {
            resumenes.add(doc);
        }
        return resumenes;
    }

    /**
     * Carga una partida completa desde la base de datos usando su ID.
     * @param idPartida El ID de la partida a cargar.
     * @return Un objeto Partida reconstruido, o null si no se encuentra.
     */
    public Partida cargarPartidaCompleta(String idPartida) {
        if (coleccionPartidas == null) return null;
        Document partidaDoc = coleccionPartidas.find(Filters.eq("idPartida", idPartida)).first();
        if (partidaDoc != null) {
            return serializador.fromDocument(partidaDoc);
        }
        return null;
    }

    /**
     * Obtiene el objetivo de puntos de una partida guardada.
     * @param idPartida El ID de la partida.
     * @return El objetivo de puntos.
     */
    public int obtenerObjetivoPuntos(String idPartida) {
        if (coleccionPartidas == null) return 31;
        Document doc = coleccionPartidas.find(Filters.eq("idPartida", idPartida))
                .projection(new Document("objetivoPuntos", 1))
                .first();
        return (doc != null) ? doc.getInteger("objetivoPuntos", 31) : 31;
    }
}
