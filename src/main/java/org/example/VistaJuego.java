package org.example;

import org.bson.Document;
import java.util.List;

public interface VistaJuego {
    void mostrarBienvenida();
    boolean pedirCargarPartida();
    String elegirPartidaGuardada(List<Document> partidas);
    void mostrarPartidaCargada(String idPartida);
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
    void mostrarSoloPuntosTotales(List<Participante> jugadores);
    void mostrarPartidaGuardada();
    void mostrarAdios();
    void cerrar();

    /**
     * Pregunta simple que se muestra inmediatamente después de cargar una partida:
     * ¿Continuar? (S/N). Si el usuario responde N se debe terminar la ejecución.
     * @return true si el usuario elige continuar (S), false si elige no (N)
     */
    boolean pedirContinuarDespuesCarga();

    /**
     * Pregunta al usuario si desea guardar la partida entre rondas o continuar jugando.
     * @return true si el usuario elige guardar (G), false si elige continuar (C)
     */
    boolean pedirGuardarEntreRondas();
}
