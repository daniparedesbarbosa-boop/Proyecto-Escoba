package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador: Orquesta la lógica del juego entre Modelo y Vista
 */
public class Controlador {
    private Vista vista;
    private Partida partida;
    private List<String> nombresJugadores;
    private String nombreJugador;
    private Map<String, Jugador> mapajugadores;

    public Controlador(Vista vista) {
        this.vista = vista;
        this.mapajugadores = new HashMap<>();
    }

    public void iniciarJuego() {
        vista.mostrarBienvenida();
        configurarJugadores();
        inicializarPartida();

        if (vista.pedirConfirmacionInicio()) {
            jugarPartida();
            mostrarResultados();
        } else {
            vista.mostrarAdiós();
        }
    }

    private void configurarJugadores() {
        nombreJugador = vista.pedirNombre();
        int numCPUs = vista.pedirNumeroRivales();
        nombresJugadores = construirNombresJugadores(nombreJugador, numCPUs);

        List<String> rivales = new ArrayList<>(nombresJugadores);
        rivales.remove(0);
        vista.mostrarConfiguracion(nombreJugador, rivales);
    }

    private void inicializarPartida() {
        partida = new Partida(nombresJugadores);
        partida.getBaraja().barajar();
        partida.repartirCartas();
        llenarMapaJugadores();
        repartirCartasInicialesMesa();
    }

    private void llenarMapaJugadores() {
        // Mapear nombres a jugadores para búsquedas rápidas
        for (Jugador j : partida.getJugadores()) {
            mapajugadores.put(j.getNombre(), j);
        }
    }

    private void repartirCartasInicialesMesa() {
        for (int i = 0; i < 4; i++) {
            Carta carta = partida.getBaraja().repartirCarta();
            if (carta != null) {
                partida.getMesa().añadirCarta(carta);
            }
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
            repartirCartasSiProcede();
            mostrarEstadoJuego();

            Jugador jugador = partida.jugadorActual();
            int cartaElegida = elegirCartaJugador(jugador);
            jugarTurno(cartaElegida);
        }

        finalizarPartida();
    }

    private void repartirCartasSiProcede() {
        if (partida.jugadoresSinCartas() && partida.getBaraja().cartasRestantes() > 0) {
            partida.repartirCartas();
            vista.mostrarNuevasCartas();
        }
    }

    private void finalizarPartida() {
        partida.asignarCartasFinales();
        vista.mostrarFinPartida();
    }

    private void mostrarEstadoJuego() {
        System.out.println("\n================================");
        vista.mostrarCartasEnMesa(partida.getMesa().getCartas());
        System.out.println("================================");

        mostrarUltimasCartasSiProcede();

        Jugador jugador = partida.jugadorActual();
        vista.mostrarTurnoJugador(jugador.getNombre());
    }

    private void mostrarUltimasCartasSiProcede() {
        if (partida.debesMostrarUltimas()) {
            vista.mostrarUltimas();
            partida.marcarUltimasMostradas();
        }
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
            procesarCaptura(jugador, jugada, combinaciones);
        } else {
            procesarNoCaptura(jugada);
        }

        partida.siguienteTurno();
    }

    private void procesarCaptura(Jugador jugador, Carta jugada, List<List<Carta>> combinaciones) {
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
    }

    private void procesarNoCaptura(Carta jugada) {
        partida.getMesa().añadirCarta(jugada);
        vista.mostrarSinCombinacion();
    }


    private boolean esCPU(Jugador jugador) {
        return jugador.getNombre().startsWith("CPU");
    }

    private void mostrarResultados() {
        vista.mostrarEncabezadoResultados();
        partida.mostrarYcalcularPuntos(vista);
    }
}

