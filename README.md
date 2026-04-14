# Juego de La Escoba - Java

Este proyecto consiste en una implementación del juego de cartas español **"La Escoba"** para consola. El sistema permite a un usuario humano enfrentarse a oponentes controlados por una IA simple con prioridades estratégicas.

## 🎮 Funcionamiento del Juego

### Objetivo y Reglas
El objetivo es capturar cartas de la mesa que, sumadas a una carta de la mano del jugador, den un total de **15**. 
* **Suma 15:** Se utilizan los valores reales de las cartas (1 al 7) y valores especiales para figuras (Sota: 8, Caballo: 9, Rey: 10).
* **La Escoba:** Si un jugador logra limpiar todas las cartas de la mesa en su turno, consigue una "Escoba" (+1 punto).
* **Final de Partida:** Las cartas que queden en la mesa al terminar el mazo se asignan al último jugador que haya realizado una captura.

### Puntuación Final
Al terminar la partida, se calculan los puntos según los siguientes criterios:
* **Cartas:** 1 punto al jugador con más cartas (si no hay empate).
* **Oros:** 1 punto al jugador con más oros (si no hay empate).
* **Sietes:** 1 punto al jugador con más sietes (si no hay empate).
* **Velo:** 1 punto directo por poseer el 7 de oros.
* **Escobas:** 1 punto por cada escoba realizada durante el juego.

## 🔄 Esquema de Flujo de Código

1. **Configuración (`Main`)**: El usuario define su nombre y la cantidad de CPUs (1-3).
2. **Preparación**: Se instancia la `Partida`, se baraja el mazo y se reparten las primeras cartas (3 a cada uno y 4 en mesa).
3. **Bucle de Juego (`jugarPartida`)**:
    * Se solicita la entrada del humano o se ejecuta la lógica de la CPU.
    * `Mesa.buscarCombinaciones()` evalúa si la carta jugada permite capturar.
    * Si la mano se vacía y hay cartas en la baraja, se dispara `repartirCartas()`.
4. **Cierre**: Se reparten las cartas sobrantes de la mesa y se ejecuta `calcularPuntos()` para mostrar la tabla de resultados final.

## 📊 Diagrama de Clases (Mermaid)

```mermaid
classDiagram
    class Main {
        +main(String[] args)
    }
    class Partida {
        -List~Jugador~ jugadores
        -Baraja baraja
        -Mesa mesa
        -Jugador ultimoQueCapturo
        +jugarPartida()
        +jugarTurno(int indiceCarta)
        +calcularPuntos()
    }
    class Baraja {
        -List~Carta~ mazo
        +barajar()
        +repartirCarta() Carta
    }
    class Carta {
        -int numero
        -char palo
        -int valor
        +toString() String
    }
    class Mesa {
        -List~Carta~ cartasEnMesa
        +buscarCombinaciones(Carta cartaJugada)
        +retirarCartas(List~Carta~ cartas)
    }
    class Jugador {
        -String nombre
        -List~Carta~ mano
        -MontonJugador monton
        +recibirCarta(Carta c)
        +jugarCarta(int indice) Carta
    }
    class MontonJugador {
        -List~Carta~ cartas
        -int escobas
        +agregarCartas(List~Carta~ nuevas)
        +getVelo() boolean
        +getSietes() int
    }

    Main ..> Partida : inicia
    Partida *-- Baraja
    Partida *-- Mesa
    Partida *-- Jugador
    Baraja o-- Carta
    Mesa o-- Carta
    Jugador *-- MontonJugador
    Jugador o-- Carta
    MontonJugador o-- Carta
