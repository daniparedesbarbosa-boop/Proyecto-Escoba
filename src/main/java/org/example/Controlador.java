package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controlador {
    private final Vista vista;
    private Partida partida;
    private final List<String> nombresJugadores;
    private String nombreJugador;
    private final Map<String, Jugador> mapajugadores;
    private final GestorArchivos gestorArchivos;
    private final List<Jugador> jugadoresPersistentes;
    private int objetivoPuntos = 31; // por defecto

    public Controlador(Vista vista) {
        if (vista == null) {
            throw new IllegalArgumentException("La vista no puede ser nula");
        }
        this.vista = vista;
        this.nombresJugadores = new ArrayList<>();
        this.mapajugadores = new HashMap<>();
        this.gestorArchivos = new GestorArchivos();
        this.jugadoresPersistentes = new ArrayList<>();
    }

    public void iniciarJuego() {
        vista.mostrarBienvenida();
        configurarJugadores();
        objetivoPuntos = vista.pedirObjetivoPuntos();

        prepararJugadoresPersistentes();

        if (vista.pedirConfirmacionInicio()) {
            ejecutarPartidasHastaObjetivo();
        } else {
            vista.mostrarAdiós();
        }
    }

    private void prepararJugadoresPersistentes() {
        jugadoresPersistentes.clear();
        mapajugadores.clear();

        for (String nombre : nombresJugadores) {
            Jugador jugador = new Jugador(nombre);
            jugadoresPersistentes.add(jugador);
            mapajugadores.put(jugador.getNombre(), jugador);
        }
    }

    private void ejecutarPartidasHastaObjetivo() {
        boolean objetivoAlcanzado = false;

        while (!objetivoAlcanzado) {
            iniciarNuevaRonda();
            int[] puntosPartida = resolverRondaActual();
            objetivoAlcanzado = guardarResultadosYComprobarObjetivo(puntosPartida);
        }
    }

    private void iniciarNuevaRonda() {
        partida = Partida.crearConJugadoresExistentes(jugadoresPersistentes);
        partida.getBaraja().barajar();
        partida.repartirCartas();
        repartirCartasInicialesMesa();
    }

    private int[] resolverRondaActual() {
        jugarPartida();
        vista.mostrarEncabezadoResultados();
        return partida.mostrarYcalcularPuntos(vista);
    }

    private boolean guardarResultadosYComprobarObjetivo(int[] puntosPartida) {
        try {
            guardarHistorialPartida(puntosPartida);
        } catch (ExcepcionPersistenciaHistorial e) {
            vista.mostrarAviso("No se pudo guardar el historial de esta ronda: " + e.getMessage());
        }

        boolean objetivoAlcanzado = seAlcanzoObjetivo();
        if (!objetivoAlcanzado) {
            vista.mostrarAviso("Nadie llegó a los " + objetivoPuntos + " puntos todavía.");
        }
        return objetivoAlcanzado;
    }

    private boolean seAlcanzoObjetivo() {
        for (Jugador jugador : jugadoresPersistentes) {
            if (jugador.getPuntosTotales() >= objetivoPuntos) {
                return true;
            }
        }
        return false;
    }

    private void configurarJugadores() {
        nombreJugador = vista.pedirNombre();
        int numCPUs = vista.pedirNumeroRivales();
        nombresJugadores.clear();
        nombresJugadores.addAll(construirNombresJugadores(nombreJugador, numCPUs));

        List<String> rivales = new ArrayList<>(nombresJugadores);
        rivales.remove(0);
        vista.mostrarConfiguracion(nombreJugador, rivales);
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

        if (mano.isEmpty()) {
            throw new IllegalStateException("El jugador " + jugador.getNombre() + " no tiene cartas para jugar");
        }

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


    private void guardarHistorialPartida(int[] puntos) {
        String ganador = obtenerNombreGanador(puntos);
        gestorArchivos.guardarHistorialPartida(partida.getJugadores(), puntos, ganador);
    }

    private String obtenerNombreGanador(int[] puntos) {
        int maxPuntos = -1;
        List<Integer> indicesGanadores = new ArrayList<>();

        for (int i = 0; i < puntos.length; i++) {
            if (puntos[i] > maxPuntos) {
                maxPuntos = puntos[i];
                indicesGanadores.clear();
                indicesGanadores.add(i);
            } else if (puntos[i] == maxPuntos) {
                indicesGanadores.add(i);
            }
        }

        if (indicesGanadores.size() != 1) {
            return null;
        }

        return partida.getJugadores().get(indicesGanadores.get(0)).getNombre();
    }
}

