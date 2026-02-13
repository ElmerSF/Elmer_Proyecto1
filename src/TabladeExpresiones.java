/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author elmer
 */
public class TabladeExpresiones {

    public enum Expresion {

        // ============================================================
        // LÍNEAS VACÍAS O SOLO ESPACIOS
        // ============================================================
        LINEA_VACIA("^\\s*$"),

        // ============================================================
        // COMENTARIOS (solo válidos si la línea inicia con ')
        // ============================================================
        COMENTARIO("^\\s*'.*$"),

        // ============================================================
        // IMPORTS
        // ============================================================
        IMPORTS("^\\s*Imports\\s+[A-Za-z][A-Za-z0-9_]*\\s*$"),

        // ============================================================
        // MODULE <Identificador>
        // ============================================================
        MODULE("^\\s*Module\\s+[A-Za-z][A-Za-z0-9_]*\\s*$"),

        // ============================================================
        // END MODULE
        // ============================================================
        END_MODULE("^\\s*End\\s+Module\\s*$"),

        // ============================================================
        // DECLARACIÓN DIM
        // Formatos válidos:
        // Dim x As Integer
        // Dim x As Integer = 5
        // Dim x As Integer = a + b * 3
        // ============================================================
        DIM_DECLARACION(
            "^\\s*Dim\\s+[A-Za-z][A-Za-z0-9_]*\\s+As\\s+(Integer|String|Boolean|Double)" +
            "(\\s*=\\s*.+)?$"
        ),

        // ============================================================
        // ASIGNACIÓN SIMPLE
        // x = 5
        // x = a + b * 3
        // ============================================================
        ASIGNACION(
            "^\\s*[A-Za-z][A-Za-z0-9_]*\\s*=\\s*.+$"
        ),

        // ============================================================
        // Console.WriteLine("texto")
        // Console.WriteLine(variable)
        // Console.WriteLine("Hola " & nombre)
        // ============================================================
        CONSOLE_WRITELINE(
            "^\\s*Console\\.WriteLine\\s*\\(.*\\)\\s*$"
        ),

        // ============================================================
        // CUALQUIER OTRA LÍNEA (para detección de errores)
        // ============================================================
        DESCONOCIDO(".*");

        public final String patron;

        Expresion(String patron) {
            this.patron = patron;
        }
    }
}

