package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Partida {
    private List<Jugador> jugadores;
    private Baraja baraja;
    private Mesa mesa;
    private int turnoActual;
    private Scanner scanner = new Scanner(System.in);

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
                System.out.println("Nuevas cartas");
            }

            mostrarEstado();

            Jugador jugador = jugadorActual();

            int cartaElegida = elegirCarta(jugador);

            jugarTurno(cartaElegida);
        }

        System.out.println("Fin de la partida");
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

    public void mostrarEstado() {
        System.out.println("\n======================");
        System.out.println("Cartas en mesa:");
        mesa.mostrarMesa();

        Jugador jugador = jugadorActual();

        System.out.println("\nTurno de: " + jugador.getNombre());
        System.out.println("Tu mano: ");

        List<Carta> mano = jugador.getMano();

        for (int i = 0; i < mano.size(); i++) {
            System.out.println(i + " - " + mano.get(i));
        }

        System.out.println("======================");
    }

    public Jugador jugadorActual() {
        return jugadores.get(turnoActual);
    }

    public int elegirCarta(Jugador jugador) {
        List<Carta> mano = jugador.getMano();
        int opcion;

        do {
            System.out.println("\nElige una carta:");

            for (int i = 0; i < mano.size(); i++) {
                System.out.println(i + " - " + mano.get(i));
            }

            opcion = scanner.nextInt();
        } while (opcion < 0 || opcion >= mano.size());

        return opcion;
    }

    public void repartirCartas() {
        for (Jugador j :  jugadores) {
            for (int i = 0; i < 3; i++) {
                j.recibirCarta(baraja.repartirCarta());
            }
        }
    }

    public void mostrarResultados() {
        System.out.println("\nRESULTADOS FINALES:");
        for (Jugador j :  jugadores) {
            MontonJugador m = j.getMonton();

            System.out.println("\nJugador: " + j.getNombre());

            System.out.println("Cartas: " + m.getCartas().size());
            System.out.println("Oros: " + m.getOros());
            System.out.println("Sietes: " + m.getSietes());
            System.out.println("Escobas: " + m.getEscobas());
            System.out.println("Velo: " + (m.getVelo() ? "Sí" : "No"));
        }
    }

    public void calcularPuntos() {
        int maxCartas = -1;
        int maxOros = -1;
        int maxSietes = -1;

        Jugador ganadorCartas = null;
        Jugador ganadorOros = null;
        Jugador ganadorSietes = null;

        for (Jugador j :  jugadores) {
            MontonJugador m = j.getMonton();

            int cartas = m.getCartas().size();
            int oros = m.getOros();
            int sietes = m.getSietes();

            if (cartas > maxCartas) {
                maxCartas = cartas;
                ganadorCartas = j;
            }

            if (oros > maxOros) {
                maxOros = oros;
                ganadorOros = j;
            }

            if (sietes > maxSietes) {
                maxSietes = sietes;
                ganadorSietes = j;
            }
        }

        System.out.println("\nPuntos:");

        if (ganadorCartas != null) {
            System.out.println("Gana a cartas: " + ganadorCartas.getNombre());
        }

        if (ganadorOros != null) {
            System.out.println("Gana a oros: " + ganadorOros.getNombre());
        }

        if (ganadorSietes != null) {
            System.out.println("Gana a sietes: " + ganadorSietes.getNombre());
        }

        for (Jugador j :  jugadores) {
            MontonJugador m = j.getMonton();

            if (m.getVelo()) {
                System.out.println("Tiene el velo: " + j.getNombre());
            }

            if (m.getEscobas() > 0) {
                System.out.println("Escobas de " + j.getNombre() + ": " + m.getEscobas());
            }
        }
    }

    public Baraja getBaraja() {
        return baraja;
    }

    public void distribuirCartasIniciales() {
        repartirCartas();

        for (int i = 0; i < 4; i++) {
            Carta carta = baraja.repartirCarta();
            if (carta != null) {
                mesa.añadirCarta(carta);
            }
        }
    }

    public Mesa getMesa() {
        return mesa;
    }

}
