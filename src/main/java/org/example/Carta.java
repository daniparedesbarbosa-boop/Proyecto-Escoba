package org.example;

public class Carta {
    public static final char OROS = 'O';
    public static final char COPAS = 'C';
    public static final char ESPADAS = 'E';
    public static final char BASTOS = 'B';

    private int numero;
    private char palo;
    private int valor;

    public Carta(int numero, char palo, int valor) {
        this.numero = numero;
        this.palo = palo;
        this.valor = valor;
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

    private String emojiPalo() {
        switch (palo) {
            case OROS: return "\uD83E\uDE99"; // 🪙
            case COPAS: return "\uD83C\uDFC6"; // 🏆
            case ESPADAS: return "\u2694\uFE0F"; // ⚔️
            case BASTOS: return "\uD83E\uDEB5"; // 🪵
            default: return "?";
        }
    }

    @Override
    public String toString() {
        return numero + " " + emojiPalo();
    }
}
