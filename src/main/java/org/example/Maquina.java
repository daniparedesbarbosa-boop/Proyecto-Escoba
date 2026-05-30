package org.example;

import java.util.List;

@Deprecated
public class Maquina extends JugadorCPU {
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

