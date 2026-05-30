package org.example;

import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class Controlador {
    private final VistaJuego vista;
    private Partida partida;
    private List<Participante> jugadoresPersistentes; // Modificado para poder cargarlo
    private String nombreJugador;
    private final GestorArchivos gestorArchivos;
    private final MongoDBManager mongoDBManager;
    private int objetivoPuntos = 31; // por defecto
    private boolean partidaCargadaAlInicio = false;
    private boolean primeraRondaTrasCarga = false;
    private boolean avisoMostradoAlCargar = false;
    private String idPartidaCargada = null;

    public Controlador(VistaJuego vista) {
        if (vista == null) {
            throw new IllegalArgumentException("La vista no puede ser nula");
        }
        this.vista = vista;
        this.jugadoresPersistentes = new ArrayList<>();
        this.gestorArchivos = new GestorArchivos();
        this.mongoDBManager = new MongoDBManager();
    }

    public void iniciarJuego() {
        vista.mostrarBienvenida();

        partidaCargadaAlInicio = intentarCargarPartida();
        primeraRondaTrasCarga = partidaCargadaAlInicio;

        if (!partidaCargadaAlInicio) {
            configurarNuevaPartida();
        } else {
            boolean continuar = vista.pedirContinuarDespuesCarga();
            if (!continuar) {
                vista.mostrarAdios();
                mongoDBManager.close();
                return;
            }
        }

        if (partida == null && (jugadoresPersistentes == null || jugadoresPersistentes.isEmpty())) {
             vista.mostrarAdios();
             mongoDBManager.close();
             return;
        }

        if (partida != null || vista.pedirConfirmacionInicio()) {
            ejecutarPartidasHastaObjetivo();
        } else {
            vista.mostrarAdios();
        }
        mongoDBManager.close();
    }

    private boolean intentarCargarPartida() {
        List<Document> partidasGuardadas = mongoDBManager.cargarResumenPartidas();
        if (partidasGuardadas.isEmpty()) {
            return false;
        }

        if (vista.pedirCargarPartida()) {
            String idPartidaElegida = vista.elegirPartidaGuardada(partidasGuardadas);
            if (idPartidaElegida != null) {
                this.partida = mongoDBManager.cargarPartidaCompleta(idPartidaElegida);
                this.objetivoPuntos = mongoDBManager.obtenerObjetivoPuntos(idPartidaElegida);
                this.jugadoresPersistentes = new ArrayList<>(partida.getJugadores());
                this.nombreJugador = jugadoresPersistentes.get(0).getNombre();
                vista.mostrarPartidaCargada(idPartidaElegida);
                // Mostrar sólo los puntos totales y, si procede, el aviso de objetivo
                vista.mostrarSoloPuntosTotales(partida.getJugadores());
                if (!seAlcanzoObjetivo()) {
                    vista.mostrarAviso(construirMensajeObjetivoNoAlcanzado());
                    avisoMostradoAlCargar = true; // recordamos que ya mostramos el aviso
                }
                idPartidaCargada = idPartidaElegida;
                // Añadimos una línea vacía antes del prompt "¿Comenzamos?"
                System.out.println();
                return true;
            }
        }
        return false;
    }

    private void configurarNuevaPartida() {
        nombreJugador = vista.pedirNombre();
        int numCPUs = vista.pedirNumeroRivales();
        List<String> nombresJugadores = construirNombresJugadores(nombreJugador, numCPUs);

        jugadoresPersistentes.clear();
        jugadoresPersistentes.add(new JugadorHumano(nombreJugador));
        for (String nombre : nombresJugadores) {
            if (!nombre.equals(nombreJugador)) {
                jugadoresPersistentes.add(new JugadorCPU(nombre));
            }
        }
        
        objetivoPuntos = vista.pedirObjetivoPuntos();
        
        List<String> rivales = new ArrayList<>(nombresJugadores);
        rivales.remove(nombreJugador);
        vista.mostrarConfiguracion(nombreJugador, rivales);
    }

    private void ejecutarPartidasHastaObjetivo() {
        boolean objetivoAlcanzado = false;
        boolean salir = false;

        // Si la partida no fue cargada, se inicia una nueva ronda
        if (partida == null) {
            iniciarNuevaRonda();
        }

        while (!objetivoAlcanzado && !salir) {
            ResultadoRonda resultado = resolverRondaActual();

            if (resultado == null) { // El usuario decidió guardar y salir
                salir = true;
            } else {
                if (partidaCargadaAlInicio && primeraRondaTrasCarga) {
                    // Ya mostramos los puntos totales y aviso al cargar la partida,
                    // evitamos repetir la presentación de la ronda y el banner.
                    primeraRondaTrasCarga = false;
                } else {
                    // Mostrar el resultado normal (sin mensaje de ganador, según petición)
                    vista.mostrarResultadoRonda(resultado);
                    boolean deseaGuardar = vista.pedirGuardarEntreRondas();
                    if (deseaGuardar) {
                        try {
                            // Si la sesión empezó cargando una partida, usamos su id original
                            // para asegurarnos de sobrescribir el mismo slot.
                            if (partidaCargadaAlInicio && idPartidaCargada != null) {
                                mongoDBManager.guardarPartida(partida, objetivoPuntos, idPartidaCargada);
                            } else {
                                mongoDBManager.guardarPartida(partida, objetivoPuntos);
                            }
                            vista.mostrarPartidaGuardada();
                        } catch (ExcepcionPersistenciaHistorial e) {
                            vista.mostrarAviso("No se pudo guardar la partida en la base de datos: " + e.getMessage());
                        }
                        salir = true;
                        continue;
                    }
                }
                objetivoAlcanzado = guardarResultadosYComprobarObjetivo(resultado);
                if (!objetivoAlcanzado && !salir) {
                    iniciarNuevaRonda();
                }
            }
        }
    }

    private void iniciarNuevaRonda() {
        partida = new Partida(jugadoresPersistentes);
        partida.getBaraja().barajar();
        partida.repartirCartas();
        repartirCartasInicialesMesa();
    }

    private ResultadoRonda resolverRondaActual() {
        boolean salir = jugarPartida(); // Devuelve true si el usuario quiere salir
        if (salir) {
            return null;
        }
        ResultadoRonda resultado = partida.calcularResultadoRonda();
        return resultado;
    }

    private boolean guardarResultadosYComprobarObjetivo(ResultadoRonda resultado) {
        try {
            guardarHistorialPartida(resultado);
        } catch (ExcepcionPersistenciaHistorial e) {
            vista.mostrarAviso(construirMensajeHistorial(e.getMessage()));
        }

        boolean objetivoAlcanzado = seAlcanzoObjetivo();
        if (!objetivoAlcanzado) {
            if (avisoMostradoAlCargar) {
                // Ya mostramos el aviso al cargar la partida; no lo repetimos.
                avisoMostradoAlCargar = false; // limpiamos la marca
            } else {
                vista.mostrarAviso(construirMensajeObjetivoNoAlcanzado());
            }
        }
        return objetivoAlcanzado;
    }

    private boolean seAlcanzoObjetivo() {
        for (Participante jugador : jugadoresPersistentes) {
            if (jugador.getPuntosTotales() >= objetivoPuntos) {
                return true;
            }
        }
        return false;
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

    private void repartirCartasInicialesMesa() {
        for (int i = 0; i < 4; i++) {
            Carta carta = partida.getBaraja().repartirCarta();
            if (carta != null) {
                partida.getMesa().añadirCarta(carta);
            }
        }
    }

    private boolean jugarPartida() {
        while (!partida.finPartida()) {
            repartirCartasSiProcede();
            mostrarEstadoJuego();

            Participante jugador = partida.jugadorActual();
            int cartaElegida = elegirCartaJugador(jugador);

            jugarTurno(cartaElegida);
        }

        finalizarPartida();
        return false; // No se salió
    }

    private void repartirCartasSiProcede() {
        if (partida.jugadoresSinCartas() && partida.getBaraja().cartasRestantes() > 0) {
            partida.repartirCartas();
            vista.mostrarNuevasCartas();
        }
    }

    private void finalizarPartida() {
        partida.asignarCartasFinales();
        // No mostramos el banner "Fin de la ronda" si estamos finalizando la primera ronda
        // tras haber cargado la partida (ese banner ya no debe aparecer al reanudar).
        if (!(partidaCargadaAlInicio && primeraRondaTrasCarga)) {
            vista.mostrarFinPartida();
        }
    }

    private void mostrarEstadoJuego() {
        System.out.println("\n================================");
        vista.mostrarCartasEnMesa(partida.getMesa().getCartas());
        System.out.println("================================");

        mostrarUltimasCartasSiProcede();

        Participante jugador = partida.jugadorActual();
        vista.mostrarTurnoJugador(jugador.getNombre());
    }

    private void mostrarUltimasCartasSiProcede() {
        if (partida.debesMostrarUltimas()) {
            vista.mostrarUltimas();
            partida.marcarUltimasMostradas();
        }
    }

    private int elegirCartaJugador(Participante jugador) {
        if (!jugador.tieneCartas()) {
            throw new IllegalStateException(construirMensajeSinCartas(jugador.getNombre()));
        }
        // ¡Polimorfismo en acción! No más if/else para CPU vs Humano.
        return jugador.elegirIndiceCarta(partida, vista);
    }

    private void jugarTurno(int indiceCarta) {
        Participante jugador = partida.jugadorActual();
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

    private void procesarCaptura(Participante jugador, Carta jugada, List<List<Carta>> combinaciones) {
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

    private void guardarHistorialPartida(ResultadoRonda resultado) {
        gestorArchivos.guardarHistorialPartida(resultado.getJugadores(), resultado.getPuntos(),
                resultado.getGanadorNombre());
    }

    private String construirMensajeHistorial(String detalle) {
        StringBuilder sb = new StringBuilder();
        sb.append("No se pudo guardar el historial de esta ronda: ").append(detalle);
        return sb.toString();
    }

    private String construirMensajeObjetivoNoAlcanzado() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nadie llegó a los ").append(objetivoPuntos).append(" puntos todavía.");
        return sb.toString();
    }

    private String construirMensajeSinCartas(String nombreJugador) {
        StringBuilder sb = new StringBuilder();
        sb.append("El jugador ").append(nombreJugador).append(" no tiene cartas para jugar");
        return sb.toString();
    }
}
