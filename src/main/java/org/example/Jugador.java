package org.example;

import java.util.*;

public class Jugador {
    private final String nombre;
    private final List<Carta> mano;
    private final MontonJugador monton;
    private int puntosTotales = 0; // Puntos acumulados a lo largo de varias partidas

    public Jugador(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ExcepcionPartida("El nombre del jugador no puede estar vacío");
        }
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        this.monton = new MontonJugador();
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

    public void recibirCarta(Carta c) {
        if (c == null) {
            throw new ExcepcionPartida("No se puede recibir una carta nula");
        }
        mano.add(c);
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

    /**
     * Reinicia el estado del jugador para una nueva partida (limpia mano y montón),
     * pero mantiene los puntos totales acumulados.
     */
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
        this.puntosTotales += puntos;
    }
}
