package org.example;

public class ExcepcionPersistenciaHistorial extends RuntimeException {
    public ExcepcionPersistenciaHistorial(String message) {
        super(message);
    }

    public ExcepcionPersistenciaHistorial(String message, Throwable cause) {
        super(message, cause);
    }
}

