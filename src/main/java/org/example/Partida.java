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

    public void jugarTurno(int indiceCarta) {
        Jugador jugador = jugadores.get(turnoActual);
        Carta jugada = jugador.jugarCarta(indiceCarta);

        System.out.println(jugador.getNombre() + " juega: " + jugada);

        List<List<Carta>> combinaciones = mesa.buscarCombinaciones(jugada);

        if (!combinaciones.isEmpty()) {
            // Evaluar todas las combinaciones y elegir la mejor según prioridades
            List<Carta> mejorCombinacion = seleccionarMejorCombinacion(combinaciones);

            // Hay combinación: se capturan las cartas
            mesa.retirarCartas(mejorCombinacion);
            mejorCombinacion.add(jugada);
            jugador.getMonton().agregarCartas(mejorCombinacion);

            // Mostrar cartas capturadas
            System.out.print("Cartas capturadas: ");
            for (int i = 0; i < mejorCombinacion.size(); i++) {
                System.out.print(mejorCombinacion.get(i));
                if (i < mejorCombinacion.size() - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();

            if (mesa.mesaVacia()) {
                jugador.getMonton().sumarEscoba();
                System.out.println("¡ESCOBA!");
            } else {
                System.out.println("¡Cartas capturadas!");
            }
            ultimoQueCapturo = jugador;
        } else {
            // No hay combinación: la carta se añade a la mesa
            mesa.añadirCarta(jugada);
            System.out.println("No se hizo combinación");
        }

        siguienteTurno();
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

        // Al finalizar la partida, asignar cartas restantes en la mesa al último que capturó
        if (!mesa.mesaVacia() && ultimoQueCapturo != null) {
            List<Carta> cartasFinales = new ArrayList<>(mesa.getCartas());
            ultimoQueCapturo.getMonton().agregarCartas(cartasFinales);
            System.out.print("Cartas finales en la mesa asignadas a " + ultimoQueCapturo.getNombre() + ": ");
            for (int i = 0; i < cartasFinales.size(); i++) {
                System.out.print(cartasFinales.get(i));
                if (i < cartasFinales.size() - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            mesa.getCartas().clear();
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
        System.out.println("\n================================");
        mesa.mostrarMesa();
        System.out.println("================================");

        // Mostrar aviso si es la última ronda (solo una vez cuando la baraja está vacía)
        if (baraja.cartasRestantes() == 0 && !ultimasCartasMostrado) {
            System.out.println("\n¡ÚLTIMAS!");
            ultimasCartasMostrado = true;
        }

        Jugador jugador = jugadorActual();
        System.out.println("\nTurno de: " + jugador.getNombre());
    }

    public Jugador jugadorActual() {
        return jugadores.get(turnoActual);
    }

    public int elegirCarta(Jugador jugador) {
        List<Carta> mano = jugador.getMano();

        // Si es una CPU, elegir la mejor carta según las prioridades
        if (esJugadorCPU(jugador)) {
            return elegirMejorCartaCPU(mano);
        }

        // Si es el jugador humano, muestra opciones
        int opcion;
        do {
            System.out.println("\nTu mano:");
            for (int i = 0; i < mano.size(); i++) {
                System.out.println((i + 1) + " - " + mano.get(i));
            }

            System.out.print("Elige una carta (1-" + mano.size() + "): ");
            try {
                opcion = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Introduce un número válido");
                scanner.nextLine(); // Limpiar el buffer
                opcion = 0; // Valor inválido para continuar el bucle
            }
        } while (opcion < 1 || opcion > mano.size());

        return opcion - 1; // Convertir a índice (0-based)
    }

    private boolean esJugadorCPU(Jugador jugador) {
        return jugador.getNombre().startsWith("CPU");
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

    public void mostrarResultados() {
        System.out.println("\nRESULTADOS FINALES:");
        for (Jugador j :  jugadores) {
            MontonJugador m = j.getMonton();

            System.out.println("\nJugador: " + j.getNombre());

            System.out.println("Cartas: " + m.getCartasCapturadas());
            System.out.println("Oros: " + m.getOrosCapturados());
            System.out.println("Sietes: " + m.getSietes());
            System.out.println("Escobas: " + m.getEscobas());
            System.out.println("Velo: " + (m.getVelo() ? "Sí" : "No"));
        }
    }

    public void calcularPuntos() {
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

        // Imprimir tabla
        System.out.println("\nPuntos:");
        String formato = "| %-12s | %-6s | %-6s | %-6s | %-4s | %-7s |";
        String separador = "+--------------+--------+--------+--------+------+---------+";
        System.out.println(separador);
        System.out.printf(formato, "Jugador", "Cartas", "Oros", "Sietes", "Velo", "Escobas");
        System.out.println();
        System.out.println(separador);
        for (Jugador j : jugadores) {
            MontonJugador m = j.getMonton();
            String nombre = j.getNombre();
            String cartas = String.valueOf(m.getCartasCapturadas());
            String oros = String.valueOf(m.getOrosCapturados());
            String sietes = String.valueOf(m.getSietes());
            String escobas = String.valueOf(m.getEscobas());
            String velo = m.getVelo() ? "Sí" : "No";

            // Marcar ganadores con * solo si no hay empate
            if (!empateCartas && ganadoresCartas.contains(j)) cartas += " *";
            if (!empateOros && ganadoresOros.contains(j)) oros += " *";
            if (!empateSietes && ganadoresSietes.contains(j)) sietes += " *";
            // No marcar * para escobas
            if (m.getVelo()) velo += " *";

            System.out.printf(formato, nombre, cartas, oros, sietes, velo, escobas);
            System.out.println();
        }
        System.out.println(separador);

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
        System.out.println("\nPuntos finales:");
        int maxPuntos = -1;
        List<Integer> indicesGanadores = new ArrayList<>();
        for (int i = 0; i < jugadores.size(); i++) {
            System.out.println(jugadores.get(i).getNombre() + ": " + puntos[i] + " punto" + (puntos[i] == 1 ? "" : "s"));
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
            System.out.println("\n¡El ganador de la partida es: " + ganador + "!");
            // Mensaje especial si el usuario es el ganador
            if (!ganador.startsWith("CPU")) {
                System.out.println("¡Felicidades, has ganado la partida! 🎉");
            } else {
                System.out.println("¡Ánimo! La próxima vez seguro que ganas tú.");
            }
        } else {
            System.out.println("\nLa partida terminó en empate.");
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

    private List<Carta> seleccionarMejorCombinacion(List<List<Carta>> combinaciones) {
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

    private int elegirMejorCartaCPU(List<Carta> mano) {
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
