# Apuntes de Java

---

## Instalación del kernel de Java en Jupyter

### Instalación de jshell

```bash
choco install jbang
```

### Instalación del kernel iJava

Estos primeros pasos pueden no ser necesarios:
- Abrir PowerShell como administrador
- Instalar pip: https://www.dataquest.io/blog/install-pip-windows/
- Instalar jupyter: `pip install --upgrade jupyter`

**Pasos para instalar el kernel de Java en Jupyter (Windows):**

1. Descargar `ijava-1.3.0.zip` desde: https://github.com/SpencerPark/IJava/releases/tag/v1.3.0
2. Descomprimirlo y lanzar el script `install.py`
3. Se instalará en `C:\ProgramData\jupyter\kernels\java`
4. Moverlo a `C:\Users\usuario\AppData\Roaming\jupyter\kernels\java\ijava1.3.0.jar`
5. Modificar `kernel.json` para que apunte a la nueva ubicación: `C:\Users\usuario\AppData\Roaming\jupyter\kernels\java\ijava-1.3.0.jar`

**En Linux:**

```bash
sudo snap install --classic jbang
sudo apt install python3-pip
sudo apt install python3-jupyter-client
# Crear carpeta
mkdir -p /usr/local/share/jupyter/kernels
# Descargar ijava-1.3.0.zip y descomprimir
sudo python3 install.py
```

Seleccionar el kernel de Java en VSCode.

---

## Introducción a Java

Java es un lenguaje de programación de propósito general, concurrente, orientado a objetos. Su intención es "escribir una vez, ejecutar en cualquier lugar" (WORA).

Java compila a **bytecode** ejecutado por la **JVM** (Java Virtual Machine), lo que permite que el código funcione en cualquier máquina con una implementación de la JVM.

---

## Estructura de un programa en Java

- Un programa Java está compuesto por una o más clases.
- Cada clase es un archivo separado con el mismo nombre que la clase (e.g., `MiClase.java`).
- Solo una clase por archivo puede ser `public`.
- Todo programa Java necesita al menos una clase con un método `main`.

```java
public class MiClase {
    public static void main(String[] args) {
        // Código que se ejecutará cuando el programa sea lanzado
    }
}
```

```java
public class HolaMundo {
    public static void main(String[] args) {
        System.out.println("Hola Mundo");
    }
}
```

### Escritura por pantalla

```java
System.out.println("Hola Mundo");
```

---

## Sintaxis básica

- Java es **Case Sensitive** (diferencia mayúsculas y minúsculas).
- Los bloques de código se delimitan con llaves `{}`.
- Las instrucciones terminan **siempre** con punto y coma `;`.

---

## Comentarios

Existen tres tipos de comentarios en Java:

```java
// Comentario de una línea

/* Comentario
   de varias líneas */

/**
 * Comentario de documentación
 * @return descripción
 */
```

---

## Identificadores

Los identificadores deben:
- Comenzar con una letra, `$` o `_`
- Ser Case Sensitive
- No contener espacios en blanco
- No ser una palabra reservada

**Palabras reservadas:** `abstract`, `assert`, `boolean`, `break`, `byte`, `case`, `catch`, `char`, `class`, `const`, `continue`, `default`, `do`, `double`, `else`, `enum`, `extends`, `final`, `finally`, `float`, `for`, `goto`, `if`, `implements`, `import`, `instanceof`, `int`, `interface`, `long`, `native`, `new`, `package`, `private`, `protected`, `public`, `return`, `short`, `static`, `strictfp`, `super`, `switch`, `synchronized`, `this`, `throw`, `throws`, `transient`, `try`, `void`, `volatile`, `while`

### Convenciones de nombres

| Elemento    | Convención               | Ejemplo          |
|-------------|--------------------------|------------------|
| Variables   | camelCase (minúscula)    | `nombreAlumno`   |
| Clases      | PascalCase               | `MiClase`        |
| Métodos     | camelCase (minúscula)    | `calcularMedia`  |
| Constantes  | MAYÚSCULAS_CON_GUIONES   | `MAX_VALOR`      |
| Paquetes    | minúsculas               | `gal.iesteis`    |

---

## Tipos de datos

### Tipos primitivos

| Tipo      | Tamaño  | Descripción                        |
|-----------|---------|------------------------------------|
| `byte`    | 8 bits  | Rango: -128 a 127                  |
| `short`   | 16 bits | Rango: -32.768 a 32.767            |
| `int`     | 32 bits | Rango: −2³¹ a 2³¹−1               |
| `long`    | 64 bits | Rango: −2⁶³ a 2⁶³−1               |
| `float`   | 32 bits | Precisión simple                   |
| `double`  | 64 bits | Doble precisión                    |
| `boolean` | 1 bit   | `true` o `false`                   |
| `char`    | 16 bits | Carácter Unicode                   |
| `void`    | —       | Sin valor                          |

Si se excede el rango de un tipo de dato, se producirá **desbordamiento (overflow)**.

```java
int a = 5;
long b = 20_123_456;
System.out.println(a + " " + b);
```

### Literales

```java
// int
int decimal = 100;
int octal   = 0144;
int hex     = 0x64;
int bin     = 0b1100100;

// long (sufijo L obligatorio)
long l = 100L;

// float (sufijo F obligatorio)
float f = 100.0f;
float f2 = 1.0e2f;

// double
double d  = 100.0;
double d2 = 1.0e2;

// char
char c1 = 'a';
char c2 = '\u0061';
char c3 = 97;
char c4 = '\n';

// boolean
boolean b1 = true;
boolean b2 = false;
```

> Es obligatorio indicar el sufijo de los literales `long` y `float`.

---

## Declaración de variables

```java
int edad;         // Declaración
edad = 20;        // Inicialización

int edad = 20;    // Declaración e inicialización

int a, b, c;
int edad = 20, altura = 180, peso = 80;

// Para tipos no primitivos (instanciación)
Rectangulo r = new Rectangulo(20, 10);
```

---

## La clase String

```java
String nombre = "Juan";
System.out.println("Hola " + nombre);
```

---

## Conversión de tipos (casting)

### Conversión implícita (widening)
```java
int a = 10;
long b = a; // automática
```

### Conversión explícita (narrowing)
```java
long a = 100;
int b = (int) a;
```

```java
byte a = 5, b = 3, c;
c = (byte) (a * b);
```

---

## Operadores

### Aritméticos

| Operador | Significado       |
|----------|-------------------|
| `+`      | Suma              |
| `-`      | Resta             |
| `*`      | Multiplicación    |
| `/`      | División          |
| `%`      | Resto o módulo    |

> No existe operador de división entera ni de potencia. Para potencias: `Math.pow(base, exp)`.

**Resultado de operaciones aritméticas:**
- Si hay un operando `long` y ninguno real → resultado `long`
- Si ninguno es `long` ni real → resultado `int`
- Si al menos uno es `double` → resultado `double`
- Si al menos uno es `float` y ninguno `double` → resultado `float`

### Incrementales y decrementales

| Operador | Prefijo | Postfijo |
|----------|---------|----------|
| `++`     | `++a`   | `a++`    |
| `--`     | `--a`   | `a--`    |

```java
int x = 3;
int y = x++; // y=3, x=4
int y = ++x; // y=4, x=4
```

### Asignación

| Operador | Significado            |
|----------|------------------------|
| `=`      | Asignación             |
| `+=`     | Suma y asignación      |
| `-=`     | Resta y asignación     |
| `*=`     | Multiplicación y asig. |
| `/=`     | División y asignación  |
| `%=`     | Resto y asignación     |

### Relacionales

| Operador | Significado      |
|----------|------------------|
| `==`     | Igual a          |
| `!=`     | Distinto de      |
| `>`      | Mayor que        |
| `<`      | Menor que        |
| `>=`     | Mayor o igual    |
| `<=`     | Menor o igual    |

### Lógicos

| Operador | Significado    |
|----------|----------------|
| `&&`     | Y lógico       |
| `\|\|`   | O lógico       |
| `!`      | Negación       |
| `^`      | O exclusivo    |

> `&&` y `||` usan evaluación en cortocircuito; `&` y `|` evalúan siempre ambos operandos.

### Operador ternario

```java
condición ? valor1 : valor2

int paga = edad >= 10 ? 5 : 2;
```

### Precedencia de operadores (mayor a menor)

1. Paréntesis `()`
2. Postfijo `++`, `--`
3. Unarios `+`, `-`, `++`, `--`, `!`, (cast)
4. `*`, `/`, `%`
5. `+`, `-`
6. `>`, `<`, `>=`, `<=`
7. `==`, `!=`
8. `&&`, `||`, `^`
9. `? :`
10. `=`, `+=`, `-=`, `*=`, `/=`, `%=`

---

## Flujo estándar de datos

```java
System.in   // entrada estándar (teclado)
System.out  // salida estándar (pantalla)
System.err  // error estándar (pantalla)
```

---

## Lectura de datos (Scanner)

```java
import java.util.Scanner;
Scanner teclado = new Scanner(System.in);
```

**Métodos principales:**

| Método            | Tipo devuelto |
|-------------------|---------------|
| `nextLine()`      | `String`      |
| `nextInt()`       | `int`         |
| `nextDouble()`    | `double`      |
| `nextFloat()`     | `float`       |
| `nextLong()`      | `long`        |
| `nextBoolean()`   | `boolean`     |
| `next()`          | `String`      |

> `nextInt()`, `nextDouble()`, etc. no consumen el salto de línea. Usar `teclado.nextLine()` para limpiar el buffer antes de leer un `String`.

```java
// Leer un carácter
char letra = teclado.next().charAt(0);
```

---

## Estructuras selectivas

### if ... else

```java
if (condición) {
    // ...
} else if (condición2) {
    // ...
} else {
    // ...
}
```

### switch-case tradicional (antes de Java 12)

```java
switch (variable) {
    case valor1:
        // ...
        break;
    case valor2:
        // ...
        break;
    default:
        // ...
}
```

- La expresión debe ser `byte`, `short`, `int`, `char` o `String`.
- Los valores del `case` deben ser literales o constantes.
- Sin `break`, la ejecución continúa al siguiente `case` (fall-through).

### switch mejorado / rule-switch (Java 12+)

```java
switch (variable) {
    case valor1 -> System.out.println("uno");
    case valor2, valor3 -> System.out.println("dos o tres");
    default -> System.out.println("otro");
}
```

**Como expresión:**

```java
String resultado = switch (d) {
    case 'L' -> "Lunes";
    case 'M' -> "Martes";
    case 'X': yield "Miércoles"; // yield obligatorio con ':'
    default -> "";
};
```

---

## Estructuras repetitivas

### Bucle for

```java
for (int i = 0; i <= 10; i += 2) {
    System.out.println(i);
}
```

### Bucle while

```java
while (condición) {
    // bloque
}
```

> Siempre hay que leer un valor antes de entrar al `while`.

### Bucle do-while

```java
do {
    // bloque
} while (condición);
```

> El bloque se ejecuta **al menos una vez**.

### Ámbito de las variables

Las variables declaradas dentro de un bucle solo existen dentro de él.

### Sentencias break y continue

- `break`: sale del bucle.
- `continue`: salta a la siguiente iteración.

> Usar con moderación para mantener el código legible.

---

## POO en Java

### Clases

```java
[acceso] class NombreClase [extends] [implements] {
    [acceso] tipo atributo;
    [acceso] tipo metodo([params]) { ... }
}
```

```java
public class Persona {
    private String nombre;
    private int edad;

    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad   = edad;
    }
}
```

### Objetos

```java
Persona persona = new Persona("Juan", 25);
Persona p = persona; // Dos referencias al mismo objeto
```

### Métodos

- Paso **por valor** para tipos primitivos (copia del valor).
- Paso **por referencia** para objetos (dirección de memoria).

### Acceso a atributos y métodos

```java
nombreObjeto.atributo;
nombreObjeto.metodo(params);
```

### null y this

```java
// null: variable sin objeto asignado
Persona p1 = null;

// this: referencia al objeto actual
public Circulo(double radio) {
    this.radio = radio;
}
```

### Getters y Setters

```java
public double getRadio() { return radio; }
public void setRadio(double radio) {
    if (radio >= 0) this.radio = radio;
}
```

### Constructores

- Mismo nombre que la clase, sin tipo de retorno.
- Si no se define, Java provee el constructor por defecto.
- Si se define uno con parámetros, el por defecto desaparece.
- Pueden sobrecargarse.

```java
public Circulo() { this.radio = 0; }
public Circulo(double radio) { this.radio = radio; }
```

### Atributos y métodos estáticos

Pertenecen a la **clase**, no a los objetos.

```java
NombreClase.atributo;
NombreClase.metodo(params);
```

```java
public static boolean DNIcorrecto(String dni) {
    return dni.length() == 9;
}
```

### Métodos mágicos

| Método        | Cuándo se ejecuta                         |
|---------------|-------------------------------------------|
| `toString()`  | Al convertir el objeto a String           |
| `equals()`    | Al comparar dos objetos                   |
| `hashCode()`  | Al obtener el código hash                 |
| `clone()`     | Al clonar un objeto                       |
| `compareTo()` | Al comparar dos objetos                   |

---

## Clases Wrapper

Envuelven tipos primitivos como objetos.

```java
Integer entero = 5;
```

### Clase Character

```java
Character.isDigit(c)
Character.isLetter(c)
Character.isLetterOrDigit(c)
Character.isLowerCase(c)
Character.isUpperCase(c)
Character.isSpaceChar(c)
Character.isWhitespace(c)
Character.toLowerCase(c)
Character.toUpperCase(c)
Character.toString(c)
```

---

## La clase String

- Inmutable: cada operación crea un nuevo objeto.
- Se puede crear con literal o con `new`.

```java
String cadena  = "Hola Mundo";
String cadena2 = new String("Hola Mundo");
```

### Métodos principales

```java
// Concatenación
cadena.concat(otra)

// Longitud
cadena.length()

// Comparación
cadena.equals(otra)
cadena.equalsIgnoreCase(otra)
cadena.isEmpty()
cadena.compareTo(otra)
cadena.compareToIgnoreCase(otra)

// Conversión
cadena.toLowerCase()
cadena.toUpperCase()
String.valueOf(valor)
cadena.replace('a', 'o')
cadena.replace("am", "o")

// Búsqueda
cadena.charAt(index)
cadena.indexOf(c)
cadena.lastIndexOf(c)
cadena.indexOf(str)
cadena.startsWith(prefix)
cadena.endsWith(suffix)
cadena.contains(seq)

// Extracción
cadena.substring(inicio)
cadena.substring(inicio, fin)
cadena.trim()
cadena.split(regex)
String.join(delimitador, elementos)

// Formateo
String.format("%d %s %.2f", entero, texto, decimal)
System.out.printf("El valor es %.2f%n", 3.14)
```

### Conversión de tipos

```java
// Primitivo a String
String s = String.valueOf(5);
String s = "" + 5;
String s = Integer.toString(5);

// String a primitivo
int    i = Integer.parseInt("5");
float  f = Float.parseFloat("5.5");
double d = Double.parseDouble("5.5");
long   l = Long.parseLong("5");
```

### Recorrido de Strings

```java
for (int i = 0; i < cadena.length(); i++) {
    System.out.println(cadena.charAt(i));
}
```

### Expresiones regulares

```java
cadena.matches("[0-9]{4}[A-Z]{3}")

// Con Pattern y Matcher
Pattern patron  = Pattern.compile("\\d{2,5}");
Matcher matcher = patron.matcher("2345");
matcher.matches();

// Buscar todas las coincidencias
while (matcher.find()) {
    System.out.println(matcher.group() + " " + matcher.start() + "-" + matcher.end());
}
```

---

## La clase StringBuilder

Cadena **mutable**.

```java
StringBuilder sb = new StringBuilder("Hola Mundo");
sb.append("!");
sb.insert(5, "el ");
sb.delete(5, 8);
sb.deleteCharAt(5);
sb.replace(5, 9, "clase");
sb.reverse();
sb.setCharAt(0, 'H');
sb.length();
sb.capacity();
```

---

## Arrays

```java
// Declaración e inicialización
String[] nombres = new String[5];
String[] nombres = {"Juan", "Pedro", "Maria"};

// Acceso
nombres[0] = "Juan";

// Longitud
numeros.length

// Bucle for
for (int i = 0; i < numeros.length; i++) { ... }

// Bucle for-each (no permite modificar)
for (String nombre : nombres) { ... }

// Paso a métodos (por referencia)
public float calcularMedia(int[] numeros) { ... }

// Retorno de arrays
public static int[] potencias2(int n) { ... }
```

### Arrays multidimensionales

```java
int[][] matriz = new int[3][4];
// Acceso: matriz[fila][columna]

// Irregulares
int[][] arr = {{2,3}, {4,5,6}, {7,8,9,10}};
```

### Clase Arrays

```java
Arrays.fill(array, valor)
Arrays.fill(array, desde, hasta, valor)
Arrays.equals(a1, a2)
Arrays.deepEquals(a1, a2)
Arrays.sort(array)
Arrays.binarySearch(array, valor)  // array debe estar ordenado
Arrays.copyOf(array, n)
Arrays.copyOfRange(array, desde, hasta)
Arrays.toString(array)
Arrays.deepToString(array)
```

### Conversión String → array

```java
String[] palabras = frase.split(" ");
String[] palabras = frase.split("[,.;]?\\s+");
```

---

## Colecciones (interfaz Collection)

Las colecciones son estructuras **dinámicas**. Solo almacenan objetos (usar Wrappers para primitivos).

**Tipos principales:**
- **Listas**: ordenadas, permiten duplicados (FIFO)
- **Colas**: punteros al inicio y final (LIFO)
- **Conjuntos**: no ordenados, sin duplicados
- **Mapas**: pares clave-valor (interfaz `Map`)

**Métodos comunes:**

```java
add(e)
remove(o)
contains(o)
size()
isEmpty()
clear()
iterator()
toArray()
```

### Clase Collections

```java
Collections.sort(lista)
Collections.reverse(lista)
Collections.shuffle(lista)
Collections.binarySearch(lista, clave)
```

---

## Clase ArrayList

```java
ArrayList<Integer> lista = new ArrayList<>();
ArrayList<String>  nombres = new ArrayList<>();

// Constructores
new ArrayList<>()
new ArrayList<>(capacidadInicial)
new ArrayList<>(otraColeccion)

// Métodos
lista.add(elemento)
lista.add(indice, elemento)
lista.addAll(coleccion)
lista.get(indice)
lista.set(indice, elemento)
lista.remove(indice)
lista.remove(objeto)
lista.contains(objeto)
lista.indexOf(objeto)
lista.size()
lista.isEmpty()
lista.clear()
lista.toArray()
lista.toString()
```

### Recorrido

```java
// for
for (int i = 0; i < nombres.size(); i++) { nombres.get(i); }

// for-each
for (String nombre : nombres) { ... }

// Iterador (ideal para eliminar)
Iterator<String> it = nombres.iterator();
while (it.hasNext()) {
    String nombre = it.next();
    if (nombre.equals("Emilio")) it.remove(); // OJO: remove del iterador
}
```

---

## Maps (HashMap)

```java
HashMap<String, String> capitales = new HashMap<>();
capitales.put("España", "Madrid");
capitales.get("España");
capitales.replace("España", "Madrid");
capitales.remove("España");
capitales.containsKey("España");
capitales.containsValue("Madrid");
capitales.keySet();
capitales.values();
capitales.size();
capitales.isEmpty();
capitales.clear();
```

### Recorrido

```java
for (String pais : capitales.keySet()) {
    System.out.println(pais + " → " + capitales.get(pais));
}
```

---

## Fecha y hora

```java
import java.time.*;

// LocalDate
LocalDate hoy    = LocalDate.now();
LocalDate fecha  = LocalDate.of(2019, 11, 29);
LocalDate fecha2 = LocalDate.parse("1990-12-31");

// LocalTime
LocalTime hora = LocalTime.of(8, 30, 12);
LocalTime hora2 = LocalTime.parse("08:30");

// LocalDateTime
LocalDateTime fh = LocalDateTime.of(2019, Month.NOVEMBER, 29, 8, 30, 0);
LocalDateTime fh2 = LocalDateTime.parse("2018-10-10T11:25");
```

### Obtención de información

```java
fecha.getYear()          fecha.getMonthValue()   fecha.getMonth()
fecha.getDayOfMonth()    fecha.getDayOfWeek()     fecha.getDayOfYear()
fecha.isLeapYear()       fecha.lengthOfMonth()    fecha.lengthOfYear()
hora.getHour()           hora.getMinute()          hora.getSecond()
```

### Operaciones

```java
fecha.plusDays(1)         fecha.minusDays(1)
fecha.plusMonths(1)       fecha.minusMonths(1)
fecha.plusYears(1)        fecha.minusYears(1)
fecha.withDayOfMonth(1)   fecha.withMonth(6)       fecha.withYear(2025)
fecha.plus(1, ChronoUnit.MONTHS)
```

### Comparación

```java
fecha.isAfter(otra)    fecha.isBefore(otra)    fecha.isEqual(otra)
fecha.compareTo(otra)
```

### Diferencia entre fechas

```java
fecha.until(otra, ChronoUnit.DAYS)
ChronoUnit.MONTHS.between(fecha1, fecha2)
```

### Clase Period

```java
Period.of(1, 4, 6)          // 1 año, 4 meses, 6 días
Period.ofYears(2)
Period.ofMonths(3)
Period.ofDays(10)
Period.between(fecha1, fecha2)

periodo.getYears()    periodo.getMonths()    periodo.getDays()
```

### Formateo

```java
import java.time.format.*;

DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
System.out.println(hoy.format(fmt));

// Con locale
Locale local = new Locale("es", "ES");
DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy", local);
```

---

## Generación de números aleatorios

### Math.random()

```java
// Número entre 1 y 100
(int)(Math.random() * 100) + 1

// Número entre min y max
(int)(Math.random() * (max - min + 1)) + min
```

### Clase Random

```java
import java.util.Random;
Random r = new Random();

r.nextInt()          // entero aleatorio
r.nextInt(n)         // entero en [0, n)
r.nextDouble()       // double en [0.0, 1.0)
r.nextBoolean()
r.nextLong()
r.nextFloat()
r.nextGaussian()     // distribución normal

// Con semilla (reproducible)
Random r = new Random(100);
```

---

## Ficheros .env en Java

### ¿Por qué usar ficheros .env?

- **Seguridad**: evitan exponer datos sensibles en el código.
- **Portabilidad**: configuración diferente por entorno.
- **Mantenimiento**: configuración centralizada.

### Estructura

```
# Comentario
DB_HOST=localhost
DB_PORT=5432
DB_USER=admin
DB_PASS=s3cr3t
API_KEY=abc123xyz
```

> Añadir siempre al `.gitignore`. Incluir `.env.example` con las claves pero sin valores.

### Librería dotenv-java (Maven)

```xml
<dependency>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>dotenv-java</artifactId>
    <version>3.2.0</version>
</dependency>
```

El fichero `.env` debe estar en la raíz del proyecto (al mismo nivel que `pom.xml`).

```java
import io.github.cdimascio.dotenv.Dotenv;

Dotenv dotenv = Dotenv.load();
String host = dotenv.get("DB_HOST");
String user = dotenv.get("DB_USER", "por_defecto"); // con valor por defecto
```

### Opciones de configuración

```java
Dotenv dotenv = Dotenv.configure()
    .directory("./config")
    .filename(".env.local")
    .ignoreIfMissing()
    .ignoreIfMalformed()
    .load();
```

---

## Paquetes

```java
package gal.iesteis.fp;

import java.util.Scanner;
import java.util.*;       // importar todo el paquete
```

### Modificadores de acceso

| Modificador         | Clase | Paquete | Subclase | Todo |
|---------------------|-------|---------|----------|------|
| `public`            | ✓     | ✓       | ✓        | ✓    |
| `protected`         | ✓     | ✓       | ✓        | ✗    |
| Sin modificador     | ✓     | ✓       | ✗        | ✗    |
| `private`           | ✓     | ✗       | ✗        | ✗    |

**Reglas de uso:**
- Atributos: siempre `private` o `protected`.
- Métodos: lo más restrictivos posible.
- Clases: `public` o sin modificador.

---

## Herencia

Representa la relación "es-un" (is-a).

```java
public class Mago extends Personaje {
    private String tipoVarita;

    public Mago(String nombre, int nivel, String tipoVarita) {
        super(nombre, nivel); // Llamada al constructor padre (primera línea)
        this.tipoVarita = tipoVarita;
    }

    @Override
    public void presentarse() {
        super.presentarse(); // Llamada al método del padre
        System.out.println("Soy un mago.");
    }
}
```

- No existe **herencia múltiple** en Java.
- `super()` debe ser la **primera línea** del constructor hijo.
- Si la clase madre no tiene constructor por defecto, debe llamarse explícitamente.

### Métodos y clases final

- `final` en un método: no puede sobrescribirse.
- `final` en una clase: no puede heredarse.

### Métodos heredados de Object

```java
toString()     equals(obj)    hashCode()
getClass()     clone()
```

### instanceof

```java
cuadrado instanceof Poligono   // true
poligono instanceof Cuadrado   // false

// Con cast implícito (Java 16+)
if (personaje instanceof Guerrero g) {
    g.usarEspada();
}
```

---

## Polimorfismo

```java
Personaje p = new Guerrero("Conan", 10); // Variable padre, objeto hijo
p.presentarse(); // Llama al método del hijo (si está sobrescrito)

ArrayList<Personaje> lista = new ArrayList<>();
lista.add(new Guerrero("Conan", 10));
lista.add(new Mago("Gandalf", 5, "Roble"));

for (Personaje personaje : lista) {
    personaje.presentarse(); // Despacho dinámico
    if (personaje instanceof Mago mago) {
        mago.lanzarHechizo();
    }
}
```

---

## Excepciones

### Tipos

- **Verificadas (checked)**: subclases de `Exception` (no de `RuntimeException`). El compilador obliga a manejarlas.
- **No verificadas (unchecked)**: subclases de `RuntimeException`. Causadas generalmente por errores de programación.

### Jerarquía

```
Throwable
├── Error (graves, no recuperables)
└── Exception
    ├── IOException (checked)
    ├── SQLException (checked)
    └── RuntimeException (unchecked)
        ├── NullPointerException
        ├── ArithmeticException
        ├── IndexOutOfBoundsException
        └── NumberFormatException
```

### try-catch-finally

```java
try {
    // código que puede lanzar excepción
} catch (InputMismatchException e) {
    System.out.println("No es un entero: " + e.getMessage());
} catch (ArithmeticException e) {
    System.out.println("Error aritmético");
} catch (IOException | SQLException e) {
    // múltiples tipos
} catch (Exception e) {
    e.printStackTrace();
} finally {
    // siempre se ejecuta
}
```

### Relanzamiento

```java
public int leeEntero() throws InputMismatchException {
    return new Scanner(System.in).nextInt();
}
```

### Lanzamiento manual

```java
throw new Exception("Edad inválida");
```

### Creación de excepciones propias

```java
public class SaldoNegativoExcepcion extends Exception {
    public SaldoNegativoExcepcion() {
        super("El saldo no puede ser negativo");
    }
}
```

---

## Clases abstractas

```java
public abstract class Animal {
    public abstract void hacerSonido(); // Sin implementación
    public void respirar() {            // Con implementación
        System.out.println("Respira.");
    }
}

public class Perro extends Animal {
    @Override
    public void hacerSonido() {
        System.out.println("Guau");
    }
}
```

- No pueden instanciarse.
- Pueden tener métodos abstractos y concretos.
- Las subclases deben implementar todos los métodos abstractos o ser también abstractas.

---

## Interfaces

```java
interface Cantante {
    String formatoCancion = "mp3"; // public static final implícito
    void cantar();                  // public abstract implícito
    default double tarifa() { return 0; }
    static void info() { System.out.println("Cantante"); }
}

class Persona implements Cantante {
    @Override
    public void cantar() { System.out.println("La la la"); }
}
```

Una clase puede implementar **múltiples interfaces**:

```java
public class Perro extends Mamifero implements Ladrador, Protector { ... }
```

### Diferencias entre clase abstracta e interfaz

| Aspecto            | Clase abstracta              | Interfaz                        |
|--------------------|------------------------------|---------------------------------|
| Herencia           | `extends` (solo una)         | `implements` (varias)           |
| Semántica          | "es un tipo de"              | "tiene este comportamiento"     |
| Atributos          | Cualquier tipo               | Solo `public static final`      |
| Constructores      | Sí                           | No                              |
| Métodos concretos  | Sí                           | Sí (`default` y `static`)       |

---

## Enumerados

```java
public enum Direccion {
    NORTE, SUR, ESTE, OESTE
}

Direccion d = Direccion.NORTE;
```

**Métodos heredados de Enum:**

```java
d.name()          // "NORTE"
d.toString()      // "NORTE"
d.ordinal()       // posición (0, 1, 2...)
d.equals(otro)
d.compareTo(otro)
Direccion.values()       // array con todas las constantes
Direccion.valueOf("SUR") // constante por nombre
```

### Enumerados con atributos

```java
public enum TipoCafe {
    CORTADO(1.5), LARGO(1.8), CAPUCHINO(2.0);

    private double precio;

    TipoCafe(double precio) { this.precio = precio; } // constructor privado

    public double getPrecio() { return precio; }
}
```

---

## Acceso a Bases de Datos

### MySQL — Driver (Maven)

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>9.7.0</version>
</dependency>
```

### PostgreSQL — Driver (Maven)

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.11</version>
</dependency>
```

### Conexión

```java
// MySQL
Connection conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/pruebas", "root", "password");

// PostgreSQL
Connection conn = DriverManager.getConnection(
    "jdbc:postgresql://localhost:5432/pruebas", "root", "password");
```

### try-with-resources (cierre automático)

```java
try (Connection conn = DriverManager.getConnection(url, user, pass)) {
    // operaciones
} catch (SQLException e) {
    e.printStackTrace();
}
```

### Operaciones CRUD

```java
// SELECT
String sql = "SELECT * FROM Alumnos WHERE YEAR(fecha_nacimiento) = ?";
try (Connection conn = DriverManager.getConnection(url, user, pass);
     PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setInt(1, 2005);
    try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            System.out.println(rs.getString("nombre"));
        }
    }
}

// INSERT / UPDATE / DELETE
String sql = "INSERT INTO Alumnos (nombre, apellido, fecha_nacimiento) VALUES (?, ?, ?)";
try (Connection conn = DriverManager.getConnection(url, user, pass);
     PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setString(1, "Fernando");
    ps.setString(2, "Gómez");
    ps.setDate(3, Date.valueOf(LocalDate.of(2004, 4, 13)));
    int filas = ps.executeUpdate();
}
```

> Para fechas: usar `java.sql.Date.valueOf(localDate)`.

### ResultSet — métodos

```java
rs.next()                        // avanza al siguiente registro
rs.getString("columna")
rs.getInt("columna")
rs.getDouble(posicion)           // posición empieza en 1
rs.getRow()
```

### Transacciones

```java
conn.setAutoCommit(false);
try {
    // operaciones...
    conn.commit();
} catch (SQLException e) {
    conn.rollback();
    throw e;
}
```

### Patrón DAO

```java
public interface AlumnoDAO {
    boolean insertar(String nombre, String apellido, LocalDate fecha) throws SQLException;
    List<Alumno> obtenerPorAnho(int anho) throws SQLException;
}

public class AlumnoDAOImpl implements AlumnoDAO {
    private final String url, usuario, password;
    // implementación de cada método con PreparedStatement
}
```

---

## Acceso a MongoDB

### Driver (Maven)

```xml
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>5.7.0</version>
</dependency>
```

### Equivalencias con SQL

| SQL           | MongoDB         |
|---------------|-----------------|
| Base de datos | Base de datos   |
| Tabla         | Colección       |
| Fila          | Documento       |
| Columna       | Campo           |
| JOIN          | `$lookup`       |

### Conexión

```java
try (MongoClient cliente = MongoClients.create("mongodb://admin:pass@localhost:27017")) {
    MongoDatabase bd = cliente.getDatabase("pruebas");
    MongoCollection<Document> col = bd.getCollection("alumnos");
    // operaciones
}
```

### La clase Document

```java
Document alumno = new Document("nombre", "Ana")
    .append("apellido", "García")
    .append("edad", 20)
    .append("modulos", List.of("Programación", "BBDD"));
```

### CRUD

```java
// INSERT
col.insertOne(documento);
col.insertMany(listaDocumentos);

// SELECT
for (Document doc : col.find()) { ... }
col.find(filtro).first();

// UPDATE
col.updateOne(filtro, Updates.set("edad", 21));
col.updateMany(filtro, Updates.combine(Updates.set("activo", true), Updates.inc("edad", 1)));

// DELETE
col.deleteOne(filtro);
col.deleteMany(filtro);

// COUNT
col.countDocuments();
col.countDocuments(filtro);
```

### Filtros (Filters)

```java
Filters.eq("campo", valor)
Filters.ne("campo", valor)
Filters.gt("campo", valor)       Filters.gte("campo", valor)
Filters.lt("campo", valor)       Filters.lte("campo", valor)
Filters.in("campo", lista)
Filters.regex("campo", "^A")
Filters.and(f1, f2)
Filters.or(f1, f2)
```

### Proyecciones

```java
Bson proj = Projections.fields(
    Projections.include("nombre", "apellido"),
    Projections.excludeId()
);
col.find().projection(proj);
```

### Ordenación y límite

```java
col.find().sort(Sorts.ascending("edad")).limit(3).skip(10);
```

### Updates

```java
Updates.set("campo", valor)
Updates.unset("campo")
Updates.inc("campo", cantidad)
Updates.push("campo", valor)
Updates.pull("campo", valor)
Updates.combine(u1, u2)
```

### Transacciones

```java
try (MongoClient cliente = MongoClients.create(url);
     ClientSession sesion = cliente.startSession()) {
    sesion.startTransaction();
    try {
        col.updateOne(sesion, filtro1, Updates.inc("saldo", -100));
        col.updateOne(sesion, filtro2, Updates.inc("saldo",  100));
        sesion.commitTransaction();
    } catch (MongoException e) {
        sesion.abortTransaction();
    }
}
```

### Excepciones MongoDB

| Operación           | Excepción                   |
|---------------------|-----------------------------|
| `create()`          | `MongoConfigurationException` |
| Credenciales        | `MongoSecurityException`    |
| `insertOne()`       | `MongoWriteException`       |
| `insertMany()`      | `MongoBulkWriteException`   |
| `find()`            | `MongoQueryException`       |
| Timeout             | `MongoTimeoutException`     |
| Red                 | `MongoSocketException`      |

> Todas heredan de `MongoException` (unchecked).

### Patrón DAO con MongoDB

```java
public interface AlumnoDAO {
    void insertar(String nombre, String apellido, int edad);
    List<Document> buscarPorApellido(String apellido);
    boolean actualizarEdad(String nombre, int nuevaEdad);
    long eliminarPorNombre(String nombre);
}

// El MongoClient se crea una sola vez y se comparte (thread-safe)
public class AlumnoDAOMongo implements AlumnoDAO {
    private final MongoCollection<Document> coleccion;
    public AlumnoDAOMongo(MongoClient cliente, String bd) {
        this.coleccion = cliente.getDatabase(bd).getCollection("alumnos");
    }
    // implementación...
}
```
