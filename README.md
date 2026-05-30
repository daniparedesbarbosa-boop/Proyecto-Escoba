# Juego de La Escoba - Java

![Java](https://img.shields.io/badge/java-21-orange?logo=java)
![Maven](https://img.shields.io/badge/maven-3.8.1-C71A36?logo=maven)
![MongoDB](https://img.shields.io/badge/MongoDB-5.1.2-13AA52?logo=mongodb)
![License](https://img.shields.io/badge/license-MIT-green)

📦 **Repositorio:** [https://github.com/daniparedesbarbosa-boop/Proyecto-Escoba.git](https://github.com/daniparedesbarbosa-boop/Proyecto-Escoba.git)

Este proyecto consiste en una implementación del juego de cartas español **"La Escoba"** para consola. El sistema permite a un usuario humano enfrentarse a oponentes controlados por una IA simple con prioridades estratégicas. Incluye persistencia en **MongoDB Atlas**, capacidad de guardar/cargar partidas y un framework de pruebas unitarias.

---

## 🚀 Inicio Rápido

**Ejecutar el juego:**

```bash
mvn clean compile exec:java -Dexec.mainClass=org.example.Main
```

O manualmente (PowerShell):

```powershell
javac -d target\classes src\main\java\org\example\*.java
java -cp target\classes org.example.Main
```

---

## 📄 Ficheros de Datos Empleados

El programa utiliza los siguientes ficheros y servicios:

- **`.env`** (Raíz del proyecto)
  - Variables de entorno para conexión a MongoDB
  - Contiene: `MONGO_URI` y `MONGO_DATABASE`
  - Ejemplo: `MONGO_URI="mongodb+srv://usuario:pass@cluster.mongodb.net/?appName=Cluster0"`

- **`historial_partidas.csv`** (Raíz del proyecto)
  - Registro histórico de partidas guardadas (opcional)
  - Se genera automáticamente al completar partidas
  - Formato: `fecha,jugadores,puntos finales`

- **`MongoDB Atlas (Cloud)**
  - Base de datos en la nube para persistencia
  - Colección: `partidas_guardadas`
  - Contiene documentos BSON con el estado completo de cada partida guardada

---

## 🛠️ Configuración de Base de Datos

### Opción 1: Con MongoDB Atlas (Recomendado)

1. Accede a [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Crea una cuenta gratuita y un cluster
3. Crea un usuario de base de datos
4. Copia la URI de conexión (incluir usuario:contraseña)
5. Permite acceso desde tu IP en "Network Access"
6. Crea el archivo `.env` con:
   ```env
   MONGO_URI="mongodb+srv://usuario:contraseña@cluster0.xxxxx.mongodb.net/?appName=Cluster0"
   MONGO_DATABASE="escoba"
   ```

### Opción 2: Con MongoDB Local

1. Instala MongoDB Community en tu máquina
2. Inicia el servicio MongoDB (`mongod`)
3. Crea el archivo `.env` con:
   ```env
   MONGO_URI="mongodb://localhost:27017"
   MONGO_DATABASE="escoba"
   ```

### Opción 3: Sin Base de Datos (Offline)

- No crees el archivo `.env`
- El juego funcionará sin persistencia
- Se guardarán registros en `historial_partidas.csv`

---

## 📋 Tabla de Contenidos

- [Inicio Rápido](#-inicio-rápido)
- [Ficheros de Datos](#-ficheros-de-datos-empleados)
- [Configuración de Base de Datos](#-configuración-de-base-de-datos)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Cómo Ejecutar](#cómo-ejecutar)
- [Funcionamiento del Juego](#-funcionamiento-del-juego)
- [Arquitectura](#arquitectura)
- [Persistencia en MongoDB](#persistencia-en-mongodb)
- [Testing](#testing)
- [Estructura del Proyecto](#estructura-del-proyecto)

---

## 📦 Requisitos Previos

- **Java 21** o superior (compilado para Java 21)
- **Maven 3.8.1** o superior
- **MongoDB Atlas** (opcional, pero recomendado para persistencia)
- Cuenta en [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) (gratuita)

---

## 🛠️ Instalación

### 1. Clonar o Descargar el Proyecto

```bash
cd C:\Users\Daniela\Documents\Clase\Programacion\PROYECTO\ JAVA\Proyecto-Escoba
```

### 2. Compilar el Proyecto

Con Maven:

```bash
mvn clean compile
```

O manualmente con javac (si prefieres evitar Maven):

```powershell
javac -d target\classes -encoding UTF-8 src\main\java\org\example\*.java
```

### 3. Instalar Dependencias

Las dependencias se descargan automáticamente con Maven. Si necesitas hacerlo manualmente:

```bash
mvn dependency:resolve
```

---

## ⚙️ Configuración

### Archivo `.env` (Mongodb Atlas)

Este proyecto requiere un archivo `.env` en la raíz del proyecto para conectar con MongoDB Atlas. Si no tienes MongoDB, el juego seguirá funcionando sin persistencia.

#### Crear el archivo `.env`:

```env
MONGO_URI="mongodb+srv://usuario:contraseña@cluster0.xxxxx.mongodb.net/?appName=Cluster0"
MONGO_DATABASE="escoba"
```

#### Obtener tus credenciales de MongoDB Atlas:

1. Accede a [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Crea un cluster (nivel free está disponible)
3. Crea un usuario de base de datos
4. Copia la URI de conexión (incluirá tu usuario y contraseña)
5. Permite el acceso desde tu IP en "Network Access"

#### Script de Configuración Automática (PowerShell):

Si quieres automatizar la creación del `.env`:

```powershell
.\setup-mongo-env.ps1
```

Este script te pedirá:
- Usuario de MongoDB
- Contraseña (se encodificará automáticamente)
- Cluster URI
- Nombre de la base de datos

---

## ▶️ Cómo Ejecutar

### Opción 1: Con Maven

```bash
mvn clean compile exec:java -Dexec.mainClass=org.example.Main
```

### Opción 2: Compilar y Ejecutar con Java

```powershell
mvn clean compile
java -cp "target\classes;%USERPROFILE%\.m2\repository\org\mongodb\mongodb-driver-sync\5.1.2\mongodb-driver-sync-5.1.2.jar;%USERPROFILE%\.m2\repository\io\github\cdimascio\dotenv-java\3.0.0\dotenv-java-3.0.0.jar;%USERPROFILE%\.m2\repository\org\slf4j\slf4j-simple\2.0.7\slf4j-simple-2.0.7.jar" org.example.Main
```

### Opción 3: Desde el IDE (IntelliJ IDEA)

1. Abre el proyecto en IntelliJ IDEA
2. Click derecho en `Main.java` → "Run 'Main.main()'"

---

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

```

---

## 🏗️ Arquitectura

Este proyecto implementa el patrón **Modelo-Vista-Controlador (MVC)**:

### Componentes MVC

- **Modelo (`Partida`, `Jugador`, `Mesa`, `Baraja`, `Carta`)**
  - Implementan la lógica del juego
  - Manejan el estado de la partida
  - Independientes de la presentación

- **Vista (`VistaJuego` - interfaz, `Vista` - implementación)**
  - Interfaz de consola para el usuario
  - Muestra tableros, puntos, mensajes
  - Captura entrada del usuario

- **Controlador (`Controlador`)**
  - Orquesta la interacción entre Vista y Modelo
  - Gestiona el flujo del juego
  - Coordina con `MongoDBManager` para persistencia

### Componentes Adicionales

- **`MongoDBManager`**: Gestiona la conexión y operaciones CRUD con MongoDB
- **`SerializadorPartida`**: Convierte objetos `Partida` a/desde documentos BSON
- **`Participante`** (clase abstracta): Base para `JugadorHumano` y `JugadorCPU`
- **`JugadorCPU`**: IA con estrategia de selección de cartas
- **Excepciones personalizadas**:
  - `ExcepcionPartida`: Errores en la lógica del juego
  - `ExcepcionPersistenciaHistorial`: Errores de persistencia en DB

---

## 💾 Persistencia en MongoDB

### Características

- ✅ **Guardar partidas en progreso**: Entre rondas puedes guardar tu estado actual
- ✅ **Cargar partidas guardadas**: Al iniciar elige una partida guardada para continuar
- ✅ **Sobrescritura de slots**: Al guardar una partida cargada, actualiza el mismo slot (no crea duplicados)
- ✅ **Datos almacenados**: ID de partida, jugadores con puntos totales, baraja, mesa
- ✅ **Fallback sin DB**: Si MongoDB no está disponible, el juego continúa sin persistencia

### Cómo Guardar/Cargar

**Al iniciar el juego:**

```
--- Partidas Guardadas ---
1. 30/5/26, 14:34 - Jugadores: Dani (0 pts), CPU1 (2 pts), CPU2 (1 pts)
2. 30/5/26, 14:43 - Jugadores: Pepe (1 pts), CPU1 (5 pts)

Elige una partida (1-2) o 0 para empezar una nueva: 1
```

**Al terminar una ronda:**

```
¿Quieres continuar la partida o prefieres guardar?: Continuar (C), Guardar (G): G
```

### Estructura de Documento Guardado

```json
{
  "_id": ObjectId("..."),
  "idPartida": "1cbf9361-abc1-...",
  "fechaGuardado": ISODate("2026-05-30T14:34:00Z"),
  "jugadores": [
    {
      "_class": "JugadorHumano",
      "nombre": "Pepe",
      "puntosTotales": 1
    },
    {
      "_class": "JugadorCPU",
      "nombre": "CPU1",
      "puntosTotales": 5
    }
  ],
  "objetivoPuntos": 21,
  "baraja": { "cartas": [...] },
  "mesa": { "cartas": [...] }
}
```

---

## 🧪 Testing

### Ejecutar Todos los Tests

Con Maven:

```bash
mvn test
```

Manualmente con Java:

```powershell
javac -d target\test-classes -cp "target\classes;jars..." src\test\java\org\example\*.java
java -cp "target\classes;target\test-classes;jars..." org.junit.platform.console.ConsoleLauncher --scan-classpath
```

### Tests Disponibles

- **`CartaTest`**: Validación de creación y valor de cartas
- **`MontonJugadorTest`**: Cálculo correcto de puntos (cartas, oros, sietes, velo, escobas)
- **`JugadorCPUTest`**: Estrategia de selección de cartas de la IA
- **`SerializadorPartidaTest`**: Conversión correcta Partida ↔ Document (MongoDB)
- **`ManualValidationRunner`**: Ejecutable sin Maven para validar flujos completos

### Ejecutar Manual Runner

```powershell
javac -d target\manual-classes -cp "jars..." src\test\java\org\example\ManualValidationRunner.java
java -cp "target\manual-classes;jars..." org.example.ManualValidationRunner
```

---

## 📁 Estructura del Proyecto

```
Proyecto-Escoba/
├── README.md                          # Este archivo
├── ARQUITECTURA_MVC.md                # Documentación detallada de arquitectura
├── EVALUACION_MVC.md                  # Evaluación del cumplimiento MVC
├── pom.xml                            # Configuración de Maven y dependencias
├── .env                               # Variables de entorno (MongoDB)
├── setup-mongo-env.ps1                # Script para configurar .env (PowerShell)
├── historial_partidas.csv             # Histórico de partidas (generado)
│
├── src/
│   ├── main/
│   │   └── java/org/example/
│   │       ├── Main.java              # Punto de entrada de la aplicación
│   │       ├── Controlador.java       # Controlador (C del MVC)
│   │       ├── Vista.java             # Vista (V del MVC)
│   │       ├── VistaJuego.java        # Interfaz de Vista
│   │       ├── Partida.java           # Modelo principal de partida
│   │       ├── Participante.java      # Interfaz de jugador (abstracta)
│   │       ├── JugadorHumano.java     # Jugador humano (subclase)
│   │       ├── JugadorCPU.java        # Jugador IA (subclase)
│   │       ├── Maquina.java           # (Deprecated) Wrapper para compatibilidad
│   │       ├── Mesa.java              # Lógica de mesa
│   │       ├── Baraja.java            # Lógica de baraja
│   │       ├── Carta.java             # Modelo de carta
│   │       ├── Palo.java              # Enum de palos (OROS, COPAS, ESPADAS, BASTOS)
│   │       ├── MontonJugador.java     # Cartas capturadas por jugador
│   │       ├── ResultadoRonda.java    # Resultado de cada ronda
│   │       │
│   │       ├── MongoDB/Persistencia/
│   │       ├── MongoDBManager.java    # Gestor de conexión y CRUD
│   │       ├── SerializadorPartida.java # Conversión Partida ↔ Document
│   │       │
│   │       ├── GestorArchivos.java    # Gestión de archivos (historial CSV)
│   │       │
│   │       └── Excepciones/
│   │           ├── ExcepcionPartida.java
│   │           └── ExcepcionPersistenciaHistorial.java
│   │
│   └── test/
│       └── java/org/example/
│           ├── CartaTest.java
│           ├── MontonJugadorTest.java
│           ├── JugadorCPUTest.java
│           ├── SerializadorPartidaTest.java
│           └── ManualValidationRunner.java
│
└── target/                            # Compilados (generado)
    ├── classes/                       # Clases compiladas
    ├── test-classes/                  # Tests compilados
    └── manual-classes/                # Clases compiladas manualmente
```

---

## 🤖 Lógica de la IA (CPU)

Los jugadores CPU siguen esta estrategia (implementada en `JugadorCPU.elegirCarta()`):

### Prioridades de Selección (en orden):

1. **Captura crítica**: Si capturar esta carta completa el objetivo (21 puntos)
2. **Maximizar captura**: Busca la combinación que capture más cartas
3. **Capturar oros**: Prioriza capturar cartas del palo oros
4. **Capturar figuras**: Sota, Caballo, Rey son puntuaciones altas
5. **Limpieza (Escoba)**: Si pueda limpiar la mesa
6. **Cartas altas**: Juega cartas numeradas altas (7, 6, 5...)
7. **Cartas medias**: Juega cartas de valor medio
8. **Cartas bajas**: Última opción, cartas con poco valor

---

## 🔧 Problemas Comunes

### MongoDB no se conecta

**Síntoma**: Mensajes de timeout o "Connection refused"

**Solución**:
1. Valida que tu IP esté en la whitelist de MongoDB Atlas (Network Access)
2. Verifica que MONGO_URI en `.env` sea correcto
3. Asegúrate de que usuario/contraseña están URL-encoded si tienen caracteres especiales
4. El juego continuará funcionando sin persistencia si la BD no está disponible

### Errores de compilación con Java 21

**Síntoma**: "Source option 21 is not supported by this compiler"

**Solución**: Asegúrate de tener Java 21+ instalado:

```bash
java -version
```

Actualiza `pom.xml` si es necesario:

```xml
<maven.compiler.source>21</maven.compiler.source>
<maven.compiler.target>21</maven.compiler.target>
```

### Ruido de logs de MongoDB

**Síntoma**: Muchas líneas de INFO/DEBUG del driver MongoDB al iniciar

**Solución**: Ya está configurado en `Main.java` con `System.setProperty()` para suprimir logs INFO del driver

---

## 📚 Documentación Adicional

- **`ARQUITECTURA_MVC.md`**: Detalles técnicos de la arquitectura MVC
- **`EVALUACION_MVC.md`**: Evaluación de criterios de cumplimiento MVC
- **`java.md`**: Notas sobre características de Java usadas

---

## 💡 Mejoras Futuras

- [ ] Interfaz gráfica (JavaFX)
- [ ] Multijugador en red (sockets)
- [ ] Estadísticas avanzadas (ganador más frecuente, etc.)
- [ ] Niveles de dificultad configurable para CPU
- [ ] Replay de partidas guardadas
- [ ] Exportar estadísticas a gráficos

---

## 🎓 Notas de Aprendizaje

Este proyecto fue desarrollado como parte de un curso de Programación Orientada a Objetos (POO) en Java, demostrando:

- ✅ Diseño de clases y herencia (`Participante` → `JugadorHumano`, `JugadorCPU`)
- ✅ Interfaces y contratos (`VistaJuego`, `Participante`)
- ✅ Excepciones personalizadas
- ✅ Patrones de diseño (MVC, Singleton para MongoDBManager)
- ✅ Persistencia con bases de datos (MongoDB)
- ✅ Testing unitario (JUnit 5)
- ✅ Gestión de configuración (dotenv)
- ✅ Separación de responsabilidades

---

## 📝 Licencia

Este proyecto está bajo licencia **MIT**. Siéntete libre de usarlo, modificarlo y distribuirlo.

---

## 👤 Autor

Desarrollado como proyecto académico de POO en Java.

**Última actualización**: 30 de Mayo de 2026
