package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class Participante {
    private final String nombre;
    private final List<Carta> mano;
    private final MontonJugador monton;
    private int puntosTotales;

    protected Participante(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ExcepcionPartida("El nombre del participante no puede estar vacío");
        }
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        this.monton = new MontonJugador();
        this.puntosTotales = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Carta> getMano() {
        return new ArrayList<>(mano);
    }

    public MontonJugador getMonton() {
        return monton;
    }

    public void recibirCarta(Carta carta) {
        if (carta == null) {
            throw new ExcepcionPartida("No se puede recibir una carta nula");
        }
        mano.add(carta);
    }

    public Carta jugarCarta(int indice) {
        if (indice < 0 || indice >= mano.size()) {
            throw new ExcepcionPartida("Índice de carta fuera de rango: " + indice);
        }
        return mano.remove(indice);
    }

    public boolean tieneCartas() {
        return !mano.isEmpty();
    }

    public int getCantidadCartasEnMano() {
        return mano.size();
    }

    public void reiniciarEstadoPartida() {
        mano.clear();
        monton.reiniciar();
    }

    public int getPuntosTotales() {
        return puntosTotales;
    }

    public void addPuntosTotales(int puntos) {
        if (puntos < 0) {
            throw new ExcepcionPartida("No se pueden añadir puntos negativos: " + puntos);
        }
        puntosTotales += puntos;
    }

    public abstract int elegirIndiceCarta(Partida partida, VistaJuego vista);
}
