package AnalizadorSintactico;

import AnalizadorLexico.Token;
import AnalizadorLexico.Tokens;
import AnalizadorLexico.Errors;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private final List<Errors> errores;
    private int currentTokenIndex;

    private int numRecursos = 0;
    private int numTareas = 0;

    // Constructor
    public Parser(List<Token> tokens, List<Errors> errores) {
        this.tokens = tokens;
        this.errores = errores;
        this.currentTokenIndex = 0;
    }

    // metodo principal para iniciar el analisis
    public void parse() {
        while (!isAtEnd()) {
            try {
                parseStatement(); // analiza una declaracin principal
            } catch (RuntimeException e) {
                errores.add(new Errors(
                        getCurrentToken().getValor(),
                        e.getMessage(),
                        getCurrentToken().getLine(),
                        getCurrentToken().getColumn()
                ));
                sincronizar(); // intenta continuar despues del error
            }
        }
    }

    // analiza una declaración principal
    private void parseStatement() {
        if (check(Tokens.PALABRA_CLAVE)) {
            String value = getCurrentToken().getValor();
            switch (value) {
                case "ASIGNACION" -> parseAsignacion();               
                default -> throw new RuntimeException("Palabra clave no reconocida: " + value);
            }
        } else {
            throw new RuntimeException("Se esperaba una palabra clave (ASIGNACION)");
        }
    }

    // Analiza una declaracion de asignacion
    private void parseAsignacion() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'ASIGNACION'");
        consume(Tokens.OPEN_BRACE, "Se esperaba '{' despues de 'ASIGNACION'");

        while (!check(Tokens.CLOSE_BRACE)) {
            if (check(Tokens.PALABRA_CLAVE)) {
                String value = getCurrentToken().getValor();
                switch (value) {
                    case "RECURSOS" -> parseRecursos();
                    case "TAREAS" -> parseTareas();
                    case "COSTOS" -> parseCostos();
                    case "MINIMIZAR", "MAXIMIZAR" -> parseObjetivo();
                    default -> throw new RuntimeException("Palabra clave no reconocida dentro de 'ASIGNACION': " + value);
                }
            } else {
                throw new RuntimeException("Se esperaba una palabra clave dentro de 'ASIGNACION'");
            }
        }

        consume(Tokens.CLOSE_BRACE, "Se esperaba '}' para cerrar 'ASIGNACION'");
    }

    // analiza la lista de recursos
    private void parseRecursos() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'RECURSOS'");
        consume(Tokens.ASIGNACION, "Se esperaba '=' despues de 'RECURSOS'");
        numRecursos = parseListaIdentificadores();
        consume(Tokens.SEMICOLON, "Se esperaba ';' despues de 'RECURSOS'");
    }

    // analiza la lista de tareas
    private void parseTareas() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'TAREAS'");
        consume(Tokens.ASIGNACION, "Se esperaba '=' despues de 'TAREAS'");
        numTareas = parseListaIdentificadores();
        consume(Tokens.SEMICOLON, "Se esperaba ';' despues de 'TAREAS'");
    }

    // analiza las matrices de costos
    private void parseCostos() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'COSTOS'");
        consume(Tokens.ASIGNACION, "Se esperaba '=' despues de 'COSTOS'");
        int numMatrices = parseListaMatrices();
        consume(Tokens.SEMICOLON, "Se esperaba ';' despues de 'COSTOS'");

        // verificar que la matriz de costos sea cuadrada y consistente con recursos y tareas
        if (numMatrices != numRecursos) {
            throw new RuntimeException("El numero de filas en la matriz de costos debe ser igual al numero de recursos");
        }
    }

    // analiza la lista de identificadores y devuelve el conteo
    private int parseListaIdentificadores() {
        consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar la lista de identificadores");
        int count = 0;
        do {
            consume(Tokens.IDENTIFICADOR, "Se esperaba un identificador");
            count++;
        } while (match(Tokens.COMMA));
        consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar la lista de identificadores");
        return count;
    }

    // analiza una lista de matrices y devuelve el conteo de filas
    private int parseListaMatrices() {
        consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar las matrices");
        int count = 0;
        do {
            parseMatriz();
            count++;
        } while (match(Tokens.COMMA));
        consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar las matrices");
        return count;
    }

    // analiza una matriz
    private void parseMatriz() {
        consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar una matriz");
        int columnCount = 0;
        do {
            if (check(Tokens.NUMERO) || check(Tokens.DECIMAL)) {
                advance();
                columnCount++;
            } else {
                throw new RuntimeException("Se esperaba un numero dentro de la matriz");
            }
        } while (match(Tokens.COMMA));
        consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar la matriz");

        // verificar que el número de columnas sea igual al número de tareas
        if (columnCount != numTareas) {
            throw new RuntimeException("El numero de columnas en cada matriz de costos debe ser igual al numero de tareas");
        }
    }

    // analiza el objetivo MINIMIZAR o MAXIMIZAR
    private void parseObjetivo() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'MINIMIZAR' o 'MAXIMIZAR'");
        consume(Tokens.SEMICOLON, "Se esperaba ';' despued del objetivo");
    }

    

    // Manejo de errores y sincronización
    private void sincronizar() {
        advance(); // Avanza al siguiente token
        while (!isAtEnd()) {
            if (getPreviousToken().getTipo() == Tokens.SEMICOLON) {
                return;
            }

            switch (getCurrentToken().getTipo()) {
                case PALABRA_CLAVE, IDENTIFICADOR -> {
                    return;
                }
            }
            advance();
        }
    }

    //metodos de ayuda
    private Token advance() {
        if (!isAtEnd()) {
            currentTokenIndex++;
        }
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
        if (isAtEnd()) {
            return false;
        }
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

    private Token getCurrentToken() {
        return tokens.get(currentTokenIndex);
    }

    private Token getPreviousToken() {
        return tokens.get(currentTokenIndex - 1);
    }
}
