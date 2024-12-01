/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package AnalizadorLexico;

/**
 *
 * @author hp
 */
public enum Tokens {
    // Palabras clave específicas
    KEYWORD_IF("if"),
    KEYWORD_WHILE("while"),

    // Identificadores y valores
    IDENTIFICADOR("[a-zA-Z_][a-zA-Z0-9_]*"), // Identificadores
    DECIMAL("\\d+\\.\\d+"),                  // Números decimales
    NUMERO("\\d+"),                          // Números enteros

    // Operadores
    COMPARACION(">=|<=|==|!="),              // Operadores relacionales largos
    RELACIONALES("[><]"),                    // Operadores relacionales simples
    ASIGNACION("="),                         // Asignación
    ARITMETICA("[+\\-*/%]"),                 // Operadores aritméticos

    // Símbolos especiales
    SEMICOLON(";"),                          // Punto y coma
    OPEN_PAREN("\\("),                       // Paréntesis de apertura
    CLOSE_PAREN("\\)"),                      // Paréntesis de cierre
    OPEN_BRACE("\\{"),                       // Llave de apertura
    CLOSE_BRACE("\\}"),                      // Llave de cierre

    // Comentarios
    COMENTARIO("//.*$");

    private final String patron;

    Tokens(String patron) {
        this.patron = patron;
    }

    public String getPatron() {
        return patron;
    }
}


//PALABRA_CLAVE("ASIGNACION|RECURSOS|TAREAS|COSTOS|RESOLVER|MINIMIZAR|MAXIMIZAR|SOLVE"), 