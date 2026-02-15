package Archivos;

/*
UNED Informática Compiladores 3307
Estudiante Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para manejo de los archivos 
-Leer el archivo .vb línea por línea
- Crear el archivo .log con numeración de 4 dígitos
- Escribe los errores
- (Opcional) Escribir tokens por línea
- (Opcional) Mostrar tabla de símbolos
- (Opcional) Clasificar líneas según TabladeExpresiones
*/

import Lexer.Token;
import Simbolos.SymbolTable;
import Simbolos.TabladeExpresiones;
import Lexer.Lexer;
import Errores.ErrorManager;
import Errores.Error;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    // ============================================================
    // LECTURA DEL ARCHIVO
    // ============================================================
    public String[] leerArchivo(String nombreArchivo) {

        try {
            File archivo = new File(nombreArchivo);

            if (!archivo.exists()) {
                System.out.println("El archivo indicado no existe: " + nombreArchivo);
                return null;
            }

            ArrayList<String> lineas = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(archivo));

            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }

            br.close();
            return lineas.toArray(new String[0]);

        } catch (Exception e) {
            System.out.println("!!!Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }

    // ============================================================
    // CREAR ARCHIVO LOG CON NUMERACIÓN
    // ============================================================
    public String crearArchivoLog(String nombreArchivo, String[] lineas) {

        try {
            String nombreBase;
            int punto = nombreArchivo.lastIndexOf('.');

            if (punto > 0) {
                nombreBase = nombreArchivo.substring(0, punto);
            } else {
                nombreBase = nombreArchivo;
            }

            String nombreLog = nombreBase + "-errores.log";

            FileWriter fw = new FileWriter(nombreLog, false);
            PrintWriter pw = new PrintWriter(fw);

            for (int i = 0; i < lineas.length; i++) {
                String numero = String.format("%04d", i + 1);
                pw.println(numero + " " + lineas[i]);
            }

            pw.close();
            fw.close();

            return nombreLog;

        } catch (Exception e) {
            System.out.println("Error al crear el archivo .log: " + e.getMessage());
            return null;
        }
    }

    // ============================================================
    // ESCRIBIR ERRORES
    // ============================================================
    public void escribirErrores(String nombreLog, ErrorManager errorManager) {

        try {
            if (nombreLog == null) {
                System.out.println("No se puede escribir errores: archivo .log nulo.");
                return;
            }

            List<Error> errores = errorManager.getErrores();

            if (errores == null || errores.isEmpty()) {
                return;
            }

            FileWriter fw = new FileWriter(nombreLog, true);
            PrintWriter pw = new PrintWriter(fw);

            pw.println();
            pw.println("------------------------------------------------------------");
            pw.println("ERRORES DETECTADOS:");
            pw.println("------------------------------------------------------------");

            for (Error err : errores) {
                String lineaFormateada = String.format("%04d", err.getLinea());
                pw.println("Error " + err.getNumero() + ". Línea " + lineaFormateada + ". " + err.getDescripcion());
            }

            pw.close();
            fw.close();

        } catch (Exception e) {
            System.out.println("Error al escribir errores en el .log: " + e.getMessage());
        }
    }

    // ============================================================
    // OPCIONAL: ESCRIBIR TOKENS POR LÍNEA
    // ============================================================
    public void escribirTokensPorLinea(String nombreArchivo, String[] lineas, Lexer lexer) {

        try {
            String nombreSalida = nombreArchivo + "-tokens.txt";
            PrintWriter pw = new PrintWriter(new FileWriter(nombreSalida));

            pw.println("=== LISTA DE TOKENS POR LÍNEA ===\n");

            for (int i = 0; i < lineas.length; i++) {
                String numero = String.format("%04d", i + 1);
                pw.println(numero + "  " + lineas[i]);

                List<Token> tokens = lexer.tokenizar(lineas[i]);

                for (Token t : tokens) {
                    pw.println("      " + t);
                }

                pw.println();
            }

            pw.close();
            System.out.println("Archivo generado: " + nombreSalida);

        } catch (Exception e) {
            System.out.println("Error al escribir tokens: " + e.getMessage());
        }
    }

    // ============================================================
    // OPCIONAL: MOSTRAR TABLA DE SÍMBOLOS
    // ============================================================
    public void escribirTablaSimbolos(String nombreArchivo, SymbolTable tabla) {

        try {
            String nombreSalida = nombreArchivo + "-simbolos.txt";
            PrintWriter pw = new PrintWriter(new FileWriter(nombreSalida));

            pw.println("=== TABLA DE SÍMBOLOS ===\n");
            pw.println(tabla.toString());

            pw.close();
            System.out.println("Archivo generado: " + nombreSalida);

        } catch (Exception e) {
            System.out.println("Error al escribir tabla de símbolos: " + e.getMessage());
        }
    }

    // ============================================================
    // OPCIONAL: CLASIFICAR LÍNEAS SEGÚN TABLADEEXPRESIONES
    // ============================================================
    public void escribirClasificacionLineas(String nombreArchivo, String[] lineas) {

        try {
            String nombreSalida = nombreArchivo + "-clasificacion.txt";
            PrintWriter pw = new PrintWriter(new FileWriter(nombreSalida));

            pw.println("=== CLASIFICACIÓN DE LÍNEAS ===\n");

            for (int i = 0; i < lineas.length; i++) {

                String numero = String.format("%04d", i + 1);
                String linea = lineas[i];

                TabladeExpresiones.Expresion tipo = clasificar(linea);

                pw.println(numero + "  " + linea);
                pw.println("      → " + tipo.name());
                pw.println();
            }

            pw.close();
            System.out.println("Archivo generado: " + nombreSalida);

        } catch (Exception e) {
            System.out.println("Error al clasificar líneas: " + e.getMessage());
        }
    }

    private TabladeExpresiones.Expresion clasificar(String linea) {
        for (TabladeExpresiones.Expresion exp : TabladeExpresiones.Expresion.values()) {
            if (linea.matches(exp.patron)) {
                return exp;
            }
        }
        return TabladeExpresiones.Expresion.DESCONOCIDO;
    }
}
