package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDBManager {

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final SerializadorPartida serializador;
    private final MongoCollection<Document> coleccionPartidas;

    public MongoDBManager() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String uri = dotenv.get("MONGO_URI", "mongodb://localhost:27017");
        String dbName = dotenv.get("MONGO_DATABASE", "escoba");

        this.mongoClient = MongoClients.create(uri);
        this.database = mongoClient.getDatabase(dbName);
        this.coleccionPartidas = database.getCollection("partidas_guardadas");
        this.serializador = new SerializadorPartida();
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
        Document partidaDoc = serializador.toDocument(partida, objetivoPuntos);
        coleccionPartidas.replaceOne(
            Filters.eq("idPartida", partida.getIdPartida()),
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
        Document doc = coleccionPartidas.find(Filters.eq("idPartida", idPartida))
                                        .projection(new Document("objetivoPuntos", 1))
                                        .first();
        return (doc != null) ? doc.getInteger("objetivoPuntos", 31) : 31;
    }
}
