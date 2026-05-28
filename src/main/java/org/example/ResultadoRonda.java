package org.example;

import java.util.List;

public class ResultadoRonda {
    private final List<Participante> jugadores;
    private final int[] puntos;
    private final List<Participante> ganadoresCartas;
    private final List<Participante> ganadoresOros;
    private final List<Participante> ganadoresSietes;
    private final boolean empateCartas;
    private final boolean empateOros;
    private final boolean empateSietes;
    private final String ganadorNombre;
    private final boolean ganadorEsJugador;
    private final boolean empateFinal;

    public ResultadoRonda(List<Participante> jugadores,
                          int[] puntos,
                          List<Participante> ganadoresCartas,
                          List<Participante> ganadoresOros,
                          List<Participante> ganadoresSietes,
                          boolean empateCartas,
                          boolean empateOros,
                          boolean empateSietes,
                          String ganadorNombre,
                          boolean ganadorEsJugador,
                          boolean empateFinal) {
        if (jugadores == null || puntos == null || ganadoresCartas == null || ganadoresOros == null
                || ganadoresSietes == null) {
            throw new ExcepcionPartida("Los datos del resultado de la ronda no pueden ser nulos");
        }

        this.jugadores = List.copyOf(jugadores);
        this.puntos = puntos.clone();
        this.ganadoresCartas = List.copyOf(ganadoresCartas);
        this.ganadoresOros = List.copyOf(ganadoresOros);
        this.ganadoresSietes = List.copyOf(ganadoresSietes);
        this.empateCartas = empateCartas;
        this.empateOros = empateOros;
        this.empateSietes = empateSietes;
        this.ganadorNombre = ganadorNombre;
        this.ganadorEsJugador = ganadorEsJugador;
        this.empateFinal = empateFinal;
    }

    public List<Participante> getJugadores() {
        return jugadores;
    }

    public int[] getPuntos() {
        return puntos.clone();
    }

    public List<Participante> getGanadoresCartas() {
        return ganadoresCartas;
    }

    public List<Participante> getGanadoresOros() {
        return ganadoresOros;
    }

    public List<Participante> getGanadoresSietes() {
        return ganadoresSietes;
    }

    public boolean isEmpateCartas() {
        return empateCartas;
    }

    public boolean isEmpateOros() {
        return empateOros;
    }

    public boolean isEmpateSietes() {
        return empateSietes;
    }

    public String getGanadorNombre() {
        return ganadorNombre;
    }

    public boolean isGanadorEsJugador() {
        return ganadorEsJugador;
    }

    public boolean isEmpateFinal() {
        return empateFinal;
    }
}
