package org.example;

public enum Palo {
    OROS('O', "🪙"),
    COPAS('C', "🍷"),
    ESPADAS('E', "⚔️"),
    BASTOS('B', "🪵");

    private final char codigo;
    private final String emoji;

    Palo(char codigo, String emoji) {
        this.codigo = codigo;
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }

    public static Palo fromCodigo(char codigo) {
        for (Palo palo : values()) {
            if (palo.codigo == codigo) {
                return palo;
            }
        }
        throw new ExcepcionPartida("Palo de carta no válido: " + codigo);
    }
}


