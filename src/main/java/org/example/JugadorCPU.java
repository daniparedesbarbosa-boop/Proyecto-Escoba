package org.example;

import java.util.ArrayList;
import java.util.List;

public class JugadorCPU extends Jugador {
    public JugadorCPU(String nombre) {
        super(nombre);
    }

    @Override
    public int elegirIndiceCarta(Partida partida, VistaJuego vista) {
        List<Carta> mano = getMano();
        if (mano == null || mano.isEmpty()) {
            throw new ExcepcionPartida("La mano de la CPU no puede ser nula o vacía");
        }

        int mejorIndice = 0;
        int mejorPuntaje = Integer.MIN_VALUE;

        for (int i = 0; i < mano.size(); i++) {
            Carta carta = mano.get(i);
            List<List<Carta>> combinaciones = partida.getMesa().buscarCombinaciones(carta);
            int puntaje = calcularPuntajeCartaCPU(carta, combinaciones);

            if (puntaje > mejorPuntaje) {
                mejorPuntaje = puntaje;
                mejorIndice = i;
            }
        }

        return mejorIndice;
    }

    private static int calcularPuntajeCartaCPU(Carta carta, List<List<Carta>> combinaciones) {
        int puntaje = 0;

        if (!combinaciones.isEmpty()) {
            List<Carta> mejorCombi = new ArrayList<>(combinaciones.get(0));
            if (combinaciones.size() > 1) {
                int mejorPuntajeComb = calcularPuntajeCombinacion(mejorCombi);
                for (List<Carta> combi : combinaciones) {
                    int puntajeCombi = calcularPuntajeCombinacion(combi);
                    if (puntajeCombi > mejorPuntajeComb) {
                        mejorPuntajeComb = puntajeCombi;
                        mejorCombi = new ArrayList<>(combi);
                    }
                }
            }
            puntaje = calcularPuntajeCombinacion(mejorCombi);
            puntaje += 10000;
        } else {
            if (carta.getPalo() == Palo.OROS) puntaje += 1000;
            if (carta.getNumero() == 7) puntaje += 100;
        }

        return puntaje;
    }

    private static int calcularPuntajeCombinacion(List<Carta> combinacion) {
        if (combinacion == null || combinacion.isEmpty()) {
            throw new ExcepcionPartida("La combinación no puede ser nula o vacía");
        }

        int puntaje = 0;

        boolean tieneOros = false;
        for (Carta carta : combinacion) {
            if (carta.getPalo() == Palo.OROS) {
                tieneOros = true;
                break;
            }
        }
        if (tieneOros) {
            puntaje += 1000;
        }

        int cantidadSietes = 0;
        for (Carta carta : combinacion) {
            if (carta.getNumero() == 7) {
                cantidadSietes++;
            }
        }
        puntaje += cantidadSietes * 100;

        puntaje += combinacion.size() * 10;

        return puntaje;
    }
}

