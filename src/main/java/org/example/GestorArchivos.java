package org.example;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {
    private static final String NOMBRE_FICHERO = "historial_partidas.csv";
    private static final String CABECERA = "fecha;jugador;puntos;cartas;oros;sietes;escobas;velo;resultado";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void guardarHistorialPartida(List<Jugador> jugadores, int[] puntos, String ganador) {
        if (jugadores == null || puntos == null) {
            throw new ExcepcionPersistenciaHistorial("Los datos de la partida no pueden ser nulos");
        }

        if (jugadores.size() != puntos.length) {
            throw new ExcepcionPersistenciaHistorial("El número de jugadores y de puntuaciones debe coincidir");
        }

        Path ruta = resolverRutaHistorial();
        boolean necesitaCabecera = Files.notExists(ruta);
        boolean separarPartidas = false;

        if (!necesitaCabecera) {
            try {
                long tamano = Files.size(ruta);
                necesitaCabecera = tamano == 0;
                separarPartidas = tamano > 0;
            } catch (IOException e) {
                throw new ExcepcionPersistenciaHistorial("No se pudo inspeccionar el historial existente", e);
            }
        }

        try {
            Path directorio = ruta.getParent();
            if (directorio != null) {
                Files.createDirectories(directorio);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(
                    ruta,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                if (separarPartidas) {
                    writer.newLine();
                }

                if (necesitaCabecera) {
                    writer.write(CABECERA);
                    writer.newLine();
                }

                String fecha = LocalDateTime.now().format(FORMATO_FECHA);
                for (int i = 0; i < jugadores.size(); i++) {
                    Jugador jugador = jugadores.get(i);
                    MontonJugador monton = jugador.getMonton();
                    String resultado = ganador == null ? "EMPATE" : (jugador.getNombre().equals(ganador) ? "GANADOR" : "PERDEDOR");

                    writer.write(String.join(";",
                            fecha,
                            jugador.getNombre(),
                            String.valueOf(puntos[i]),
                            String.valueOf(monton.getCartasCapturadas()),
                            String.valueOf(monton.getOrosCapturados()),
                            String.valueOf(monton.getSietes()),
                            String.valueOf(monton.getEscobas()),
                            monton.getVelo() ? "SI" : "NO",
                            resultado));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new ExcepcionPersistenciaHistorial("No se pudo guardar el historial de la partida", e);
        } catch (SecurityException e) {
            throw new ExcepcionPersistenciaHistorial("No hay permisos para escribir el historial de la partida", e);
        }
    }

    public List<String> cargarHistorialPartidas() {
        Path ruta = resolverRutaHistorial();
        List<String> lineas = new ArrayList<>();

        if (Files.notExists(ruta)) {
            return lineas;
        }

        try (BufferedReader reader = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            throw new ExcepcionPersistenciaHistorial("No se pudo leer el historial de la partida", e);
        } catch (SecurityException e) {
            throw new ExcepcionPersistenciaHistorial("No hay permisos para leer el historial de la partida", e);
        }

        return lineas;
    }

    private Path resolverRutaHistorial() {
        Path base = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();

        if (base.getFileName() != null && "classes".equalsIgnoreCase(base.getFileName().toString())) {
            Path parent = base.getParent();
            Path target = parent != null ? parent.getParent() : null;
            if (target != null) {
                return target.resolve(NOMBRE_FICHERO);
            }
        }

        return base.resolve(NOMBRE_FICHERO);
    }
}



