package org.example;

import java.util.List;

public class Maquina extends Participante {
    public Maquina() {
        super("Máquina");
    }

    public List<Carta> elegirCartas(List<List<Carta>> combinaciones) {
        if (combinaciones == null || combinaciones.isEmpty()) {
            return null;
        }
        return combinaciones.get(0);
    }
}

