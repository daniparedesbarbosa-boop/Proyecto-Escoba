package org.example;

public class Main {
    public static void main(String[] args) {
        Vista vista = new Vista();
        Controlador controlador = new Controlador(vista);

        try {
            controlador.iniciarJuego();
        } catch (RuntimeException e) {
            System.err.println("Se ha producido un error inesperado durante la ejecución del juego: " + e.getMessage());
        } finally {
            vista.cerrar();
        }
    }
}
