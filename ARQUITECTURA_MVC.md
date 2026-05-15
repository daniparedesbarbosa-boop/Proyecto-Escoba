```
ESTRUCTURA MVC - PROYECTO ESCOBA
═════════════════════════════════════════════════════════════════

┌─────────────────────────────────────────────────────────────────┐
│                          APPLICATION                            │
│                           (Main.java)                           │
│          Punto de entrada - Constructor de Vista/Ctrl            │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │
                    ┌─────────┴─────────┐
                    │                   │
                    ▼                   ▼
      ┌──────────────────────┐  ┌──────────────────────┐
      │     VISTA (V)        │  │   CONTROLADOR (C)    │
      │   (Vista.java)       │  │ (Controlador.java)   │
      └──────────────────────┘  └──────────────────────┘
      │                         │
      │ Responsabilidades:     │ Responsabilidades:
      │                         │
      ├─ Presentación          ├─ Orquestación
      ├─ Captura de entrada   ├─ Flujo del juego
      ├─ Salida en pantalla   ├─ Lógica de turnos
      ├─ Formato de datos     ├─ Cálculo de puntos
      ├─ Mensajes            ├─ Interacción V↔M
      └─ Tablas/UI            └─ Coordinación general
            │                          │
            │◄──────────────────────────┤
            │      comunica a través   │
            │    de toString() de M    │
            │                          │
            └──────────────┬───────────┘
                           │
                    ┌──────├──────────┐
                    │      │          │
                    ▼      ▼          ▼
         ┌───────────────────────────────────┐
         │       MODELO (M)                  │
         │    Clases de Negocio              │
         └───────────────────────────────────┘
         │
         ├─ Partida.java ────────────────┐
         │  └─ Lógica del juego          │
         │  └─ Gestión de turnos         │
         │  └─ Cálculo de puntos        │
         │  └─ Selección de cartas       │
         │                               │
         ├─ Baraja.java ─────────────────┤
         │  └─ Mazo de cartas            │
         │  └─ Barajar/Repartir          │
         │                               │
         ├─ Carta.java ──────────────────┤
         │  └─ Datos: número, palo       │
         │  └─ Valor y formato emoji     │
         │                               │
         ├─ Mesa.java ───────────────────┤
         │  └─ Cartas en la mesa         │
         │  └─ Búsqueda de combos        │
         │                               │
         ├─ Jugador.java ────────────────┤
         │  └─ Nombre y mano             │
         │  └─ Montón capturado          │
         │                               │
         └─ MontonJugador.java ──────────┤
            └─ Cartas capturadas
            └─ Contadores (oros, sietes)

═════════════════════════════════════════════════════════════════

FLUJO DE DATOS:
═════════════════════════════════════════════════════════════════

1. Main crea Vista y Controlador
                  │
2. Controlador.iniciarJuego()
   ├─ Solicita datos mediante Vista
   ├─ Crea Partida (Modelo)
   ├─ Inicia el juego
   │
3. Mientras la partida no termine:
   ├─ Mostrar estado (Vista)
   ├─ Pedir entrada (Vista)
   ├─ Actualizar modelo (Controlador)
   ├─ Mostrar cambios (Vista)
   │
4. Mostrar resultados (Vista)
   ├─ Calcular puntos (Partida)
   ├─ Mostrar tabla (Vista)
   └─ Mostrar ganador (Vista)

═════════════════════════════════════════════════════════════════

VENTAJAS DE ESTA ESTRUCTURA:
═════════════════════════════════════════════════════════════════

✓ Separación de responsabilidades
  - Vista: interfaz de usuario
  - Modelo: lógica de negocio
  - Controlador: orquestación

✓ Mantenibilidad: cambios en UI no afectan lógica

✓ Testabilidad: se puede probar cada capa por separado

✓ Escalabilidad: fácil agregar nuevas vistas (GUI, web, API)

✓ Reutilización: modelo puede usarse en distintos contextos

═════════════════════════════════════════════════════════════════
```

