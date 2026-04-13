package org.example;

import java.util.ArrayList;
import java.util.List;

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
        for (Carta c : nuevas) {
            if (c.getPalo() == Carta.OROS) {
                orosCapturados++;
            }
        }
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
        int count = 0;
        for (Carta c : cartas) {
            if (c.getPalo() == Carta.OROS) {
                count++;
            }
        } return count;
    }

    public int getSietes() {
        int contador = 0;
        for (Carta c : cartas) {
            if (c.getNumero() == 7) {
                contador++;
            }
        }
        return contador;
    }

    public boolean getVelo() {
        for (Carta c : cartas) {
            if (c.getNumero() == 7 && c.getPalo() == Carta.OROS) {
                return true;
            }
        } return false;
    }

    public int getCartasCapturadas() {
        return cartasCapturadas;
    }

    public int getOrosCapturados() {
        return orosCapturados;
    }
}
