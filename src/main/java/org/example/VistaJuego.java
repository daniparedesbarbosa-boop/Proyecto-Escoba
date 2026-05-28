package org.example;

import java.util.List;

public interface VistaJuego {
    void mostrarBienvenida();
    String pedirNombre();
    int pedirNumeroRivales();
    int pedirObjetivoPuntos();
    void mostrarConfiguracion(String nombreJugador, List<String> rivales);
    boolean pedirConfirmacionInicio();
    void mostrarCartasEnMesa(List<Carta> cartas);
    void mostrarUltimas();
    void mostrarNuevasCartas();
    void mostrarTurnoJugador(String nombreJugador);
    int elegirCartaJugador(List<Carta> mano);
    void mostrarJugadaJugador(String nombreJugador, Carta carta);
    void mostrarCartasCapturadas(List<Carta> cartas);
    void mostrarEscoba();
    void mostrarCartasCapturadosSinCombinacion();
    void mostrarSinCombinacion();
    void mostrarAviso(String mensaje);
    void mostrarFinPartida();
    void mostrarResultadoRonda(ResultadoRonda resultado);
    void mostrarAdios();
    void cerrar();
}


