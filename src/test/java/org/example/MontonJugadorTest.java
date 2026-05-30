package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MontonJugadorTest {

    @Test
    void agregarCartasDebeActualizarContadoresYVelo() {
        MontonJugador monton = new MontonJugador();
        List<Carta> capturas = List.of(
                Carta.crearEspanola(7, Palo.OROS),
                Carta.crearEspanola(3, Palo.COPAS),
                Carta.crearEspanola(2, Palo.OROS)
        );

        monton.agregarCartas(capturas);
        monton.sumarEscoba();

        assertEquals(3, monton.getCartasCapturadas());
        assertEquals(2, monton.getOrosCapturados());
        assertEquals(2, monton.getOros());
        assertEquals(1, monton.getSietes());
        assertTrue(monton.getVelo());
        assertEquals(1, monton.getEscobas());
        assertEquals(3, monton.getCartas().size());
    }

    @Test
    void reiniciarDebeDejarElMontonVacioYSinContadores() {
        MontonJugador monton = new MontonJugador();
        monton.agregarCartas(List.of(Carta.crearEspanola(7, Palo.OROS)));
        monton.sumarEscoba();

        monton.reiniciar();

        assertTrue(monton.getCartas().isEmpty());
        assertEquals(0, monton.getCartasCapturadas());
        assertEquals(0, monton.getOrosCapturados());
        assertEquals(0, monton.getEscobas());
        assertEquals(0, monton.getOros());
        assertEquals(0, monton.getSietes());
        assertFalse(monton.getVelo());
    }
}

