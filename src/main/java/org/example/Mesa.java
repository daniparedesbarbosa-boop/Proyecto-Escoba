package org.example;

import java.util.ArrayList;
import java.util.List;

public class Mesa {
    private List<Carta> cartasEnMesa;

    public Mesa() {
        cartasEnMesa = new ArrayList<>();
    }

    public void añadirCarta(Carta carta) {
        cartasEnMesa.add(carta);
    }

    public void retirarCartas(List<Carta> cartas) {
        cartasEnMesa.removeAll(cartas);
    }

    public List<Carta> getCartas() {
        return cartasEnMesa;
    }

    public boolean mesaVacia() {
        return cartasEnMesa.isEmpty();
    }

    public void mostrarMesa() {
        System.out.println("Cartas en la mesa:");
        for (int i = 1; i < cartasEnMesa.size() + 1; i++) {
            System.out.println(i + " - " + cartasEnMesa.get(i));
        }
    }

    public List<List<Carta>> buscarCombinaciones(Carta cartaJugada) {
        List<List<Carta>> resultado = new ArrayList<>();
        int objetivo = 15 - cartaJugada.getValor();

        buscarRecursivo(resultado, new ArrayList<>(), 0, objetivo);

        return resultado;
    }

    private void buscarRecursivo(List<List<Carta>> resultado, List<Carta> actual, int indice, int restante) {
        if (restante == 0) {
            resultado.add(new ArrayList<>(actual));
            return;
        }

        if (restante < 0 || indice >= cartasEnMesa.size()) {
            return;
        }

        for (int i = indice; i < cartasEnMesa.size(); i++) {
            Carta carta = cartasEnMesa.get(i);

            actual.add(carta);

            buscarRecursivo(resultado, actual, indice + 1, restante - carta.getValor());

            actual.remove(actual.size() - 1);
        }
    }
}
