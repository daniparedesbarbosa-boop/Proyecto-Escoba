package org.example;

public class Carta {
    public static final char OROS = 'O';
    public static final char COPAS = 'C';
    public static final char ESPADAS = 'E';
    public static final char BASTOS = 'B';

    private final int numero;
    private final char palo;
    private final int valor;

    public Carta(int numero, char palo, int valor) {
        this.numero = numero;
        this.palo = palo;
        this.valor = valor;
    }

    public static Carta crearEspanola(int numero, char palo) {
        validarNumero(numero);
        validarPalo(palo);
        return new Carta(numero, palo, valorCarta(numero));
    }

    private static void validarNumero(int numero) {
        if (numero < 1 || numero > 10) {
            throw new ExcepcionPartida("El número de carta debe estar entre 1 y 10: " + numero);
        }
    }

    private static void validarPalo(char palo) {
        if (palo != OROS && palo != COPAS && palo != ESPADAS && palo != BASTOS) {
            throw new ExcepcionPartida("Palo de carta no válido: " + palo);
        }
    }

    private static int valorCarta(int numero) {
        return numero <= 7 ? numero : numero - 2;
    }

    public int getNumero() {
        return numero;
    }

    public char getPalo() {
        return palo;
    }

    public int getValor() {
        return valor;
    }

    private static String emojiPalo(char palo) {
        switch (palo) {
            case OROS: return "\uD83E\uDE99"; // 🪙
            case COPAS: return "\uD83C\uDF77"; // 🍷
            case ESPADAS: return "\u2694\uFE0F"; // ⚔️
            case BASTOS: return "\uD83E\uDEB5"; // 🪵
            default: return "?";
        }
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
        sb.append(nombreCarta).append(" ").append(emojiPalo(palo));
        return sb.toString();
    }
}
