package org.example;

public class Carta {
    private final int numero;
    private final Palo palo;
    private final int valor;

    public Carta(int numero, Palo palo, int valor) {
        this.numero = numero;
        if (palo == null) {
            throw new ExcepcionPartida("El palo de la carta no puede ser nulo");
        }
        this.palo = palo;
        this.valor = valor;
    }

    public static Carta crearEspanola(int numero, Palo palo) {
        validarNumero(numero);
        validarPalo(palo);
        return new Carta(numero, palo, valorCarta(numero));
    }

    public static Carta crearEspanola(int numero, char codigoPalo) {
        return crearEspanola(numero, Palo.fromCodigo(codigoPalo));
    }

    private static void validarNumero(int numero) {
        if (numero < 1 || numero > 10) {
            throw new ExcepcionPartida("El número de carta debe estar entre 1 y 10: " + numero);
        }
    }

    private static void validarPalo(Palo palo) {
        if (palo == null) {
            throw new ExcepcionPartida("El palo de la carta no puede ser nulo");
        }
    }

    private static int valorCarta(int numero) {
        return numero <= 7 ? numero : numero - 2;
    }

    public int getNumero() {
        return numero;
    }

    public Palo getPalo() {
        return palo;
    }

    public int getValor() {
        return valor;
    }

    private static String displayNumero(int numero) {
        switch (numero) {
            case 8: return "SOTA";
            case 9: return "CABALLO";
            case 10: return "REY";
            default: return String.valueOf(numero);
        }
    }

    @Override
    public String toString() {
        String nombreCarta = displayNumero(numero);
        StringBuilder sb = new StringBuilder();
        sb.append(nombreCarta).append(" ").append(palo.getEmoji());
        return sb.toString();
    }
}
