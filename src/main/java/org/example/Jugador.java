package org.example;

import java.util.*;

public class Jugador {
    private String nombre;
    private List<Carta> mano;
    private MontonJugador monton;
    private int puntosTotales = 0; // Puntos acumulados a lo largo de varias partidas

    public Jugador(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío");
        }
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        this.monton = new MontonJugador();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Carta> getMano() {
        return mano;
    }

    public MontonJugador getMonton() {
        return monton;
    }

    public void recibirCarta(Carta c) {
        mano.add(c);
    }

    public Carta jugarCarta(int indice) {
        if (indice < 0 || indice >= mano.size()) {
            throw new IllegalArgumentException("Índice de carta fuera de rango: " + indice);
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
        this.mano = new ArrayList<>();
        this.monton = new MontonJugador();
    }

    public int getPuntosTotales() {
        return puntosTotales;
    }

    public void addPuntosTotales(int puntos) {
        if (puntos < 0) return;
        this.puntosTotales += puntos;
    }
}
