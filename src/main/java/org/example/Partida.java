package org.example;

import java.util.ArrayList;
import java.util.List;

public class Partida {
    private List<Jugador> jugadores;
    private Baraja baraja;
    private Mesa mesa;
    private int turnoActual;

    public Partida(List<String> nombresJugadores) {
        baraja = new Baraja();
        mesa = new Mesa();
        jugadores = new ArrayList<>();

        for (String nombre : nombresJugadores) {
            jugadores.add(new Jugador(nombre));
        }

        turnoActual = 0;
    }

    public void jugarTurno(int indiceCarta) {
        Jugador jugador = jugadores.get(turnoActual);
        Carta jugada = jugador.jugarCarta(indiceCarta);

        System.out.println(jugador.getNombre() + " juega: " + jugada);

        List<List<Carta>> combinaciones = mesa.buscarCombinaciones(jugada);

        if (!combinaciones.isEmpty()) {
            List<Carta> capturadas = combinaciones.get(0);
            mesa.retirarCartas(capturadas);
            capturadas.add(jugada);
            jugador.getMonton().agregarCartas(capturadas);

            if (mesa.mesaVacia()) {
                jugador.getMonton().sumarEscoba();
                System.out.println("¡ESCOBA!");
            } else {
                mesa.añadirCarta(jugada);
                System.out.println("No se hizo combinación");
            }

            siguienteTurno();
        }
    }

    private void siguienteTurno() {
        turnoActual = (turnoActual + 1) % jugadores.size();
    }

    public void jugarPartida() {
        while (!finPartida()) {
            if (jugadoresSinCartas() && baraja.cartasRestantes() > 0) {
                repartirCartas();
            }

            mostrarEstado();

            jugarTurno(0);
        }

        System.out.println("Siguiente partida");
    }

    public boolean finPartida() {
        if (baraja.cartasRestantes() > 0) {
            return false;
        }

        for (Jugador j :  jugadores) {
            if (!j.getMano().isEmpty()){
                return false;
            }
        }

        return true;
    }

    public boolean jugadoresSinCartas() {
        for (Jugador j :  jugadores) {
            if (!j.getMano().isEmpty()){
                return false;
            }
        }

        return true;
    }

    public void repartirCartas() {
        for (Jugador j :  jugadores) {
            for (int i = 0; i < 3; i++) {
                j.recibirCarta(baraja.repartirCarta());
            }
        }
    }
}
