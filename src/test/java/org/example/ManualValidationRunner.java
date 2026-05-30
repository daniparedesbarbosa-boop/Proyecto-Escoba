package org.example;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public final class ManualValidationRunner {

    private ManualValidationRunner() {
    }

    public static void main(String[] args) {
        validarCarta();
        validarMontonJugador();
        validarCpu();
        validarSerializacion();
        System.out.println("Validaciones manuales completadas correctamente.");
    }

    private static void validarCarta() {
        Carta carta = Carta.crearEspanola(8, Palo.OROS);
        require(carta.getNumero() == 8, "El número de carta debe ser 8");
        require(carta.getPalo() == Palo.OROS, "El palo debe ser oros");
        require(carta.getValor() == 6, "La sota debe valer 6");
        require("SOTA 🪙".equals(carta.toString()), "La representación en texto debe coincidir");
    }

    private static void validarMontonJugador() {
        MontonJugador monton = new MontonJugador();
        monton.agregarCartas(List.of(
                Carta.crearEspanola(7, Palo.OROS),
                Carta.crearEspanola(3, Palo.COPAS),
                Carta.crearEspanola(2, Palo.OROS)
        ));
        monton.sumarEscoba();

        require(monton.getCartasCapturadas() == 3, "Debe contar 3 cartas capturadas");
        require(monton.getOrosCapturados() == 2, "Debe contar 2 oros capturados");
        require(monton.getOros() == 2, "Debe contar 2 cartas de oros");
        require(monton.getSietes() == 1, "Debe contar un siete");
        require(monton.getVelo(), "Debe activar el velo con el 7 de oros");
        require(monton.getEscobas() == 1, "Debe contar una escoba");

        monton.reiniciar();
        require(monton.getCartas().isEmpty(), "El montón debe reiniciarse vacío");
        require(monton.getCartasCapturadas() == 0, "El contador de cartas debe reiniciarse");
    }

    private static void validarCpu() {
        JugadorCPU cpu = new JugadorCPU("CPU");
        Partida partida = new Partida(List.of(cpu));

        partida.getMesa().añadirCarta(Carta.crearEspanola(4, Palo.COPAS));
        partida.getMesa().añadirCarta(Carta.crearEspanola(5, Palo.BASTOS));

        cpu.recibirCarta(Carta.crearEspanola(8, Palo.COPAS));
        cpu.recibirCarta(Carta.crearEspanola(7, Palo.OROS));

        require(cpu.elegirIndiceCarta(partida, null) == 0, "La CPU debe priorizar la captura disponible");
    }

    private static void validarSerializacion() {
        JugadorHumano humano = new JugadorHumano("Ana");
        JugadorCPU cpu = new JugadorCPU("CPU 1");
        Partida partida = new Partida(new ArrayList<>(List.of(humano, cpu)));

        humano.recibirCarta(Carta.crearEspanola(1, Palo.OROS));
        humano.recibirCarta(Carta.crearEspanola(2, Palo.COPAS));
        cpu.recibirCarta(Carta.crearEspanola(3, Palo.ESPADAS));

        humano.getMonton().agregarCartas(List.of(
                Carta.crearEspanola(7, Palo.OROS),
                Carta.crearEspanola(2, Palo.COPAS)
        ));
        humano.getMonton().setEscobas(2);
        humano.addPuntosTotales(5);

        cpu.getMonton().agregarCartas(List.of(
                Carta.crearEspanola(7, Palo.BASTOS),
                Carta.crearEspanola(4, Palo.OROS)
        ));
        cpu.getMonton().setEscobas(1);
        cpu.addPuntosTotales(3);

        partida.getMesa().añadirCarta(Carta.crearEspanola(4, Palo.BASTOS));
        partida.getMesa().añadirCarta(Carta.crearEspanola(5, Palo.COPAS));
        partida.getBaraja().repartirCarta();
        partida.getBaraja().repartirCarta();
        partida.getBaraja().repartirCarta();
        partida.establecerUltimoCapturador(cpu);
        partida.siguienteTurno();

        SerializadorPartida serializador = new SerializadorPartida();
        Document document = serializador.toDocument(partida, 15);
        Partida restaurada = serializador.fromDocument(document);

        require(restaurada != null, "La partida restaurada no puede ser nula");
        require(partida.getIdPartida().equals(restaurada.getIdPartida()), "El id de partida debe preservarse");
        require(restaurada.getTurnoActual() == 1, "El turno debe preservarse");
        require("CPU 1".equals(restaurada.getUltimoQueCapturoNombre()), "El último capturador debe preservarse");
        require(document.getInteger("objetivoPuntos") == 15, "El objetivo de puntos debe serializarse");
        require(restaurada.getJugadores().size() == 2, "Deben restaurarse los dos jugadores");

        Participante humanoRestaurado = restaurada.getJugadores().get(0);
        Participante cpuRestaurado = restaurada.getJugadores().get(1);

        require("Ana".equals(humanoRestaurado.getNombre()), "El nombre del jugador humano debe preservarse");
        require(humanoRestaurado.getPuntosTotales() == 5, "Los puntos del jugador humano deben preservarse");
        require(humanoRestaurado.getMonton().getVelo(), "El velo del jugador humano debe preservarse");

        require("CPU 1".equals(cpuRestaurado.getNombre()), "El nombre de la CPU debe preservarse");
        require(cpuRestaurado.getPuntosTotales() == 3, "Los puntos de la CPU deben preservarse");
        require(cpuRestaurado.getMonton().getEscobas() == 1, "Las escobas de la CPU deben preservarse");

        require(restaurada.getMesa().getCartas().size() == 2, "La mesa debe restaurarse con dos cartas");
        require(restaurada.getBaraja().cartasRestantes() == 37, "La baraja debe restaurarse con 37 cartas");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}

