package org.example;

public class JugadorCPU extends Participante {
    public JugadorCPU(String nombre) {
        super(nombre);
    }

    @Override
    public int elegirIndiceCarta(Partida partida, VistaJuego vista) {
        return partida.elegirMejorCartaCPU(getMano());
    }
}

