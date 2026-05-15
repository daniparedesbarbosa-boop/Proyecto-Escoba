package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Partida: Modelo - Lógica del juego de Escoba
 */
public class Partida {
    private List<Jugador> jugadores;
    private Baraja baraja;
    private Mesa mesa;
    private int turnoActual;
    private Jugador ultimoQueCapturo = null;
    private boolean ultimasCartasMostrado = false;

    public Partida(List<String> nombresJugadores) {
        baraja = new Baraja();
        mesa = new Mesa();
        jugadores = new ArrayList<>();

        for (String nombre : nombresJugadores) {
            jugadores.add(new Jugador(nombre));
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
            mesa.getCartas().clear();
        }
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

    public boolean debesMostrarUltimas() {
        return baraja.cartasRestantes() == 0 && !ultimasCartasMostrado;
    }

    public void marcarUltimasMostradas() {
        ultimasCartasMostrado = true;
    }


    public void repartirCartas() {
        for (Jugador j :  jugadores) {
            for (int i = 0; i < 3; i++) {
                Carta carta = baraja.repartirCarta();
                if (carta != null) {
                    j.recibirCarta(carta);
                }
            }
        }
    }

    public void mostrarYcalcularPuntos(Vista vista) {
        int maxCartas = -1;
        int maxOros = -1;
        int maxSietes = -1;
        int maxEscobas = -1;

        List<Jugador> ganadoresCartas = new ArrayList<>();
        List<Jugador> ganadoresOros = new ArrayList<>();
        List<Jugador> ganadoresSietes = new ArrayList<>();
        List<Jugador> ganadoresEscobas = new ArrayList<>();

        // Buscar máximos
        for (Jugador j : jugadores) {
            MontonJugador m = j.getMonton();
            int cartas = m.getCartasCapturadas();
            int oros = m.getOrosCapturados();
            int sietes = m.getSietes();
            int escobas = m.getEscobas();
            if (cartas > maxCartas) maxCartas = cartas;
            if (oros > maxOros) maxOros = oros;
            if (sietes > maxSietes) maxSietes = sietes;
            if (escobas > maxEscobas) maxEscobas = escobas;
        }
        // Buscar ganadores (solo si no hay empate)
        for (Jugador j : jugadores) {
            MontonJugador m = j.getMonton();
            if (m.getCartasCapturadas() == maxCartas) ganadoresCartas.add(j);
            if (m.getOrosCapturados() == maxOros) ganadoresOros.add(j);
            if (m.getSietes() == maxSietes) ganadoresSietes.add(j);
            if (m.getEscobas() == maxEscobas && maxEscobas > 0) ganadoresEscobas.add(j);
        }

        // Si hay empate en cartas, oros o sietes, no hay ganador en esa categoría
        boolean empateCartas = ganadoresCartas.size() > 1;
        boolean empateOros = ganadoresOros.size() > 1;
        boolean empateSietes = ganadoresSietes.size() > 1;

        // Mostrar tabla
        vista.mostrarTablaResultados(jugadores, ganadoresCartas, ganadoresOros, ganadoresSietes,
                empateCartas, empateOros, empateSietes);

        // Calcular puntos finales
        int[] puntos = new int[jugadores.size()];
        // Cartas
        if (!empateCartas) {
            for (int i = 0; i < jugadores.size(); i++) {
                if (ganadoresCartas.contains(jugadores.get(i))) puntos[i]++;
            }
        }
        // Oros
        if (!empateOros) {
            for (int i = 0; i < jugadores.size(); i++) {
                if (ganadoresOros.contains(jugadores.get(i))) puntos[i]++;
            }
        }
        // Sietes
        if (!empateSietes) {
            for (int i = 0; i < jugadores.size(); i++) {
                if (ganadoresSietes.contains(jugadores.get(i))) puntos[i]++;
            }
        }
        // Escobas
        for (int i = 0; i < jugadores.size(); i++) {
            puntos[i] += jugadores.get(i).getMonton().getEscobas();
        }
        // Velo
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).getMonton().getVelo()) puntos[i]++;
        }

        // Mostrar puntos finales
        vista.mostrarPuntosFinales(jugadores, puntos);

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

    public Jugador jugadorActual() {
        return jugadores.get(turnoActual);
    }

    public void establecerUltimoCapturador(Jugador jugador) {
        ultimoQueCapturo = jugador;
    }

    public List<Carta> seleccionarMejorCombinacion(List<List<Carta>> combinaciones) {
        List<Carta> mejorCombinacion = null;
        int mejorPuntaje = -1;

        for (List<Carta> combinacion : combinaciones) {
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

            // Seleccionar la combinación con mayor puntaje
            if (puntaje > mejorPuntaje) {
                mejorPuntaje = puntaje;
                mejorCombinacion = combinacion;
            }
        }

        return mejorCombinacion;
    }

    public int elegirMejorCartaCPU(List<Carta> mano) {
        int mejorIndice = 0; // Por defecto, la primera carta
        int mejorPuntaje = Integer.MIN_VALUE;

        for (int i = 0; i < mano.size(); i++) {
            Carta carta = mano.get(i);
            int puntaje = 0;

            List<List<Carta>> combinaciones = mesa.buscarCombinaciones(carta);
            if (!combinaciones.isEmpty()) {
                // Si hay combinaciones, usar la mejor combinación posible
                List<Carta> mejorCombi = seleccionarMejorCombinacion(combinaciones);
                // Puntuar igual que en seleccionarMejorCombinacion
                boolean tieneOros = false;
                int cantidadSietes = 0;
                for (Carta c : mejorCombi) {
                    if (c.getPalo() == Carta.OROS) tieneOros = true;
                    if (c.getNumero() == 7) cantidadSietes++;
                }
                if (tieneOros) puntaje += 1000;
                puntaje += cantidadSietes * 100;
                puntaje += mejorCombi.size() * 10;
                // Prioridad extra por poder capturar
                puntaje += 10000;
            } else {
                // Si no hay combinación, priorizar oros y sietes en mano
                if (carta.getPalo() == Carta.OROS) puntaje += 1000;
                if (carta.getNumero() == 7) puntaje += 100;
            }

            if (puntaje > mejorPuntaje) {
                mejorPuntaje = puntaje;
                mejorIndice = i;
            }
        }
        return mejorIndice;
    }
}
