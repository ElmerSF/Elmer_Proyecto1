/*
UNED Informática Compiladores 3307
Estudiante Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para validación de los tokens y reglas del lenguaje
*/

import java.util.List;

public class Validador {

    private final ErrorManager errorManager;
    private final SymbolTable symbolTable;

    // Control del orden lógico del archivo
    private boolean moduleEncontrado = false;

    // Control de Imports y Module
    private boolean importsEncontrado = false;
    private boolean moduleValidado = false;

    // Control de End Module
    private int cantidadEndModule = 0;
    private int lineaEndModule = -1;

    public Validador(ErrorManager errorManager, SymbolTable symbolTable) {
        this.errorManager = errorManager;
        this.symbolTable = symbolTable;
    }

    // ============================================================
    // MÉTODO PRINCIPAL DE VALIDACIÓN POR LÍNEA
    // ============================================================
    public void validarLinea(List<Token> tokens, String linea, int numeroLinea) {

        if (tokens == null || tokens.isEmpty()) {
            return;
        }

        Token primero = tokens.get(0);

        // 1. End Module primero
        if (esEndModule(tokens)) {
            validarEndModule(tokens, linea, numeroLinea);
            return;
        }

        // 2. Comentarios inválidos (comentario en medio de línea)
        String lineaTrim = linea.trim();
        if (!lineaTrim.startsWith("'") && linea.contains("'")) {
            errorManager.agregarError(ErrorCode.STRING_SIN_CERRAR, linea, numeroLinea);
            return;
        }

        // 3. Código después de un End Module válido
        if (cantidadEndModule > 0) {
            errorManager.agregarError(
                    ErrorCode.ENDMODULE_NO_ES_ULTIMA_LINEA,
                    linea,
                    numeroLinea
            );
            cantidadEndModule = 0;
            lineaEndModule = -1;
        }

        // 4. Imports
        if (primero.es(TokenType.Type.IMPORTS)) {
            importsEncontrado = true;
            return;
        }

        // 5. Module
        if (primero.es(TokenType.Type.MODULE)) {
            validarModule(tokens, linea, numeroLinea);
            return;
        }

        // Compatibilidad con tu lógica original
        if (primero.es(TokenType.Type.MODULE)) {
            moduleEncontrado = true;
            return;
        }

        // 6. Dim
        if (primero.es(TokenType.Type.DIM)) {

            if (!moduleEncontrado) {
                errorManager.agregarError(ErrorCode.DIM_ANTES_DE_MODULE, linea, numeroLinea);
                return;
            }

            validarDeclaracionDim(tokens, linea, numeroLinea);
            return;
        }

        // 7. Console.WriteLine
        if (esConsoleWriteLine(tokens)) {
            validarConsoleWriteLine(tokens, linea, numeroLinea);
        }
    }

    // ============================================================
    // END MODULE
    // ============================================================
    private boolean esEndModule(List<Token> tokens) {
        if (tokens.size() < 2) return false;

        return tokens.get(0).es(TokenType.Type.END) &&
               tokens.get(1).es(TokenType.Type.MODULE);
    }

    private void validarEndModule(List<Token> tokens, String linea, int numeroLinea) {

        int indexEnd = linea.indexOf("End");
        int indexModule = linea.indexOf("Module");

        if (indexModule - indexEnd != 4) {
            errorManager.agregarError(ErrorCode.ENDMODULE_ESPACIO_INCORRECTO, linea, numeroLinea);
            return;
        }

        if (tokens.size() > 2) {
            errorManager.agregarError(ErrorCode.ENDMODULE_TIENE_TOKENS_EXTRA, linea, numeroLinea);
            return;
        }

        cantidadEndModule++;
        lineaEndModule = numeroLinea;
    }

    public void validarFinDeArchivo(int ultimaLineaConContenido) {

        if (cantidadEndModule == 0) {
            errorManager.agregarError(
                    ErrorCode.ENDMODULE_NO_ES_ULTIMA_LINEA,
                    "Fin de archivo",
                    ultimaLineaConContenido
            );
            return;
        }

        if (cantidadEndModule > 1) {
            errorManager.agregarError(
                    ErrorCode.ENDMODULE_DUPLICADO,
                    "Fin de archivo",
                    lineaEndModule
            );
        }

        if (lineaEndModule != ultimaLineaConContenido) {
            errorManager.agregarError(
                    ErrorCode.ENDMODULE_NO_ES_ULTIMA_LINEA,
                    "Fin de archivo",
                    ultimaLineaConContenido
            );
        }
    }

    // ============================================================
    // MODULE
    // ============================================================
    private void validarModule(List<Token> tokens, String linea, int numeroLinea) {

        if (!importsEncontrado) {
            errorManager.agregarError(ErrorCode.MODULE_ANTES_DE_IMPORTS, linea, numeroLinea);
            return;
        }

        if (tokens.size() < 2) {
            errorManager.agregarError(ErrorCode.MODULE_SIN_IDENTIFICADOR, linea, numeroLinea);
            return;
        }

        Token identificador = tokens.get(1);

        if (!identificador.es(TokenType.Type.IDENTIFIER)) {
            errorManager.agregarError(ErrorCode.MODULE_SIN_IDENTIFICADOR, linea, numeroLinea);
            return;
        }

        int indexModule = linea.indexOf("Module");
        int indexIdent = linea.indexOf(identificador.lexema);

        if (indexIdent - indexModule != 7) {
            errorManager.agregarError(ErrorCode.MODULE_ESPACIO_INCORRECTO, linea, numeroLinea);
            return;
        }

        moduleValidado = true;
        moduleEncontrado = true;
    }

    // ============================================================
    // Console.WriteLine
    // ============================================================
    private boolean esConsoleWriteLine(List<Token> tokens) {

        if (tokens.size() < 3) {
            return false;
        }

        return tokens.get(0).es(TokenType.Type.IDENTIFIER) &&
               tokens.get(0).lexema.equalsIgnoreCase("Console") &&
               tokens.get(1).lexema.equals(".") &&
               tokens.get(2).es(TokenType.Type.IDENTIFIER) &&
               tokens.get(2).lexema.equalsIgnoreCase("WriteLine");
    }

    private void validarConsoleWriteLine(List<Token> tokens, String linea, int numeroLinea) {

        // 1. Strings sin cerrar o comillas Unicode
        for (int i = 3; i < tokens.size(); i++) {
            Token t = tokens.get(i);

            if (t.es(TokenType.Type.STRING_LITERAL)) {
                if (!t.lexema.startsWith("\"") || !t.lexema.endsWith("\"")) {
                    errorManager.agregarError(ErrorCode.STRING_SIN_CERRAR, linea, numeroLinea);
                    return;
                }
            }

            if (t.lexema.equals("“") || t.lexema.equals("”")) {
                errorManager.agregarError(ErrorCode.STRING_SIN_CERRAR, linea, numeroLinea);
                return;
            }
        }

        // 2. Paréntesis de cierre
        Token ultimo = tokens.get(tokens.size() - 1);

        if (!ultimo.es(TokenType.Type.PAREN_CLOSE)) {
            errorManager.agregarError(ErrorCode.PARENTESIS_FALTANTE, linea, numeroLinea);
            return;
        }

        // 3. Paréntesis vacíos
        if (tokens.size() == 5) {
            errorManager.agregarError(ErrorCode.PARENTESIS_VACIOS, linea, numeroLinea);
            return;
        }

        // 4. Strings sin cerrar dentro de los paréntesis
        for (int i = 4; i < tokens.size() - 1; i++) {
            Token t = tokens.get(i);

            if (t.es(TokenType.Type.STRING_LITERAL)) {
                if (!t.lexema.startsWith("\"") || !t.lexema.endsWith("\"")) {
                    errorManager.agregarError(ErrorCode.STRING_SIN_CERRAR, linea, numeroLinea);
                    return;
                }
            }
        }
    }

    // ============================================================
    // Declaraciones Dim
    // ============================================================
    private void validarDeclaracionDim(List<Token> tokens, String linea, int numeroLinea) {

        if (tokens.size() < 4) {
            errorManager.agregarError(ErrorCode.DECLARACION_INCOMPLETA, linea, numeroLinea);
            return;
        }

        Token identificador = tokens.get(1);

        // Identificador inválido por tipo
        if (!identificador.es(TokenType.Type.IDENTIFIER)) {

            String lex = identificador.lexema;

            if (lex.startsWith("_")) {
                errorManager.agregarError(ErrorCode.IDENTIFICADOR_INICIA_CON_GUION_BAJO, linea, numeroLinea);

            } else if (lex.matches("^[0-9].*")) {
                errorManager.agregarError(ErrorCode.IDENTIFICADOR_INICIA_CON_NUMERO, linea, numeroLinea);

            } else {
                errorManager.agregarError(ErrorCode.IDENTIFICADOR_INVALIDO, linea, numeroLinea);
            }
        }

        Token asToken = tokens.get(2);

        // Identificador con espacios (ej: "mi var As Integer")
        if (asToken.es(TokenType.Type.IDENTIFIER) &&
            tokens.size() > 3 &&
            tokens.get(3).es(TokenType.Type.AS)) {

            errorManager.agregarError(ErrorCode.IDENTIFICADOR_CON_ESPACIOS, linea, numeroLinea);
            return;
        }

        if (!asToken.es(TokenType.Type.AS)) {
            errorManager.agregarError(ErrorCode.FALTA_AS, linea, numeroLinea);
        }

        Token tipo = tokens.get(3);
        String tipoLex = tipo.lexema;

        boolean tipoEsValido = esTipoValido(tipoLex);

        if (!tipoEsValido) {
            errorManager.agregarError(ErrorCode.TIPO_INVALIDO, linea, numeroLinea);
            return;
        }

        // Registrar en tabla de símbolos
        symbolTable.registrar(identificador.lexema, tipoLex);

        // Asignación opcional
        if (tokens.size() > 4) {
            validarAsignacion(tokens, linea, numeroLinea, tipo);
        }

        boolean hayOperacion = false;

        for (int i = 5; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.es(TokenType.Type.OP_PLUS) ||
                t.es(TokenType.Type.OP_MINUS) ||
                t.es(TokenType.Type.OP_MULT) ||
                t.es(TokenType.Type.OP_DIV)) {
                hayOperacion = true;
                break;
            }
        }

        if (!hayOperacion && tokens.size() > 6) {
            errorManager.agregarError(ErrorCode.TOKENS_EXTRA, linea, numeroLinea);
        }
    }

    private boolean esTipoValido(String tipo) {
        // Versión incorrecta (Double no permitido)
        // return tipo.equalsIgnoreCase("Integer") ||
        //        tipo.equalsIgnoreCase("Double") ||
        //        tipo.equalsIgnoreCase("String") ||
        //        tipo.equalsIgnoreCase("Boolean");

        // Versión correcta según el proyecto
        return tipo.equalsIgnoreCase("Integer") ||
               tipo.equalsIgnoreCase("String") ||
               tipo.equalsIgnoreCase("Boolean") ||
               tipo.equalsIgnoreCase("Byte");

    }

    private void validarAsignacion(List<Token> tokens, String linea, int numeroLinea, Token tipoDeclarado) {

        if (!tokens.get(4).es(TokenType.Type.OP_ASSIGN)) {
            errorManager.agregarError(ErrorCode.FALTA_IGUAL, linea, numeroLinea);
            return;
        }

        if (tokens.size() < 6) {
            errorManager.agregarError(ErrorCode.FALTA_VALOR, linea, numeroLinea);
            return;
        }

        if (tokens.size() == 6) {
            validarCompatibilidad(tipoDeclarado, tokens.get(5), linea, numeroLinea);
            return;
        }

        validarOperacionMatematica(tokens, linea, numeroLinea, tipoDeclarado);
    }

    private void validarCompatibilidad(Token tipo, Token valor, String linea, int numeroLinea) {

        String t = tipo.lexema.toLowerCase();

        switch (t) {

            case "integer":
                if (!valor.es(TokenType.Type.NUMBER) || valor.lexema.contains(".")) {
                    errorManager.agregarError(ErrorCode.VALOR_NO_COMPATIBLE, linea, numeroLinea);
                }
                break;

            // Caso eliminado (Double no permitido por el proyecto)
            // case "double":
            //     if (!valor.es(TokenType.Type.NUMBER)) {
            //         errorManager.agregarError(ErrorCode.VALOR_NO_COMPATIBLE, linea, numeroLinea);
            //     }
            //     break;

            // Caso agregado para Byte
            case "byte":
                // Byte solo acepta enteros sin punto
                if (!valor.es(TokenType.Type.NUMBER) || valor.lexema.contains(".")) {
                    errorManager.agregarError(ErrorCode.VALOR_NO_COMPATIBLE, linea, numeroLinea);
                }
                break;


            case "string":
                if (!valor.es(TokenType.Type.STRING_LITERAL)) {
                    errorManager.agregarError(ErrorCode.VALOR_NO_COMPATIBLE, linea, numeroLinea);
                }
                break;

            case "boolean":
                if (!valor.lexema.equalsIgnoreCase("True") &&
                    !valor.lexema.equalsIgnoreCase("False")) {
                    errorManager.agregarError(ErrorCode.VALOR_NO_COMPATIBLE, linea, numeroLinea);
                }
                break;

            default:
                errorManager.agregarError(ErrorCode.TIPO_INVALIDO, linea, numeroLinea);
        }
    }

    private void validarOperacionMatematica(List<Token> tokens, String linea, int numeroLinea, Token tipoDeclarado) {

        for (int i = 5; i < tokens.size(); i++) {

            Token t = tokens.get(i);

            if (t.es(TokenType.Type.OP_PLUS) ||
                t.es(TokenType.Type.OP_MINUS) ||
                t.es(TokenType.Type.OP_MULT) ||
                t.es(TokenType.Type.OP_DIV)) {
                continue;
            }

            if (t.es(TokenType.Type.NUMBER)) {
                continue;
            }

            if (t.es(TokenType.Type.IDENTIFIER)) {

                if (!symbolTable.existe(t.lexema)) {
                    errorManager.agregarError(ErrorCode.VARIABLE_NO_DECLARADA, linea, numeroLinea);
                    continue;
                }

                String tipoVar = symbolTable.tipoDe(t.lexema);

                // Línea incorrecta (Double no permitido)
                // if (!tipoVar.equals("integer") && !tipoVar.equals("double")) {

                // Línea corregida
                if (!tipoVar.equals("integer") && !tipoVar.equals("byte")) {
                    errorManager.agregarError(ErrorCode.OPERANDO_NO_NUMERICO, linea, numeroLinea);
                }


                continue;
            }

            errorManager.agregarError(ErrorCode.OPERANDO_INVALIDO, linea, numeroLinea);
        }

        if (tipoDeclarado.lexema.equalsIgnoreCase("String") ||
            tipoDeclarado.lexema.equalsIgnoreCase("Boolean")) {

            errorManager.agregarError(ErrorCode.VALOR_NO_COMPATIBLE, linea, numeroLinea);
        }
    }
}
