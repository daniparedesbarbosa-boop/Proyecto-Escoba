package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║        ¡BIENVENIDO A LA ESCOBA!        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserta tu nombre: ");
        String nombre = scanner.next().trim();

        if (nombre.isEmpty()) {
            nombre = "Jugador";
        }

        int numCPUs = 0;
        while (numCPUs < 1 || numCPUs > 3) {
            System.out.println("¿Contra cuántos quieres jugar? (1-3): ");
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

        List<String> nombresJugadores = new ArrayList<>();
        nombresJugadores.add(nombre);

        String[] nombresIA = {"CPU1", "CPU2", "CPU3"};
        for (int i = 0; i < numCPUs; i++) {
            nombresJugadores.add(nombresIA[i]);
        }

        System.out.println();
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║              LA PARTIDA               ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
        System.out.println("Jugador: " + nombre);
        System.out.print("Rivales: ");
        for (int i = 0; i < numCPUs; i++) {
            System.out.print(nombresIA[i]);
            if (i < numCPUs - 1) System.out.print(", ");
        }
        System.out.println();
        System.out.println();

        Partida partida = new Partida(nombresJugadores);

        partida.getBaraja().barajar();

        partida.repartirCartas();

        for (int i = 0; i < 4; i++) {
            Carta carta = partida.getBaraja().repartirCarta();
            if (carta != null) {
                partida.getMesa().añadirCarta(carta);
            }
        }

        System.out.println("¿Comenzamos? (S/N)");
        String respuesta = scanner.next().trim();
        if (respuesta.equalsIgnoreCase("S")) {
            partida.jugarPartida();
        } else {
            System.out.println("¡Hasta luego!");
            return;
        }

        System.out.println();
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║           FIN DE LA PARTIDA           ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();

        partida.mostrarResultados();
        System.out.println();
        partida.calcularPuntos();
    }
}
