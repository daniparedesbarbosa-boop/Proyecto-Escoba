package org.example;

import java.util.ArrayList;
import java.util.List;

public class MontonJugador {
    private final List<Carta> cartas;
    private int escobas;
    private int cartasCapturadas = 0;
    private int orosCapturados = 0;

    public MontonJugador() {
        cartas = new ArrayList<>();
        this.escobas = 0;
    }

    public void agregarCartas(List<Carta> nuevas) {
        if (nuevas == null || nuevas.isEmpty()) {
            throw new ExcepcionPartida("La lista de cartas capturadas no puede ser nula o vacía");
        }

        if (nuevas.stream().anyMatch(c -> c == null)) {
            throw new ExcepcionPartida("La lista de cartas capturadas no puede contener cartas nulas");
        }

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

    public void reiniciar() {
        cartas.clear();
        escobas = 0;
        cartasCapturadas = 0;
        orosCapturados = 0;
    }

    public List<Carta> getCartas() {
        return new ArrayList<>(cartas);
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
