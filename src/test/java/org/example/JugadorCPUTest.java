package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JugadorCPUTest {

    @Test
    void elegirIndiceCartaDebePriorizarLaCapturaDisponible() {
        JugadorCPU cpu = new JugadorCPU("CPU");
        Partida partida = new Partida(List.of(cpu));

        partida.getMesa().añadirCarta(Carta.crearEspanola(4, Palo.COPAS));
        partida.getMesa().añadirCarta(Carta.crearEspanola(5, Palo.BASTOS));

        cpu.recibirCarta(Carta.crearEspanola(8, Palo.COPAS));
        cpu.recibirCarta(Carta.crearEspanola(7, Palo.OROS));

        int indice = cpu.elegirIndiceCarta(partida, null);

        assertEquals(0, indice);
    }

    @Test
    void elegirIndiceCartaDebePriorizarOrosCuandoNoHayCaptura() {
        JugadorCPU cpu = new JugadorCPU("CPU");
        Partida partida = new Partida(List.of(cpu));

        partida.getMesa().añadirCarta(Carta.crearEspanola(2, Palo.COPAS));
        partida.getMesa().añadirCarta(Carta.crearEspanola(3, Palo.BASTOS));

        cpu.recibirCarta(Carta.crearEspanola(6, Palo.COPAS));
        cpu.recibirCarta(Carta.crearEspanola(7, Palo.OROS));

        int indice = cpu.elegirIndiceCarta(partida, null);

        assertEquals(1, indice);
    }
}

