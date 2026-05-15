package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador: Orquesta la lógica del juego entre Modelo y Vista
 */
public class Controlador {
    private Vista vista;
    private Partida partida;
    private List<String> nombresJugadores;
    private String nombreJugador;

    public Controlador(Vista vista) {
        this.vista = vista;
    }

    public void iniciarJuego() {
        // Mostrar bienvenida
        vista.mostrarBienvenida();

        // Pedir nombre del jugador
        nombreJugador = vista.pedirNombre();

        // Pedir número de rivales
        int numCPUs = vista.pedirNumeroRivales();

        // Construir lista de nombres
        nombresJugadores = construirNombresJugadores(nombreJugador, numCPUs);

        // Mostrar configuración
        List<String> rivales = new ArrayList<>(nombresJugadores);
        rivales.remove(0);
        vista.mostrarConfiguracion(nombreJugador, rivales);

        // Crear partida
        partida = new Partida(nombresJugadores);
        partida.getBaraja().barajar();
        partida.repartirCartas();

        // Poner 4 cartas iniciales en la mesa
        for (int i = 0; i < 4; i++) {
            Carta carta = partida.getBaraja().repartirCarta();
            if (carta != null) {
                partida.getMesa().añadirCarta(carta);
            }
        }

        // Pedir confirmación para comenzar
        if (vista.pedirConfirmacionInicio()) {
            jugarPartida();
            mostrarResultados();
        } else {
            vista.mostrarAdiós();
        }
    }

    private List<String> construirNombresJugadores(String nombreJugador, int numCPUs) {
        List<String> nombres = new ArrayList<>();
        nombres.add(nombreJugador);

        String[] nombresIA = {"CPU1", "CPU2", "CPU3"};
        for (int i = 0; i < numCPUs; i++) {
            nombres.add(nombresIA[i]);
        }

        return nombres;
    }

    private void jugarPartida() {
        while (!partida.finPartida()) {
            if (partida.jugadoresSinCartas() && partida.getBaraja().cartasRestantes() > 0) {
                partida.repartirCartas();
                vista.mostrarNuevasCartas();
            }

            mostrarEstadoJuego();

            Jugador jugador = partida.jugadorActual();

            int cartaElegida = elegirCartaJugador(jugador);

            jugarTurno(cartaElegida);
        }

        // Al finalizar la partida, asignar cartas restantes al último que capturó
        partida.asignarCartasFinales();
        vista.mostrarFinPartida();
    }

    private void mostrarEstadoJuego() {
        System.out.println("\n================================");
        vista.mostrarCartasEnMesa(partida.getMesa().getCartas());
        System.out.println("================================");

        // Mostrar "¡ÚLTIMAS!" solo una vez cuando es la última ronda
        if (partida.debesMostrarUltimas()) {
            vista.mostrarUltimas();
            partida.marcarUltimasMostradas();
        }

        Jugador jugador = partida.jugadorActual();
        vista.mostrarTurnoJugador(jugador.getNombre());
    }

    private int elegirCartaJugador(Jugador jugador) {
        List<Carta> mano = jugador.getMano();

        // Si es una CPU, elegir automáticamente
        if (esCPU(jugador)) {
            return partida.elegirMejorCartaCPU(mano);
        }

        // Si es el jugador humano, pedir input
        return vista.elegirCartaJugador(mano);
    }

    private void jugarTurno(int indiceCarta) {
        Jugador jugador = partida.jugadorActual();
        Carta jugada = jugador.jugarCarta(indiceCarta);

        vista.mostrarJugadaJugador(jugador.getNombre(), jugada);

        List<List<Carta>> combinaciones = partida.getMesa().buscarCombinaciones(jugada);

        if (!combinaciones.isEmpty()) {
            // Hay combinación: se capturan las cartas
            List<Carta> mejorCombinacion = partida.seleccionarMejorCombinacion(combinaciones);
            partida.getMesa().retirarCartas(mejorCombinacion);
            mejorCombinacion.add(jugada);
            jugador.getMonton().agregarCartas(mejorCombinacion);

            vista.mostrarCartasCapturadas(mejorCombinacion);

            if (partida.getMesa().mesaVacia()) {
                jugador.getMonton().sumarEscoba();
                vista.mostrarEscoba();
            } else {
                vista.mostrarCartasCapturadosSinCombinacion();
            }

            partida.establecerUltimoCapturador(jugador);
        } else {
            // No hay combinación: la carta se añade a la mesa
            partida.getMesa().añadirCarta(jugada);
            vista.mostrarSinCombinacion();
        }

        partida.siguienteTurno();
    }

    private boolean esCPU(Jugador jugador) {
        return jugador.getNombre().startsWith("CPU");
    }

    private void mostrarResultados() {
        vista.mostrarEncabezadoResultados();
        partida.mostrarYcalcularPuntos(vista);
    }
}

