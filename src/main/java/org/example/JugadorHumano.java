package org.example;

public class JugadorHumano extends Jugador {
    public JugadorHumano(String nombre) {
        super(nombre);
    }

    @Override
    public int elegirIndiceCarta(Partida partida, VistaJuego vista) {
        return vista.elegirCartaJugador(getMano());
    }
}

