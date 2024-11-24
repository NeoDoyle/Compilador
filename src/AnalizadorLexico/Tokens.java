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
     PALABRA_CLAVE("ASIGNACION|RECURSOS|TAREAS|COSTOS|RESOLVER|MINIMIZAR|MAXIMIZAR|SOLVE"),  // Palabras clave
    IDENTIFICADOR("[a-zA-Z_][a-zA-Z0-9_]*"),                 // Identificadores (nombres de variables)
    DECIMAL("\\d+\\.\\d+"),                                  // Números decimales (ej. 42.5)
    NUMERO("\\d+"),                                          // Números enteros (ej. 42)
    ASIGNACION("="),                                         // Símbolo de asignación (ej. x = 5)
    SIMBOLO_ESPECIAL("[\\[\\]\\{\\}\\,\\;]"),                // Símbolos especiales (corchetes, llaves, comas y punto y coma)
    COMENTARIO("//.*$");                                     // Comentarios (cualquier cosa después de //)

    
    private final String patron;

    Tokens(String patron) {
        this.patron = patron;
    }

    public String getPatron() {
        return patron;
    }
}//tokens