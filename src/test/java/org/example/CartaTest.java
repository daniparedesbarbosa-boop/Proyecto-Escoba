package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartaTest {

    @Test
    void crearEspanolaDebeAsignarValorCorrectoYRepresentacion() {
        Carta carta = Carta.crearEspanola(8, Palo.OROS);

        assertEquals(8, carta.getNumero());
        assertEquals(Palo.OROS, carta.getPalo());
        assertEquals(6, carta.getValor());
        assertEquals("SOTA 🪙", carta.toString());
    }

    @Test
    void crearEspanolaDebeRechazarNumerosFueraDeRango() {
        assertThrows(ExcepcionPartida.class, () -> Carta.crearEspanola(0, Palo.COPAS));
        assertThrows(ExcepcionPartida.class, () -> Carta.crearEspanola(11, Palo.COPAS));
    }
}

