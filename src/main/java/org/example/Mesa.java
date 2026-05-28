package org.example;

import java.util.ArrayList;
import java.util.List;

public class Mesa {
    private final List<Carta> cartasEnMesa;

    public Mesa() {
        cartasEnMesa = new ArrayList<>();
    }

    public void añadirCarta(Carta carta) {
        if (carta == null) {
            throw new ExcepcionPartida("No se puede añadir una carta nula a la mesa");
        }
        cartasEnMesa.add(carta);
    }

    public void retirarCartas(List<Carta> cartas) {
        if (cartas == null || cartas.isEmpty()) {
            throw new ExcepcionPartida("La lista de cartas a retirar no puede ser nula o vacía");
        }

        if (cartas.stream().anyMatch(c -> c == null)) {
            throw new ExcepcionPartida("La lista de cartas a retirar no puede contener cartas nulas");
        }

        cartasEnMesa.removeAll(cartas);
    }

    public List<Carta> getCartas() {
        return new ArrayList<>(cartasEnMesa);
    }

    public boolean mesaVacia() {
        return cartasEnMesa.isEmpty();
    }

    public boolean contiene(Carta carta) {
        return cartasEnMesa.contains(carta);
    }

    public int getSize() {
        return cartasEnMesa.size();
    }

    public void limpiarMesa() {
        cartasEnMesa.clear();
    }

    public List<List<Carta>> buscarCombinaciones(Carta cartaJugada) {
        if (cartaJugada == null) {
            throw new ExcepcionPartida("La carta jugada no puede ser nula");
        }

        List<List<Carta>> resultado = new ArrayList<>();
        int objetivo = 15 - cartaJugada.getValor();

        buscarRecursivo(resultado, new ArrayList<>(), 0, objetivo, cartasEnMesa);

        return resultado;
    }

    private static void buscarRecursivo(List<List<Carta>> resultado, List<Carta> actual, int indice, int restante, List<Carta> cartas) {
        if (restante == 0) {
            resultado.add(new ArrayList<>(actual));
            return;
        }

        if (restante < 0 || indice >= cartas.size()) {
            return;
        }

        for (int i = indice; i < cartas.size(); i++) {
            Carta carta = cartas.get(i);

            actual.add(carta);

            buscarRecursivo(resultado, actual, i + 1, restante - carta.getValor(), cartas);

            actual.remove(actual.size() - 1);
        }
    }
}


