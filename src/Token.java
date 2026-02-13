/*
UNED Informática Compiladores 3307
Estudiante: Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase que representa un token generado por el Lexer
*/

public class Token {

    public final String lexema;        // Texto exacto del token
    public final TokenType.Type type;  // Tipo del token según el enum TokenType

    public Token(String lexema, TokenType.Type type) {
        this.lexema = lexema;
        this.type = type;
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    /**
     * Compara si el token es de un tipo específico.
     */
    public boolean es(TokenType.Type tipo) {
        return this.type == tipo;
    }

    /**
     * Compara tipo y lexema simultáneamente.
     * Útil para casos como:
     *   token.es("RESERVED_WORD", "Module")
     */
    public boolean es(String tipo, String valor) {
        return this.type.name().equalsIgnoreCase(tipo)
                && this.lexema.equalsIgnoreCase(valor);
    }

    /**
     * Representación legible del token.
     */
    @Override
    public String toString() {
        return "[" + type + " : \"" + lexema + "\"]";
    }
}
