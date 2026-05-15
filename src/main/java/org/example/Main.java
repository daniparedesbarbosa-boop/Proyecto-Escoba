package org.example;

/**
 * Main: Punto de entrada de la aplicación
 */
public class Main {
    public static void main(String[] args) {
        Vista vista = new Vista();
        Controlador controlador = new Controlador(vista);
        
        controlador.iniciarJuego();
        
        vista.cerrar();
    }
}
