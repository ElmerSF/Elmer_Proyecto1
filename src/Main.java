/*
UNED Informática Compiladores 3307
Estudiante: Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase principal donde se inicia

ASCII art basado en colecciones anónimas de:
ASCII Art Archive. (n.d.). ASCII Art Gallery. https://www.asciiart.eu/gallery
Modificado para uso en el Proyecto 1 de Compiladores (UNED).
*/

import java.util.List;

public class Main {

    // Manejo de errores global
    private static final ErrorManager errorManager = new ErrorManager();
    private static final String directorio = System.getProperty("user.dir");

    public static void main(String[] args) {

        // Encabezado
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        System.out.println("\t Analizador Léxico   Proyecto 1 UNED Estudiante: Elmer Salazar (3-426-158)");
        System.out.println("--------------------------------------------------------------------------------------------------------------\n");

        // Validar argumentos
        if (args.length == 0) {
            System.out.println("No se indicó ningún archivo como argumento.");
            return;
        }

        String archivo = args[0];

        if (!archivo.toLowerCase().endsWith(".vb")) {
            System.out.println("El archivo debe tener extensión .vb");
            return;
        }

        System.out.println("Ubicación actual: " + directorio);
        System.out.println("Archivo recibido: " + archivo + "\n");

        // ASCII ART
        System.out.println("\033[32m                          .-\"\"\"-.");
        System.out.println("\033[32m                         / .===. \\");
        System.out.println("\033[32m                         \\\\ 6 6 \\\\");
        System.out.println("\033[32m                         ( \\___/ )");
        System.out.println("\033[32m    _________________ooo__\\_____/_____________________");
        System.out.println("\033[32m   /                                                  \\");
        System.out.println("\033[36m   |ANALIZADOR LÉXICO   Proyecto 1 Compiladores 03307  |");
        System.out.println("\033[32m   \\______________________________ooo_________________/");
        System.out.println("\033[32m                         |  |  |");
        System.out.println("\033[32m                         |_ | _|");
        System.out.println("\033[32m                         |  |  |");
        System.out.println("\033[32m                         |__|__|");
        System.out.println("\033[32m                         /-'Y'-\\");
        System.out.println("\033[32m                        (__/ \\__)");
        System.out.println("\033[0m");

        // Infraestructura
        FileManager fm = new FileManager();
        Lexer lexer = new Lexer();
        SymbolTable symbolTable = new SymbolTable();
        Validador validador = new Validador(errorManager, symbolTable);

        // Leer archivo fuente
        String[] lineas = fm.leerArchivo(archivo);

        if (lineas == null) {
            System.out.println("No se pudo leer el archivo.");
            return;
        }
        if (lineas.length == 0) {
            System.out.println("El archivo está vacío.");
            return;
        }

        // Crear archivo .log con numeración de líneas
        String archivoLog = fm.crearArchivoLog(archivo, lineas);

        // Procesar línea por línea
        for (int i = 0; i < lineas.length; i++) {

            String linea = lineas[i];
            int numeroLinea = i + 1;

            // 1. Clasificar la línea completa con TabladeExpresiones
            TabladeExpresiones.Expresion tipoLinea = clasificarLinea(linea);

            // Línea vacía o comentario → no se valida más
            if (tipoLinea == TabladeExpresiones.Expresion.LINEA_VACIA ||
                tipoLinea == TabladeExpresiones.Expresion.COMENTARIO) {
                continue;
            }

            // 2. Tokenizar con Lexer (usando TokenType)
            List<Token> tokens = lexer.tokenizar(linea);

            // 3. Validar semántica/sintaxis con Validador
            validador.validarLinea(tokens, linea, numeroLinea);
        }

        // Validación al final del archivo (End Module, etc.)
        validador.validarFinDeArchivo(lineas.length);

        // Escribir errores al .log
        fm.escribirErrores(archivoLog, errorManager);

        System.out.println("\nAnálisis completado. Archivo log generado: " + archivoLog);
    }

    private static TabladeExpresiones.Expresion clasificarLinea(String linea) {
        for (TabladeExpresiones.Expresion exp : TabladeExpresiones.Expresion.values()) {
            if (linea.matches(exp.patron)) {
                return exp;
            }
        }
        return TabladeExpresiones.Expresion.DESCONOCIDO;
    }
}
