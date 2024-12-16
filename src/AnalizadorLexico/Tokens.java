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
    //KEYWORD_IF(""),
    //KEYWORD_WHILE("while"),
    PALABRA_CLAVE("HUNGARO|VOGEL|ESQNOROESTE|CRUCEARROYO|RECURSOS|TAREAS|COSTOS|MINIMIZAR|MAXIMIZAR|FUENTES|DESTINOS|OFERTA|DEMANDA|RESOLVER"),

    // Identificadores y valores
    IDENTIFICADOR("[a-zA-Z_][a-zA-Z0-9_]*"), // Identificadores
    DECIMAL("\\d+\\.\\d+"),                  // Números decimales
    NUMERO("\\d+"),                          // Números enteros
    COMA(","),                              // Coma


    // Operadores
    COMPARACION(">=|<=|==|!="),              // Operadores relacionales largos
    RELACIONALES("[><]"),                    // Operadores relacionales simples
    ASIGNACION("="),                         // Asignación
    ARITMETICA("[+\\-*/%]"),                 // Operadores aritméticos

    // Símbolos especiales
    SEMICOLON(";"),                          // Punto y coma
    OPEN_BRACE("\\{"),  // {                 // llave de apertura
    CLOSE_BRACE("\\}"), // }                 // llave de cierre
    OPEN_BRACKET("\\["), // [                // corchete de apertura
    CLOSE_BRACKET("\\]"), // ]               // corchete de cierre
    OPEN_PAREN("\\("),                       // Paréntesis de apertura
    CLOSE_PAREN("\\)"),                      // Paréntesis de cierre
   
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

