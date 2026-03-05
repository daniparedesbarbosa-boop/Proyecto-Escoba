package org.example;

import java.util.*;

public class Jugador {
    private String nombre;
    private List<Carta> mano;
    private MontonJugador monton;

    public Jugador(String nombre) {
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
        return mano.remove(indice);
    }
}
