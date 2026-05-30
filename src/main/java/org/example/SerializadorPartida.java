package org.example;

import org.bson.Document;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SerializadorPartida {

    // --- SERIALIZACIÓN (Java Object -> BSON Document) ---

    public Document toDocument(Partida partida, int objetivoPuntos) {
        if (partida == null) return null;

        Participante ultimoCaptor = partida.getJugadores().stream()
                .filter(p -> p.getNombre().equals(partida.getUltimoQueCapturoNombre()))
                .findFirst().orElse(null);

        return new Document("idPartida", partida.getIdPartida())
                .append("objetivoPuntos", objetivoPuntos)
                .append("fechaGuardado", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .append("jugadores", partida.getJugadores().stream().map(this::participanteToDocument).collect(Collectors.toList()))
                .append("mesa", partida.getMesa().getCartas().stream().map(this::cartaToDocument).collect(Collectors.toList()))
                .append("baraja", partida.getBaraja().getCartas().stream().map(this::cartaToDocument).collect(Collectors.toList()))
                .append("turnoActual", partida.getTurnoActual())
                .append("ultimoQueCapturoNombre", ultimoCaptor != null ? ultimoCaptor.getNombre() : null);
    }

    private Document participanteToDocument(Participante p) {
        // Guardamos solo la información necesaria: clase, nombre y puntos totales.
        // No guardamos las cartas en mano ni el monton para que al cargar
        // la partida todos los jugadores comiencen sin cartas en mano.
        return new Document("_class", p.getClass().getName())
                .append("nombre", p.getNombre())
                .append("puntosTotales", p.getPuntosTotales());
    }

    private Document montonToDocument(MontonJugador m) {
        return new Document("cartas", m.getCartas().stream().map(this::cartaToDocument).collect(Collectors.toList()))
                .append("escobas", m.getEscobas());
    }

    private Document cartaToDocument(Carta c) {
        return new Document("numero", c.getNumero())
                .append("palo", c.getPalo().name());
    }

    // --- DESERIALIZACIÓN (BSON Document -> Java Object) ---

    public Partida fromDocument(Document doc) {
        if (doc == null) return null;

        List<Document> jugadoresDocs = doc.getList("jugadores", Document.class);
        List<Participante> jugadores = jugadoresDocs.stream().map(this::documentToParticipante).collect(Collectors.toList());

        List<Document> mesaDocs = doc.getList("mesa", Document.class);
        Mesa mesa = new Mesa();
        mesaDocs.stream().map(this::documentToCarta).forEach(mesa::añadirCarta);

        List<Document> barajaDocs = doc.getList("baraja", Document.class);
        List<Carta> cartasBaraja = barajaDocs.stream().map(this::documentToCarta).collect(Collectors.toList());
        Baraja baraja = new Baraja(cartasBaraja);

        int turnoActual = doc.getInteger("turnoActual");
        String ultimoQueCapturoNombre = doc.getString("ultimoQueCapturoNombre");
        String idPartida = doc.getString("idPartida");

        return new Partida(idPartida, jugadores, mesa, baraja, turnoActual, ultimoQueCapturoNombre);
    }

    private Participante documentToParticipante(Document doc) {
        String className = doc.getString("_class");
        String nombre = doc.getString("nombre");
        int puntosTotales = doc.getInteger("puntosTotales");

        Participante p;
        if (JugadorHumano.class.getName().equals(className)) {
            p = new JugadorHumano(nombre);
        } else if (JugadorCPU.class.getName().equals(className)) {
            p = new JugadorCPU(nombre);
        } else {
            throw new IllegalArgumentException("Clase de Participante desconocida: " + className);
        }

        p.addPuntosTotales(puntosTotales);

        return p;
    }

    private Carta documentToCarta(Document doc) {
        int numero = doc.getInteger("numero");
        Palo palo = Palo.valueOf(doc.getString("palo"));
        return Carta.crearEspanola(numero, palo);
    }
}
