package org.example;

public class Main {
    public static void main(String[] args) {
        Vista vista = new Vista();
        Controlador controlador = new Controlador(vista);

        try {
            controlador.iniciarJuego();
        } catch (ExcepcionPersistenciaHistorial e) {
            System.err.println("No se pudo completar la persistencia del historial: " + e.getMessage());
        } catch (ExcepcionPartida e) {
            System.err.println("Se produjo un error en la partida: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Se ha producido un error inesperado durante la ejecución del juego: " + e.getMessage());
        } finally {
            vista.cerrar();
        }
    }
}
