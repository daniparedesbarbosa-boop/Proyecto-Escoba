package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * MontonJugador: Modelo - Gestiona las cartas capturadas por un jugador
 */
public class MontonJugador {
    private List<Carta> cartas;
    private int escobas;
    private int cartasCapturadas = 0;
    private int orosCapturados = 0;

    public MontonJugador() {
        cartas = new ArrayList<>();
        this.escobas = 0;
    }

    public void agregarCartas(List<Carta> nuevas) {
        cartas.addAll(nuevas);
        cartasCapturadas += nuevas.size();
        actualizarOrosCapturados(nuevas);
    }

    private void actualizarOrosCapturados(List<Carta> nuevas) {
        long orosNuevos = nuevas.stream()
                                  .filter(c -> c.getPalo() == Carta.OROS)
                                  .count();
        orosCapturados += orosNuevos;
    }

    public void sumarEscoba() {
        escobas++;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public int getEscobas() {
        return escobas;
    }

    public int getOros() {
        return (int) cartas.stream()
                           .filter(c -> c.getPalo() == Carta.OROS)
                           .count();
    }

    public int getSietes() {
        return (int) cartas.stream()
                           .filter(c -> c.getNumero() == 7)
                           .count();
    }

    public boolean getVelo() {
        return cartas.stream()
                     .anyMatch(c -> c.getNumero() == 7 && c.getPalo() == Carta.OROS);
    }


    public int getCartasCapturadas() {
        return cartasCapturadas;
    }

    public int getOrosCapturados() {
        return orosCapturados;
    }
}
