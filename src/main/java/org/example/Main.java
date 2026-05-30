package org.example;

public class Main {
    public static void main(String[] args) {
        // Reducir el ruido de logging del driver de MongoDB (se establece antes de crear el controlador
        // porque este último crea el MongoDBManager que inicializa el cliente).
        // Usamos las propiedades de slf4j-simple para bajar el nivel a WARN.
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "warn");
        System.setProperty("org.slf4j.simpleLogger.log.org.mongodb.driver", "warn");

        Vista vista = new Vista();
        Controlador controlador = new Controlador(vista);

        try {
            controlador.iniciarJuego();
        } catch (ExcepcionPersistenciaHistorial e) {
            System.err.println(formarMensajeError("No se pudo completar la persistencia del historial", e.getMessage()));
        } catch (ExcepcionPartida e) {
            System.err.println(formarMensajeError("Se produjo un error en la partida", e.getMessage()));
        } catch (RuntimeException e) {
            System.err.println(formarMensajeError("Se ha producido un error inesperado durante la ejecución del juego", e.getMessage()));
        } finally {
            vista.cerrar();
        }
    }

    private static String formarMensajeError(String cabecera, String detalle) {
        return String.format("%s: %s", cabecera, detalle);
    }
}
