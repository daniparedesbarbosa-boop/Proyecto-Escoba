package org.example;

public class ExcepcionPartida extends RuntimeException {
    public ExcepcionPartida(String message) {
        super(message);
    }

    public ExcepcionPartida(String message, Throwable cause) {
        super(message, cause);
    }
}

