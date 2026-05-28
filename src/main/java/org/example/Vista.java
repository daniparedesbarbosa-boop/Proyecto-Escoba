package org.example;

import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Vista implements VistaJuego {
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

    @Override
    public boolean pedirCargarPartida() {
        System.out.print("Hay partidas guardadas. ¿Quieres cargar una? (S/N): ");
        String respuesta = leerLineaSegura("No se pudo leer la respuesta").trim();
        return respuesta.equalsIgnoreCase("S");
    }

    @Override
    public String elegirPartidaGuardada(List<Document> partidas) {
        if (partidas == null || partidas.isEmpty()) {
            System.out.println("No hay partidas para cargar.");
            return null;
        }

        System.out.println("\n--- Partidas Guardadas ---");
        for (int i = 0; i < partidas.size(); i++) {
            Document doc = partidas.get(i);
            String fechaStr = doc.getString("fechaGuardado");
            LocalDateTime fecha = LocalDateTime.parse(fechaStr);
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

            List<Document> jugadoresDocs = doc.getList("jugadores", Document.class);
            String jugadoresStr = jugadoresDocs.stream()
                .map(j -> String.format("%s (%d pts)", j.getString("nombre"), j.getInteger("puntosTotales")))
                .collect(Collectors.joining(", "));

            System.out.printf("%d. %s - Jugadores: %s%n", i + 1, fecha.format(formatter), jugadoresStr);
        }
        System.out.println("--------------------------");

        while (true) {
            System.out.printf("Elige una partida (1-%d) o 0 para empezar una nueva: ", partidas.size());
            int opcion = leerNumeroValido();
            if (opcion >= 0 && opcion <= partidas.size()) {
                return (opcion == 0) ? null : partidas.get(opcion - 1).getString("idPartida");
            }
            System.out.println("Opción no válida.");
        }
    }

    @Override
    public void mostrarPartidaCargada(String idPartida) {
        System.out.printf("\n¡Partida %s cargada! Continuamos donde la dejamos...%n", idPartida.substring(0, 8));
    }

    public String pedirNombre() {
        System.out.print("Inserta tu nombre: ");
        String nombre = leerLineaSegura("No se pudo leer el nombre del jugador").trim();
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

    public int pedirObjetivoPuntos() {
        while (true) {
            System.out.print("¿A cuántos puntos quieres jugar? (21/31): ");
            int valor = leerNumeroValido();
            if (valor == 21 || valor == 31) {
                return valor;
            }
            System.out.println("Introduce 21 o 31");
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
        } catch (NoSuchElementException | IllegalStateException e) {
            throw new ExcepcionPartida("No se pudo leer una respuesta numérica", e);
        }
    }

    private String leerLineaSegura(String mensajeError) {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException | IllegalStateException e) {
            throw new ExcepcionPartida(mensajeError, e);
        }
    }

    public void mostrarConfiguracion(String nombreJugador, List<String> rivales) {
        System.out.println();
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║          ¡EMPIEZA LA PARTIDA!         ║");
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
        String respuesta = leerLineaSegura("No se pudo leer la confirmación de inicio").trim();
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
            } else if (opcion == -2) { // Código especial para guardar
                return -2;
            }
        }
    }

    private int pedirIndiceCartaValido(int max) {
        System.out.printf("Elige una carta (1-%d) o escribe 'S' para guardar y salir: ", max);
        String entrada = leerLineaSegura("No se pudo leer la entrada").trim();

        if (entrada.equalsIgnoreCase("S")) {
            return -2; // Devolvemos un código especial para guardar
        }

        try {
            int opcion = Integer.parseInt(entrada);
            if (opcion < 1 || opcion > max) {
                System.out.printf("Introduce un número entre 1 y %d%n", max);
                return -1;
            }
            return opcion;
        } catch (NumberFormatException e) {
            System.out.println("Entrada no válida. Introduce un número o 'S'.");
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

    public void mostrarAviso(String mensaje) {
        System.out.println(construirMensaje("Aviso", mensaje));
    }

    public void mostrarError(String mensaje) {
        System.err.println(construirMensaje("Error", mensaje));
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
        System.out.println("Fin de la ronda");
    }

    public void mostrarResultadoRonda(ResultadoRonda resultado) {
        if (resultado == null) {
            throw new ExcepcionPartida("El resultado de la ronda no puede ser nulo");
        }

        mostrarEncabezadoResultados();
        mostrarTablaResultados(resultado.getJugadores(), resultado.getGanadoresCartas(),
                resultado.getGanadoresOros(), resultado.getGanadoresSietes(), resultado.isEmpateCartas(),
                resultado.isEmpateOros(), resultado.isEmpateSietes());
        mostrarPuntosFinales(resultado.getJugadores(), resultado.getPuntos());

        if (resultado.isEmpateFinal()) {
            mostrarEmpate();
        } else {
            mostrarGanador(resultado.getGanadorNombre(), resultado.isGanadorEsJugador());
        }
    }

    // ===== MÉTODOS DE RESULTADOS =====

    public void mostrarEncabezadoResultados() {
        System.out.println();
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║            FIN DE LA RONDA            ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
    }

    public void mostrarTablaResultados(List<Participante> jugadores, List<Participante> ganadoresCartas,
                                       List<Participante> ganadoresOros, List<Participante> ganadoresSietes,
                                       boolean empateCartas, boolean empateOros, boolean empateSietes) {
        System.out.println("\nPuntos:");
        // Añadimos columna Total para mostrar puntos acumulados
        String formato = "| %-12s | %-6s | %-6s | %-6s | %-4s | %-7s | %-5s |";
        String separador = "+--------------+--------+--------+--------+------+---------+-------+";

        imprimirEncabezadoTabla(separador, formato);

        for (Participante j : jugadores) {
            imprimirFilaJugador(j, formato, ganadoresCartas, ganadoresOros, ganadoresSietes,
                               empateCartas, empateOros, empateSietes);
        }

        System.out.println(separador);
    }

    private void imprimirEncabezadoTabla(String separador, String formato) {
        System.out.println(separador);
        System.out.printf(formato, "Jugador", "Cartas", "Oros", "Sietes", "Velo", "Escobas", "Total");
        System.out.println();
        System.out.println(separador);
    }

    private void imprimirFilaJugador(Participante j, String formato, List<Participante> ganadoresCartas,
                                     List<Participante> ganadoresOros, List<Participante> ganadoresSietes,
                                     boolean empateCartas, boolean empateOros, boolean empateSietes) {
        MontonJugador m = j.getMonton();
        String nombre = j.getNombre();
        String cartas = formatearCeldaGanador(m.getCartasCapturadas(), ganadoresCartas.contains(j), empateCartas);
        String oros = formatearCeldaGanador(m.getOrosCapturados(), ganadoresOros.contains(j), empateOros);
        String sietes = formatearCeldaGanador(m.getSietes(), ganadoresSietes.contains(j), empateSietes);
        String velo = m.getVelo() ? "Sí *" : "No";
        String escobas = String.valueOf(m.getEscobas());
        String total = String.valueOf(j.getPuntosTotales());

        System.out.printf(formato, nombre, cartas, oros, sietes, velo, escobas, total);
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

    public void mostrarPuntosFinales(List<Participante> jugadores, int[] puntos) {
        System.out.println("\nPuntos de la ronda:");
        for (int i = 0; i < jugadores.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(jugadores.get(i).getNombre()).append(": ").append(puntos[i])
              .append(" punto").append(puntos[i] == 1 ? "" : "s");
            System.out.println(sb.toString());
        }

        // Mostrar puntos totales acumulados hasta ahora (sumatoria de rondas anteriores + ronda actual)
        System.out.println("\nPuntos totales:");
        for (Participante j : jugadores) {
            StringBuilder sb = new StringBuilder();
            sb.append(j.getNombre()).append(": ").append(j.getPuntosTotales())
               .append(" punto").append(j.getPuntosTotales() == 1 ? "" : "s");
            System.out.println(sb.toString());
        }
    }

    public void mostrarGanador(String nombreGanador, boolean esJugador) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n¡El ganador de la ronda es: ").append(nombreGanador).append("!");
        System.out.println(sb.toString());
        if (esJugador) {
            System.out.println("¡Felicidades, has ganado esta ronda! 🎉");
        } else {
            System.out.println("¡Ánimo! La próxima vez seguro que ganas tú.");
        }
    }

    public void mostrarEmpate() {
        System.out.println("\nLa ronda terminó en empate.");
    }

    @Override
    public void mostrarPartidaGuardada() {
        System.out.println("\nPartida guardada correctamente. ¡Hasta la próxima!");
    }

    public void mostrarAdios() {
        System.out.println("¡Hasta luego!");
    }

    private String construirMensaje(String etiqueta, String mensaje) {
        StringBuilder sb = new StringBuilder();
        sb.append(etiqueta).append(": ").append(mensaje);
        return sb.toString();
    }

    public void cerrar() {
        scanner.close();
    }
}
