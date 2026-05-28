package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MontonJugador {
    private final List<Carta> cartas;
    private final Map<Palo, Integer> cartasPorPalo;
    private final Map<Integer, Integer> cartasPorNumero;
    private int escobas;
    private int cartasCapturadas = 0;
    private int orosCapturados = 0;
    private boolean velo;

    public MontonJugador() {
        cartas = new ArrayList<>();
        cartasPorPalo = new HashMap<>();
        cartasPorNumero = new HashMap<>();
        this.escobas = 0;
        this.velo = false;
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
        actualizarMapas(nuevas);
    }

    private void actualizarOrosCapturados(List<Carta> nuevas) {
        long orosNuevos = nuevas.stream()
                                  .filter(c -> c.getPalo() == Palo.OROS)
                                  .count();
        orosCapturados += orosNuevos;
    }

    private void actualizarMapas(List<Carta> nuevas) {
        for (Carta carta : nuevas) {
            cartasPorPalo.put(carta.getPalo(), cartasPorPalo.getOrDefault(carta.getPalo(), 0) + 1);
            cartasPorNumero.put(carta.getNumero(), cartasPorNumero.getOrDefault(carta.getNumero(), 0) + 1);

            if (carta.getNumero() == 7 && carta.getPalo() == Palo.OROS) {
                velo = true;
            }
        }
    }

    public void sumarEscoba() {
        escobas++;
    }

    public void reiniciar() {
        cartas.clear();
        cartasPorPalo.clear();
        cartasPorNumero.clear();
        escobas = 0;
        cartasCapturadas = 0;
        orosCapturados = 0;
        velo = false;
    }

    public List<Carta> getCartas() {
        return new ArrayList<>(cartas);
    }

    public int getEscobas() {
        return escobas;
    }

    public void setEscobas(int escobas) {
        this.escobas = escobas;
    }

    public int getOros() {
        return cartasPorPalo.getOrDefault(Palo.OROS, 0);
    }

    public int getSietes() {
        return cartasPorNumero.getOrDefault(7, 0);
    }

    public boolean getVelo() {
        return velo;
    }


    public int getCartasCapturadas() {
        return cartasCapturadas;
    }

    public int getOrosCapturados() {
        return orosCapturados;
    }
}
