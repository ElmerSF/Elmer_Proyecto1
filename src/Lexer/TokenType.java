package Lexer;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author elmer
 */
public class TokenType {
    public enum Type {

    // ============================================================
    // PALABRAS RESERVADAS
    // ============================================================
    MODULE("^MODULE$"),
    END("^END$"),
    SUB("^SUB$"),
    FUNCTION("^FUNCTION$"),
    DIM("^DIM$"),
    AS("^AS$"),
    IF("^IF$"),
    THEN("^THEN$"),
    ELSEIF("^ELSEIF$"),
    ELSE("^ELSE$"),
    RETURN("^RETURN$"),
    WHILE("^WHILE$"),
    IMPORTS("^IMPORTS$"),

    // Tipos de datos
    TYPE_INTEGER("^INTEGER$"),
    TYPE_STRING("^STRING$"),
    TYPE_BOOLEAN("^BOOLEAN$"),
    TYPE_BYTE("^BYTE$"),

    // ============================================================
    // IDENTIFICADORES Y LITERALES
    // ============================================================
    IDENTIFIER("^[A-Za-z][A-Za-z0-9_]*$"),
    NUMBER("^[0-9]+(\\.[0-9]+)?$"),
    STRING_LITERAL("^\"([^\"\\\\]|\\\\.)*\"$"),

    // ============================================================
    // OPERADORES
    // ============================================================
    OP_ASSIGN("^=$"),
    OP_PLUS("^\\+$"),
    OP_MINUS("^-+$"),
    OP_MULT("^\\*$"),
    OP_DIV("^/$"),
    OP_CONCAT("^&$"),
    OP_INVALID("^==$"),   // patrón imposible


    // ============================================================
    // SÍMBOLOS
    // ============================================================
    PAREN_OPEN("^\\($"),
    PAREN_CLOSE("^\\)$"),
    COMMA("^,$"),

    // ============================================================
    // COMENTARIOS
    // ============================================================
    COMMENT("^'.*$"),

    // ============================================================
    // TOKENS INVÁLIDOS O DESCONOCIDOS
    // ============================================================
    UNKNOWN(".*");

    // ============================================================
    // Campo patrón (regex)
    // ============================================================
    public final String patron;

    Type(String patron) {
        this.patron = patron;
    }
}

}
