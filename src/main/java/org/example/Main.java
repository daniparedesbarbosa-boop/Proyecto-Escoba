package org.example;

public class Main {
    public static void main(String[] args) {
        Vista vista = new Vista();
        Controlador controlador = new Controlador(vista);
        
        controlador.iniciarJuego();
        
        vista.cerrar();
    }
}
