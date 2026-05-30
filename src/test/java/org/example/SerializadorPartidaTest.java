package org.example;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SerializadorPartidaTest {

    @Test
    void roundTripDebePreservarEstadoCompletoDeLaPartida() {
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
        humano.getMonton().sumarEscoba();
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

        assertNotNull(document);
        assertEquals(partida.getIdPartida(), restaurada.getIdPartida());
        assertEquals(1, restaurada.getTurnoActual());
        assertEquals("CPU 1", restaurada.getUltimoQueCapturoNombre());
        assertEquals(15, document.getInteger("objetivoPuntos"));

        assertEquals(2, restaurada.getJugadores().size());

        Participante humanoRestaurado = restaurada.getJugadores().get(0);
        Participante cpuRestaurado = restaurada.getJugadores().get(1);

        assertEquals("Ana", humanoRestaurado.getNombre());
        assertEquals(5, humanoRestaurado.getPuntosTotales());
        assertEquals(2, humanoRestaurado.getMano().size());
        assertCartaEquals(Carta.crearEspanola(1, Palo.OROS), humanoRestaurado.getMano().get(0));
        assertCartaEquals(Carta.crearEspanola(2, Palo.COPAS), humanoRestaurado.getMano().get(1));
        assertEquals(2, humanoRestaurado.getMonton().getCartasCapturadas());
        assertEquals(1, humanoRestaurado.getMonton().getOrosCapturados());
        assertEquals(1, humanoRestaurado.getMonton().getOros());
        assertEquals(1, humanoRestaurado.getMonton().getSietes());
        assertEquals(2, humanoRestaurado.getMonton().getEscobas());
        assertTrue(humanoRestaurado.getMonton().getVelo());

        assertEquals("CPU 1", cpuRestaurado.getNombre());
        assertEquals(3, cpuRestaurado.getPuntosTotales());
        assertEquals(1, cpuRestaurado.getMano().size());
        assertCartaEquals(Carta.crearEspanola(3, Palo.ESPADAS), cpuRestaurado.getMano().get(0));
        assertEquals(2, cpuRestaurado.getMonton().getCartasCapturadas());
        assertEquals(1, cpuRestaurado.getMonton().getOrosCapturados());
        assertEquals(1, cpuRestaurado.getMonton().getOros());
        assertEquals(1, cpuRestaurado.getMonton().getSietes());
        assertEquals(1, cpuRestaurado.getMonton().getEscobas());

        assertEquals(2, restaurada.getMesa().getCartas().size());
        assertCartaEquals(Carta.crearEspanola(4, Palo.BASTOS), restaurada.getMesa().getCartas().get(0));
        assertCartaEquals(Carta.crearEspanola(5, Palo.COPAS), restaurada.getMesa().getCartas().get(1));

        assertEquals(37, restaurada.getBaraja().cartasRestantes());
        assertCartaEquals(Carta.crearEspanola(4, Palo.OROS), restaurada.getBaraja().getCartas().get(0));
        assertCartaEquals(Carta.crearEspanola(10, Palo.BASTOS), restaurada.getBaraja().getCartas().get(restaurada.getBaraja().cartasRestantes() - 1));
    }

    private static void assertCartaEquals(Carta expected, Carta actual) {
        assertEquals(expected.getNumero(), actual.getNumero());
        assertEquals(expected.getPalo(), actual.getPalo());
        assertEquals(expected.getValor(), actual.getValor());
    }
}


