package org.example;

import java.util.List;
import java.util.InputMismatchException;
import java.util.Scanner;

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
        while (true) {
            System.out.print("¿Contra cuántos quieres jugar? (1-3): ");
            int numCPUs = leerNumeroValido();
            if (numCPUs >= 1 && numCPUs <= 3) {
                return numCPUs;
            }
            System.out.println("Introduce un número entre 1 y 3");
        }
    }

    private int leerNumeroValido() {
        try {
            int numero = scanner.nextInt();
            scanner.nextLine();
            return numero;
        } catch (InputMismatchException e) {
            System.out.println("Introduce un número válido");
            scanner.nextLine();
            return -1;
        } catch (IllegalStateException e) {
            System.out.println("No se pudo leer la entrada");
            return -1;
        }
    }

    public void mostrarConfiguracion(String nombreJugador, List<String> rivales) {
        System.out.println();
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║           EMPIEZA LA PARTIDA          ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
        StringBuilder sb = new StringBuilder();
        sb.append("Jugador: ").append(nombreJugador);
        System.out.println(sb.toString());
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
        StringBuilder sb = new StringBuilder();
        sb.append("\nTurno de: ").append(nombreJugador);
        System.out.println(sb.toString());
    }

    public void mostrarCartasJugador(List<Carta> mano) {
        System.out.println("\nTu mano:");
        for (int i = 0; i < mano.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(i + 1).append(" - ").append(mano.get(i));
            System.out.println(sb.toString());
        }
    }

    public int elegirCartaJugador(List<Carta> mano) {
        while (true) {
            mostrarCartasJugador(mano);
            int opcion = pedirIndiceCartaValido(mano.size());
            if (opcion > 0) {
                return opcion - 1;
            }
        }
    }

    private int pedirIndiceCartaValido(int max) {
        StringBuilder sb = new StringBuilder();
        sb.append("Elige una carta (1-").append(max).append("): ");
        System.out.print(sb.toString());
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine();
            if (opcion < 1 || opcion > max) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Introduce un número entre 1 y ").append(max);
                System.out.println(sb2.toString());
                return -1;
            }
            return opcion;
        } catch (InputMismatchException e) {
            System.out.println("Introduce un número válido");
            scanner.nextLine();
            return -1;
        } catch (IllegalStateException e) {
            System.out.println("No se pudo leer la entrada");
            return -1;
        }
    }

    public void mostrarJugadaJugador(String nombreJugador, Carta carta) {
        StringBuilder sb = new StringBuilder();
        sb.append(nombreJugador).append(" juega: ").append(carta);
        System.out.println(sb.toString());
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
        StringBuilder sb = new StringBuilder();
        sb.append("Cartas finales en la mesa asignadas a ").append(nombreJugador).append(": ");
        for (int i = 0; i < cartas.size(); i++) {
            sb.append(cartas.get(i));
            if (i < cartas.size() - 1) {
                sb.append(" | ");
            }
        }
        System.out.println(sb.toString());
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

        imprimirEncabezadoTabla(separador, formato);

        for (Jugador j : jugadores) {
            imprimirFilaJugador(j, formato, ganadoresCartas, ganadoresOros, ganadoresSietes,
                               empateCartas, empateOros, empateSietes);
        }

        System.out.println(separador);
    }

    private void imprimirEncabezadoTabla(String separador, String formato) {
        System.out.println(separador);
        System.out.printf(formato, "Jugador", "Cartas", "Oros", "Sietes", "Velo", "Escobas");
        System.out.println();
        System.out.println(separador);
    }

    private void imprimirFilaJugador(Jugador j, String formato, List<Jugador> ganadoresCartas,
                                     List<Jugador> ganadoresOros, List<Jugador> ganadoresSietes,
                                     boolean empateCartas, boolean empateOros, boolean empateSietes) {
        MontonJugador m = j.getMonton();
        String nombre = j.getNombre();
        String cartas = formatearCeldaGanador(m.getCartasCapturadas(), ganadoresCartas.contains(j), empateCartas);
        String oros = formatearCeldaGanador(m.getOrosCapturados(), ganadoresOros.contains(j), empateOros);
        String sietes = formatearCeldaGanador(m.getSietes(), ganadoresSietes.contains(j), empateSietes);
        String velo = m.getVelo() ? "Sí *" : "No";
        String escobas = String.valueOf(m.getEscobas());

        System.out.printf(formato, nombre, cartas, oros, sietes, velo, escobas);
        System.out.println();
    }

    private String formatearCeldaGanador(int valor, boolean esGanador, boolean hayEmpate) {
        StringBuilder sb = new StringBuilder();
        sb.append(valor);
        if (!hayEmpate && esGanador) {
            sb.append(" *");
        }
        return sb.toString();
    }

    public void mostrarPuntosFinales(List<Jugador> jugadores, int[] puntos) {
        System.out.println("\nPuntos finales:");
        for (int i = 0; i < jugadores.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(jugadores.get(i).getNombre()).append(": ").append(puntos[i])
              .append(" punto").append(puntos[i] == 1 ? "" : "s");
            System.out.println(sb.toString());
        }
    }

    public void mostrarGanador(String nombreGanador, boolean esJugador) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n¡El ganador de la partida es: ").append(nombreGanador).append("!");
        System.out.println(sb.toString());
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

