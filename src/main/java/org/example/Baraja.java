package org.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Baraja {
    private List<Carta> mazo;

    public Baraja() {
        mazo = new ArrayList<>();
        inicializarBaraja();
    }

    private void inicializarBaraja() {
        char[] palos = {
                Carta.OROS,
                Carta.COPAS,
                Carta.ESPADAS,
                Carta.BASTOS
        };

        for (char palo : palos) {
            for (int numero = 1; numero <= 10; numero++) {
                mazo.add(new Carta(numero, palo, numero));
            }
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

    public void mostrarBaraja() {
        for (Carta c : mazo) {
            System.out.println(c);
        }
    }
}
