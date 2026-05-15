package org.example;

import java.util.List;
import java.util.Scanner;

/**
 * Vista: Maneja toda la presentación y entrada del usuario
 */
public class Vista {
    private Scanner scanner;

    public Vista() {
        this.scanner = new Scanner(System.in);
    }

    // ===== MÉTODOS DE BIENVENIDA Y CONFIGURACIÓN =====

    public void mostrarBienvenida() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║        ¡BIENVENIDO A LA ESCOBA!        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }

    public String pedirNombre() {
        System.out.print("Inserta tu nombre: ");
        String nombre = scanner.nextLine().trim();
        return nombre.isEmpty() ? "Jugador" : nombre;
    }

    public int pedirNumeroRivales() {
        int numCPUs = 0;
        while (numCPUs < 1 || numCPUs > 3) {
            System.out.print("¿Contra cuántos quieres jugar? (1-3): ");
            try {
                numCPUs = scanner.nextInt();
                scanner.nextLine();

                if (numCPUs < 1 || numCPUs > 3) {
                    System.out.println("Introduce un número entre 1 y 3");
                    numCPUs = 0;
                }
            } catch (Exception e) {
                System.out.println("Introduce un número válido");
                scanner.nextLine();
                numCPUs = 0;
            }
        }
        return numCPUs;
    }

    public void mostrarConfiguracion(String nombreJugador, List<String> rivales) {
        System.out.println();
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║           EMPIEZA LA PARTIDA          ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
        System.out.println("Jugador: " + nombreJugador);
        System.out.print("Rivales: ");
        for (int i = 0; i < rivales.size(); i++) {
            System.out.print(rivales.get(i));
            if (i < rivales.size() - 1) System.out.print(", ");
        }
        System.out.println();
        System.out.println();
    }

    public boolean pedirConfirmacionInicio() {
        System.out.print("¿Comenzamos? (S/N): ");
        String respuesta = scanner.nextLine().trim();
        return respuesta.equalsIgnoreCase("S");
    }

    // ===== MÉTODOS DE JUEGO =====

    public void mostrarCartasEnMesa(List<Carta> cartas) {
        System.out.println("\nCartas en la mesa:");
        if (cartas.isEmpty()) {
            System.out.println("(vacía)");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cartas.size(); i++) {
                sb.append(cartas.get(i));
                if (i < cartas.size() - 1) {
                    sb.append(" | ");
                }
            }
            System.out.println(sb.toString());
        }
    }

    public void mostrarUltimas() {
        System.out.println("\n¡ÚLTIMAS!");
    }

    public void mostrarNuevasCartas() {
        System.out.println("Nuevas cartas repartidas");
    }

    public void mostrarTurnoJugador(String nombreJugador) {
        System.out.println("\nTurno de: " + nombreJugador);
    }

    public void mostrarCartasJugador(List<Carta> mano) {
        System.out.println("\nTu mano:");
        for (int i = 0; i < mano.size(); i++) {
            System.out.println((i + 1) + " - " + mano.get(i));
        }
    }

    public int elegirCartaJugador(List<Carta> mano) {
        int opcion;
        do {
            mostrarCartasJugador(mano);
            System.out.print("Elige una carta (1-" + mano.size() + "): ");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Introduce un número válido");
                scanner.nextLine();
                opcion = 0;
            }
        } while (opcion < 1 || opcion > mano.size());

        return opcion - 1;
    }

    public void mostrarJugadaJugador(String nombreJugador, Carta carta) {
        System.out.println(nombreJugador + " juega: " + carta);
    }

    public void mostrarCartasCapturadas(List<Carta> cartas) {
        System.out.print("Cartas capturadas: ");
        for (int i = 0; i < cartas.size(); i++) {
            System.out.print(cartas.get(i));
            if (i < cartas.size() - 1) {
                System.out.print(" | ");
            }
        }
        System.out.println();
    }

    public void mostrarEscoba() {
        System.out.println("¡ESCOBA!");
    }

    public void mostrarCartasCapturadosSinCombinacion() {
        System.out.println("¡Cartas capturadas!");
    }

    public void mostrarSinCombinacion() {
        System.out.println("No se hizo combinación");
    }

    public void mostrarCartasFinales(String nombreJugador, List<Carta> cartas) {
        System.out.print("Cartas finales en la mesa asignadas a " + nombreJugador + ": ");
        for (int i = 0; i < cartas.size(); i++) {
            System.out.print(cartas.get(i));
            if (i < cartas.size() - 1) {
                System.out.print(" | ");
            }
        }
        System.out.println();
    }

    public void mostrarFinPartida() {
        System.out.println("Fin de la partida");
    }

    // ===== MÉTODOS DE RESULTADOS =====

    public void mostrarEncabezadoResultados() {
        System.out.println();
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║           FIN DE LA PARTIDA           ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
    }

    public void mostrarTablaResultados(List<Jugador> jugadores, List<Jugador> ganadoresCartas,
                                       List<Jugador> ganadoresOros, List<Jugador> ganadoresSietes,
                                       boolean empateCartas, boolean empateOros, boolean empateSietes) {
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
            if (m.getVelo()) velo += " *";

            System.out.printf(formato, nombre, cartas, oros, sietes, velo, escobas);
            System.out.println();
        }
        System.out.println(separador);
    }

    public void mostrarPuntosFinales(List<Jugador> jugadores, int[] puntos) {
        System.out.println("\nPuntos finales:");
        for (int i = 0; i < jugadores.size(); i++) {
            System.out.println(jugadores.get(i).getNombre() + ": " + puntos[i] + " punto" + (puntos[i] == 1 ? "" : "s"));
        }
    }

    public void mostrarGanador(String nombreGanador, boolean esJugador) {
        System.out.println("\n¡El ganador de la partida es: " + nombreGanador + "!");
        if (esJugador) {
            System.out.println("¡Felicidades, has ganado la partida! 🎉");
        } else {
            System.out.println("¡Ánimo! La próxima vez seguro que ganas tú.");
        }
    }

    public void mostrarEmpate() {
        System.out.println("\nLa partida terminó en empate.");
    }

    public void mostrarAdiós() {
        System.out.println("¡Hasta luego!");
    }

    public void cerrar() {
        scanner.close();
    }
}

