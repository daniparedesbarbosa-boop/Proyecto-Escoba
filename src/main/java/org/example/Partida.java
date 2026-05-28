package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

public class Partida {
    private final List<Jugador> jugadores;
    private final Baraja baraja;
    private final Mesa mesa;
    private int turnoActual;
    private Jugador ultimoQueCapturo = null;
    private boolean ultimasCartasMostrado = false;

    public Partida(List<String> nombresJugadores) {
        if (nombresJugadores == null || nombresJugadores.isEmpty()) {
            throw new ExcepcionPartida("Debe haber al menos un jugador");
        }
        baraja = new Baraja();
        mesa = new Mesa();
        jugadores = new ArrayList<>();

        for (String nombre : nombresJugadores) {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ExcepcionPartida("El nombre de un jugador no puede estar vacío");
            }
            jugadores.add(new Jugador(nombre));
        }

        turnoActual = 0;
    }

    // Constructor privado usado por la fábrica crearConJugadoresExistentes
    private Partida() {
        baraja = new Baraja();
        mesa = new Mesa();
        jugadores = new ArrayList<>();
        turnoActual = 0;
    }

    /**
     * Fábrica para crear una partida reutilizando instancias de Jugador (manteniendo
     * los puntos acumulados). Se usa en lugar de un constructor con lista genérica
     * para evitar la colisión por borrado de tipos (erasure) entre
     * Partida(List<String>) y Partida(List<Jugador>).
     */
    public static Partida crearConJugadoresExistentes(List<Jugador> jugadoresExistentes) {
        if (jugadoresExistentes == null || jugadoresExistentes.isEmpty()) {
            throw new ExcepcionPartida("Debe haber al menos un jugador");
        }
        Partida p = new Partida();
        // Reiniciar mano/monton de cada jugador pero conservar sus puntos totales
        for (Jugador j : jugadoresExistentes) {
            if (j == null) {
                throw new ExcepcionPartida("La lista de jugadores existentes no puede contener valores nulos");
            }
            j.reiniciarEstadoPartida();
            p.jugadores.add(j);
        }
        return p;
    }

    public void siguienteTurno() {
        turnoActual = (turnoActual + 1) % jugadores.size();
    }

    public void asignarCartasFinales() {
        // Al finalizar la partida, asignar cartas restantes en la mesa al último que capturó
        if (!mesa.mesaVacia() && ultimoQueCapturo != null) {
            List<Carta> cartasFinales = new ArrayList<>(mesa.getCartas());
            ultimoQueCapturo.getMonton().agregarCartas(cartasFinales);
            mesa.limpiarMesa();
        }
    }

    public boolean finPartida() {
        if (baraja.cartasRestantes() > 0) {
            return false;
        }

        return jugadores.stream().allMatch(j -> j.getMano().isEmpty());
    }

    public boolean jugadoresSinCartas() {
        return jugadores.stream().allMatch(j -> j.getMano().isEmpty());
    }

    public boolean debesMostrarUltimas() {
        return baraja.cartasRestantes() == 0 && !ultimasCartasMostrado;
    }

    public void marcarUltimasMostradas() {
        ultimasCartasMostrado = true;
    }


    public void repartirCartas() {
        if (jugadores.isEmpty()) {
            throw new ExcepcionPartida("No se puede repartir cartas sin jugadores");
        }

        for (Jugador j :  jugadores) {
            for (int i = 0; i < 3; i++) {
                Carta carta = baraja.repartirCarta();
                if (carta != null) {
                    j.recibirCarta(carta);
                }
            }
        }
    }

    public int[] mostrarYcalcularPuntos(Vista vista) {
        if (vista == null) {
            throw new ExcepcionPartida("La vista no puede ser nula al calcular los puntos");
        }

        List<Jugador> ganadoresCartas = obtenerGanadores(m -> m.getCartasCapturadas());
        List<Jugador> ganadoresOros = obtenerGanadores(MontonJugador::getOrosCapturados);
        List<Jugador> ganadoresSietes = obtenerGanadores(MontonJugador::getSietes);

        boolean empateCartas = hayEmpate(ganadoresCartas);
        boolean empateOros = hayEmpate(ganadoresOros);
        boolean empateSietes = hayEmpate(ganadoresSietes);

        int[] puntos = calcularPuntosTotales(ganadoresCartas, ganadoresOros, ganadoresSietes,
                empateCartas, empateOros, empateSietes);

        actualizarPuntosTotales(puntos);
        mostrarResultadosRonda(vista, puntos, ganadoresCartas, ganadoresOros, ganadoresSietes,
                empateCartas, empateOros, empateSietes);
        determinarGanador(vista, puntos);

        return puntos;
    }

    private List<Jugador> obtenerGanadores(ToIntFunction<MontonJugador> extractor) {
        int maximo = calcularMaximo(extractor);
        List<Jugador> ganadores = new ArrayList<>();

        for (Jugador jugador : jugadores) {
            if (extractor.applyAsInt(jugador.getMonton()) == maximo) {
                ganadores.add(jugador);
            }
        }

        return ganadores;
    }

    private int calcularMaximo(ToIntFunction<MontonJugador> extractor) {
        int maximo = -1;
        for (Jugador jugador : jugadores) {
            int valor = extractor.applyAsInt(jugador.getMonton());
            if (valor > maximo) {
                maximo = valor;
            }
        }
        return maximo;
    }

    private boolean hayEmpate(List<Jugador> ganadores) {
        return ganadores.size() > 1;
    }

    private void actualizarPuntosTotales(int[] puntos) {
        for (int i = 0; i < jugadores.size(); i++) {
            jugadores.get(i).addPuntosTotales(puntos[i]);
        }
    }

    private void mostrarResultadosRonda(Vista vista, int[] puntos, List<Jugador> ganadoresCartas,
                                        List<Jugador> ganadoresOros, List<Jugador> ganadoresSietes,
                                        boolean empateCartas, boolean empateOros, boolean empateSietes) {
        vista.mostrarTablaResultados(jugadores, ganadoresCartas, ganadoresOros, ganadoresSietes,
                empateCartas, empateOros, empateSietes);
        vista.mostrarPuntosFinales(jugadores, puntos);
    }

    private int[] calcularPuntosTotales(List<Jugador> ganadoresCartas, List<Jugador> ganadoresOros,
                                        List<Jugador> ganadoresSietes, boolean empateCartas,
                                        boolean empateOros, boolean empateSietes) {
        Map<Jugador, Integer> puntosMap = crearMapaPuntosInicial();
        sumarPuntosPorCategoria(puntosMap, ganadoresCartas, empateCartas);
        sumarPuntosPorCategoria(puntosMap, ganadoresOros, empateOros);
        sumarPuntosPorCategoria(puntosMap, ganadoresSietes, empateSietes);
        sumarPuntosPorEscobas(puntosMap);
        sumarPuntosPorVelo(puntosMap);
        return convertirPuntosEnArray(puntosMap);
    }

    private Map<Jugador, Integer> crearMapaPuntosInicial() {
        Map<Jugador, Integer> puntosMap = new HashMap<>();
        for (Jugador jugador : jugadores) {
            puntosMap.put(jugador, 0);
        }
        return puntosMap;
    }

    private void sumarPuntosPorCategoria(Map<Jugador, Integer> puntosMap, List<Jugador> ganadores,
                                         boolean hayEmpate) {
        if (hayEmpate) {
            return;
        }

        for (Jugador ganador : ganadores) {
            puntosMap.put(ganador, puntosMap.get(ganador) + 1);
        }
    }

    private void sumarPuntosPorEscobas(Map<Jugador, Integer> puntosMap) {
        for (Jugador jugador : jugadores) {
            puntosMap.put(jugador, puntosMap.get(jugador) + jugador.getMonton().getEscobas());
        }
    }

    private void sumarPuntosPorVelo(Map<Jugador, Integer> puntosMap) {
        for (Jugador jugador : jugadores) {
            if (jugador.getMonton().getVelo()) {
                puntosMap.put(jugador, puntosMap.get(jugador) + 1);
            }
        }
    }

    private int[] convertirPuntosEnArray(Map<Jugador, Integer> puntosMap) {
        int[] puntos = new int[jugadores.size()];
        for (int i = 0; i < jugadores.size(); i++) {
            puntos[i] = puntosMap.get(jugadores.get(i));
        }

        return puntos;
    }

    private void determinarGanador(Vista vista, int[] puntos) {
        int maxPuntos = -1;
        List<Integer> indicesGanadores = new ArrayList<>();
        for (int i = 0; i < jugadores.size(); i++) {
            if (puntos[i] > maxPuntos) {
                maxPuntos = puntos[i];
                indicesGanadores.clear();
                indicesGanadores.add(i);
            } else if (puntos[i] == maxPuntos) {
                indicesGanadores.add(i);
            }
        }

        // Mensaje de ganador
        if (indicesGanadores.size() == 1) {
            String ganador = jugadores.get(indicesGanadores.get(0)).getNombre();
            boolean esJugador = !ganador.startsWith("CPU");
            vista.mostrarGanador(ganador, esJugador);
        } else {
            vista.mostrarEmpate();
        }
    }

    public Baraja getBaraja() {
        return baraja;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public List<Jugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }

    public Jugador jugadorActual() {
        return jugadores.get(turnoActual);
    }

    public void establecerUltimoCapturador(Jugador jugador) {
        ultimoQueCapturo = jugador;
    }

    public List<Carta> seleccionarMejorCombinacion(List<List<Carta>> combinaciones) {
        if (combinaciones == null || combinaciones.isEmpty()) {
            throw new ExcepcionPartida("No se pueden seleccionar combinaciones vacías");
        }

        List<Carta> mejorCombinacion = null;
        int mejorPuntaje = -1;

        for (List<Carta> combinacion : combinaciones) {
            int puntaje = calcularPuntajeCombinacion(combinacion);

            // Seleccionar la combinación con mayor puntaje
            if (puntaje > mejorPuntaje) {
                mejorPuntaje = puntaje;
                mejorCombinacion = combinacion;
            }
        }

        return mejorCombinacion;
    }

    private static int calcularPuntajeCombinacion(List<Carta> combinacion) {
        if (combinacion == null || combinacion.isEmpty()) {
            throw new ExcepcionPartida("La combinación no puede ser nula o vacía");
        }

        int puntaje = 0;

        // Prioridad 1: Cartas de oros (+1000 puntos si contiene al menos una carta de oros)
        boolean tieneOros = false;
        for (Carta carta : combinacion) {
            if (carta.getPalo() == Carta.OROS) {
                tieneOros = true;
                break;
            }
        }
        if (tieneOros) {
            puntaje += 1000;
        }

        // Prioridad 2: Cartas con valor 7 (+100 puntos por cada siete)
        int cantidadSietes = 0;
        for (Carta carta : combinacion) {
            if (carta.getNumero() == 7) {
                cantidadSietes++;
            }
        }
        puntaje += cantidadSietes * 100;

        // Prioridad 3: Cantidad de cartas (+10 puntos por carta)
        puntaje += combinacion.size() * 10;

        return puntaje;
    }

    public int elegirMejorCartaCPU(List<Carta> mano) {
        if (mano == null || mano.isEmpty()) {
            throw new ExcepcionPartida("La mano de la CPU no puede ser nula o vacía");
        }

        int mejorIndice = 0; // Por defecto, la primera carta
        int mejorPuntaje = Integer.MIN_VALUE;

        for (int i = 0; i < mano.size(); i++) {
            Carta carta = mano.get(i);

            List<List<Carta>> combinaciones = mesa.buscarCombinaciones(carta);
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
            // Si hay combinaciones, usar la mejor combinación posible
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
            // Prioridad extra por poder capturar
            puntaje += 10000;
        } else {
            // Si no hay combinación, priorizar oros y sietes en mano
            if (carta.getPalo() == Carta.OROS) puntaje += 1000;
            if (carta.getNumero() == 7) puntaje += 100;
        }

        return puntaje;
    }
}
