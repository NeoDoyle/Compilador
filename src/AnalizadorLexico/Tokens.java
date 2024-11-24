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
    NUMERO("\\d+"),                                          // Números (enteros)
    MATRIZ("\\[(\\s*\"[a-zA-Z0-9_]+\"\\s*,?\\s*)+\\]"),     // Matriz de cadenas (ej. ["Trabajador1", "Trabajador2"])
    MATRIZ_NUMERICA("\\[(\\s*\\d+\\s*,?\\s*)+\\]"),         // Matriz numérica (ej. [4, 8, 6])
    ASIGNACION("="),                                        // Símbolo de asignación (ej. x = 5)
    SIMBOLO_ESPECIAL("[\\[\\]\\{\\}\\,\\;]"),               // Símbolos especiales (corchetes, llaves, comas y punto y coma)
    COMENTARIO("//.*$");                                  // Comentarios (cualquier cosa después de //)
    private final String patron;

    Tokens(String patron) {
        this.patron = patron;
    }

    public String getPatron() {
        return patron;
    }
}