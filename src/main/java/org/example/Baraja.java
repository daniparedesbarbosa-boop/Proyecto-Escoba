package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baraja {
    private final List<Carta> mazo;

    public Baraja() {
        mazo = new ArrayList<>();
        inicializarBaraja();
    }

    private void inicializarBaraja() {
        for (Palo palo : Palo.values()) {
            crearCartasDelPalo(palo);
        }
    }

    private void crearCartasDelPalo(Palo palo) {
        for (int numero = 1; numero <= 10; numero++) {
            mazo.add(Carta.crearEspanola(numero, palo));
        }
    }

    public void barajar() {
        Collections.shuffle(mazo);
    }

    public Carta repartirCarta() {
        if (mazo.isEmpty()) {
            return null;
        }
        return mazo.remove(0);
    }

    public int cartasRestantes() {
        return mazo.size();
    }

}
