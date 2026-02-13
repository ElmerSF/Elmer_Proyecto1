/*
UNED Informática Compiladores 3307
Estudiante: Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Tabla de símbolos para almacenar variables declaradas y sus tipos
*/

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    // Mapa que almacena: nombreVariable → tipo
    private final Map<String, String> tabla;

    public SymbolTable() {
        this.tabla = new HashMap<>();
    }

    // ============================================================
    // REGISTRO DE VARIABLES
    // ============================================================

    /**
     * Registra una variable en la tabla de símbolos.
     * @param nombre Nombre del identificador
     * @param tipo Tipo declarado (Integer, String, Boolean, Double)
     */
    public void registrar(String nombre, String tipo) {
        tabla.put(nombre.toLowerCase(), tipo.toLowerCase());
    }

    // ============================================================
    // CONSULTAS
    // ============================================================

    /**
     * Verifica si una variable ya fue declarada.
     */
    public boolean existe(String nombre) {
        return tabla.containsKey(nombre.toLowerCase());
    }

    /**
     * Devuelve el tipo de una variable.
     * @return tipo en minúsculas (integer, string, boolean, double)
     */
    public String tipoDe(String nombre) {
        return tabla.get(nombre.toLowerCase());
    }

    // ============================================================
    // DEPURACIÓN / DEBUG
    // ============================================================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TABLA DE SÍMBOLOS:\n");

        for (Map.Entry<String, String> entry : tabla.entrySet()) {
            sb.append("  ").append(entry.getKey())
              .append(" : ").append(entry.getValue())
              .append("\n");
        }

        return sb.toString();
    }
}
