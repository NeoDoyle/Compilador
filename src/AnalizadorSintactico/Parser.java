/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorSintactico;


import AnalizadorLexico.Token;
import AnalizadorLexico.Tokens;
import AnalizadorLexico.Errors;
import java.util.List;

/**
 *
 * @author otvam
 */
public class Parser {
    private final List<Token> tokens;
    private final List<Errors> errores;
    private int currentTokenIndex;

    // Constructor
    public Parser(List<Token> tokens, List<Errors> errores) {
        this.tokens = tokens;
        this.errores = errores;
        this.currentTokenIndex = 0;
    }

    // Método principal para iniciar el análisis
    public void parse() {
        while (!isAtEnd()) {
            try {
                parseStatement(); // Analiza una declaración
            } catch (Exception e) {
                errores.add(new Errors(
                        getCurrentToken().getValor(),
                        e.getMessage(),
                        getCurrentToken().getLine(),
                        getCurrentToken().getColumn()
                ));
                advance(); // Continúa con el siguiente token para evitar un bucle infinito
            }
        }
    }

    // Ejemplo: Análisis de una declaración
    private void parseStatement() {
    if (match(Tokens.KEYWORD_IF)) {
        parseIfStatement();
    } else if (match(Tokens.KEYWORD_WHILE)) {
        parseWhileStatement();
    } else if (check(Tokens.IDENTIFICADOR)) {
        parseAssignment();
    } else {
        throw new RuntimeException("Se esperaba una declaración válida.");
    }

    // Asegúrate de avanzar al menos un token para evitar bucles infinitos
    if (!isAtEnd()) {
        advance();
    }
}




    // Método para analizar una declaración `if`
    private void parseIfStatement() {
    consume(Tokens.KEYWORD_IF, "Se esperaba 'if'.");
    consume(Tokens.OPEN_PAREN, "Se esperaba '(' después de 'if'.");
    if (isAtEnd()) throw new RuntimeException("Faltan tokens para la condición del 'if'.");

    parseExpression(); // Procesa la condición
    consume(Tokens.CLOSE_PAREN, "Se esperaba ')' después de la condición.");

    if (!check(Tokens.OPEN_BRACE)) throw new RuntimeException("Se esperaba '{' para abrir el bloque del 'if'.");
    parseBlock();
}



    // Método para analizar una declaración `while`
    private void parseWhileStatement() {
    consume(Tokens.KEYWORD_WHILE, "Se esperaba 'while'.");
    consume(Tokens.OPEN_PAREN, "Se esperaba '(' después de 'while'.");
    parseExpression(); // Analiza la condición dentro de los paréntesis
    consume(Tokens.CLOSE_PAREN, "Se esperaba ')' después de la condición.");
    parseBlock(); // Analiza el bloque asociado al 'while'
}

    // Método para analizar una asignación
private void parseAssignment() {
    Token currentToken = getCurrentToken();

    if (currentToken.getTipo() == Tokens.IDENTIFICADOR
        && tokens.get(currentTokenIndex + 1).getTipo() == Tokens.ASIGNACION
        && tokens.get(currentTokenIndex + 2).getTipo() == Tokens.NUMERO
        && tokens.get(currentTokenIndex + 3).getTipo() == Tokens.SEMICOLON) {
        // Regla satisfecha
        currentTokenIndex += 4; // Avanzar al siguiente token
    } else {
        if (!isAtEnd()) {
    errores.add(new Errors(
        getCurrentToken().getValor(),
        "Mensaje de error",
        getCurrentToken().getLine(),
        getCurrentToken().getColumn()
    ));
}
    }
}

    // Método para analizar una expresión (simplificado)
    private void parseExpression() {
    if (match(Tokens.IDENTIFICADOR)) {
        if (match(Tokens.ASIGNACION)) {
            parseExpression(); // Procesa la asignación
        }
    } else if (match(Tokens.NUMERO)) {
        advance(); // Consume un número
    } else {
        throw new RuntimeException("Expresión no válida.");
    }
}


    // Método para analizar un bloque de código (delimitado por llaves)
    private void parseBlock() {
    if (isAtEnd()) {
        throw new RuntimeException("Faltan tokens antes del bloque.");
    }

    consume(Tokens.OPEN_BRACE, "Se esperaba '{' para abrir el bloque de código.");
    while (!check(Tokens.CLOSE_BRACE) && !isAtEnd()) {
        parseStatement(); // Procesa declaraciones dentro del bloque
    }
    consume(Tokens.CLOSE_BRACE, "Se esperaba '}' para cerrar el bloque de código.");
}

    // Utilidades para manejo de tokens
    private Token advance() {
    if (!isAtEnd()) currentTokenIndex++;
    System.out.println("Avanzando al token: " + currentTokenIndex + " de " + tokens.size());
    return getPreviousToken();
}

    private boolean match(Tokens expected) {
        if (check(expected)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(Tokens expected) {
        if (isAtEnd()) return false;
        return getCurrentToken().getTipo() == expected;
    }

    private void consume(Tokens expected, String errorMessage) {
        if (check(expected)) {
            advance();
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    private boolean isAtEnd() {
        return currentTokenIndex >= tokens.size();
    }

    private void parseTerm() {
    if (match(Tokens.NUMERO) || match(Tokens.IDENTIFICADOR)) {
        advance(); // Consume un número o identificador
    } else {
        throw new RuntimeException("Se esperaba un número o un identificador.");
    }
}
    
    private Token getCurrentToken() {
    if (isAtEnd()) {
        System.err.println("Índice fuera de límites: " + currentTokenIndex + ", tamaño: " + tokens.size());
        throw new RuntimeException("Se intentó acceder a un token fuera del rango permitido.");
    }
    return tokens.get(currentTokenIndex);
}

    private Token getPreviousToken() {
        return tokens.get(currentTokenIndex - 1);
    }
}