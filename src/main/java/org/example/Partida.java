package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

public class Partida {
    private final List<Participante> jugadores;
    private final Baraja baraja;
    private final Mesa mesa;
    private int turnoActual;
    private Participante ultimoQueCapturo = null;
    private boolean ultimasCartasMostrado = false;

    public Partida(List<Participante> participantes) {
        if (participantes == null || participantes.isEmpty()) {
            throw new ExcepcionPartida("Debe haber al menos un jugador");
        }
        baraja = new Baraja();
        mesa = new Mesa();
        jugadores = new ArrayList<>();

        for (Participante p : participantes) {
            if (p == null) {
                throw new ExcepcionPartida("La lista de participantes no puede contener nulos");
            }
            p.reiniciarEstadoPartida();
            jugadores.add(p);
        }

        turnoActual = 0;
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

        for (Participante j :  jugadores) {
            for (int i = 0; i < 3; i++) {
                Carta carta = baraja.repartirCarta();
                if (carta != null) {
                    j.recibirCarta(carta);
                }
            }
        }
    }

    public ResultadoRonda calcularResultadoRonda() {

        List<Participante> ganadoresCartas = obtenerGanadores(m -> m.getCartasCapturadas());
        List<Participante> ganadoresOros = obtenerGanadores(MontonJugador::getOrosCapturados);
        List<Participante> ganadoresSietes = obtenerGanadores(MontonJugador::getSietes);

        boolean empateCartas = hayEmpate(ganadoresCartas);
        boolean empateOros = hayEmpate(ganadoresOros);
        boolean empateSietes = hayEmpate(ganadoresSietes);

        int[] puntos = calcularPuntosTotales(ganadoresCartas, ganadoresOros, ganadoresSietes,
                empateCartas, empateOros, empateSietes);

        actualizarPuntosTotales(puntos);
        return construirResultadoRonda(puntos, ganadoresCartas, ganadoresOros, ganadoresSietes,
                empateCartas, empateOros, empateSietes);
    }

    private List<Participante> obtenerGanadores(ToIntFunction<MontonJugador> extractor) {
        int maximo = calcularMaximo(extractor);
        List<Participante> ganadores = new ArrayList<>();

        for (Participante jugador : jugadores) {
            if (extractor.applyAsInt(jugador.getMonton()) == maximo) {
                ganadores.add(jugador);
            }
        }

        return ganadores;
    }

    private int calcularMaximo(ToIntFunction<MontonJugador> extractor) {
        int maximo = -1;
        for (Participante jugador : jugadores) {
            int valor = extractor.applyAsInt(jugador.getMonton());
            if (valor > maximo) {
                maximo = valor;
            }
        }
        return maximo;
    }

    private boolean hayEmpate(List<Participante> ganadores) {
        return ganadores.size() > 1;
    }

    private void actualizarPuntosTotales(int[] puntos) {
        for (int i = 0; i < jugadores.size(); i++) {
            jugadores.get(i).addPuntosTotales(puntos[i]);
        }
    }

    private ResultadoRonda construirResultadoRonda(int[] puntos, List<Participante> ganadoresCartas,
                                                   List<Participante> ganadoresOros,
                                                   List<Participante> ganadoresSietes,
                                                   boolean empateCartas, boolean empateOros,
                                                   boolean empateSietes) {
        String ganadorNombre = determinarGanadorNombre(puntos);
        boolean empateFinal = ganadorNombre == null;
        boolean ganadorEsJugador = ganadorNombre != null && !ganadorNombre.startsWith("CPU");

        return new ResultadoRonda(jugadores, puntos, ganadoresCartas, ganadoresOros, ganadoresSietes,
                empateCartas, empateOros, empateSietes, ganadorNombre, ganadorEsJugador, empateFinal);
    }

    private int[] calcularPuntosTotales(List<Participante> ganadoresCartas, List<Participante> ganadoresOros,
                                        List<Participante> ganadoresSietes, boolean empateCartas,
                                        boolean empateOros, boolean empateSietes) {
        Map<Participante, Integer> puntosMap = crearMapaPuntosInicial();
        sumarPuntosPorCategoria(puntosMap, ganadoresCartas, empateCartas);
        sumarPuntosPorCategoria(puntosMap, ganadoresOros, empateOros);
        sumarPuntosPorCategoria(puntosMap, ganadoresSietes, empateSietes);
        sumarPuntosPorEscobas(puntosMap);
        sumarPuntosPorVelo(puntosMap);
        return convertirPuntosEnArray(puntosMap);
    }

    private Map<Participante, Integer> crearMapaPuntosInicial() {
        Map<Participante, Integer> puntosMap = new HashMap<>();
        for (Participante jugador : jugadores) {
            puntosMap.put(jugador, 0);
        }
        return puntosMap;
    }

    private void sumarPuntosPorCategoria(Map<Participante, Integer> puntosMap, List<Participante> ganadores,
                                         boolean hayEmpate) {
        if (hayEmpate) {
            return;
        }

        for (Participante ganador : ganadores) {
            puntosMap.put(ganador, puntosMap.get(ganador) + 1);
        }
    }

    private void sumarPuntosPorEscobas(Map<Participante, Integer> puntosMap) {
        for (Participante jugador : jugadores) {
            puntosMap.put(jugador, puntosMap.get(jugador) + jugador.getMonton().getEscobas());
        }
    }

    private void sumarPuntosPorVelo(Map<Participante, Integer> puntosMap) {
        for (Participante jugador : jugadores) {
            if (jugador.getMonton().getVelo()) {
                puntosMap.put(jugador, puntosMap.get(jugador) + 1);
            }
        }
    }

    private int[] convertirPuntosEnArray(Map<Participante, Integer> puntosMap) {
        int[] puntos = new int[jugadores.size()];
        for (int i = 0; i < jugadores.size(); i++) {
            puntos[i] = puntosMap.get(jugadores.get(i));
        }

        return puntos;
    }

    private String determinarGanadorNombre(int[] puntos) {
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

        if (indicesGanadores.size() == 1) {
            return jugadores.get(indicesGanadores.get(0)).getNombre();
        }

        return null;
    }

    public Baraja getBaraja() {
        return baraja;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public List<Participante> getJugadores() {
        return new ArrayList<>(jugadores);
    }

    public Participante jugadorActual() {
        return jugadores.get(turnoActual);
    }

    public void establecerUltimoCapturador(Participante jugador) {
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
            if (carta.getPalo() == Palo.OROS) {
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
            if (carta.getPalo() == Palo.OROS) puntaje += 1000;
            if (carta.getNumero() == 7) puntaje += 100;
        }

        return puntaje;
    }
}
