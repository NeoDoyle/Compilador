package AnalizadorSintactico;

import AnalizadorLexico.Token;
import AnalizadorLexico.Tokens;
import AnalizadorLexico.Errores;
import java.util.List;


public class Parser {

    private final List<Token> tokens;
    private final List<Errores> errores;
    private int currentTokenIndex;

    private int numRecursos = 0;
    private int numTareas = 0;
    private int numFuentes = 0;
    private int numDestinos = 0;
    private int numOfertas = 0;
    private int numDemandas = 0;
    
    private boolean recursosPresentes = false;
    private boolean tareasPresentes = false;
    private boolean costosPresentes = false;
    private boolean objetivoPresente = false;
    
    private boolean fuentesPresentes = false;
    private boolean destinosPresentes = false;
    private boolean ofertaPresente = false;
    private boolean demandaPresente = false;
    private String metodoActual = "";

    private int ultimoElementoProcesado = 0; // 1: Recursos, 2: Tareas, 3: Costos, 4: Objetivo

    // Constructor
    public Parser(List<Token> tokens, List<Errores> errores) {
        this.tokens = tokens;
        this.errores = errores;
        this.currentTokenIndex = 0;
    }

    // Metodo principal para iniciar el analisis
    public void parse() {
    while (!isAtEnd()) {
        try {
            parseStatement(); // Analiza una declaración principal, como HUNGARO, VOGEL, etc.
        } catch (RuntimeException e) {
            errores.add(new Errores(
                    getCurrentToken().getValor(),
                    e.getMessage(),
                    getCurrentToken().getLine(),
                    getCurrentToken().getColumn()
            ));
            sincronizar(); // Intenta continuar después del error
        }
    }

    // Verificar que todos los elementos estén presentes al final del análisis
    try {
        if (metodoActual != null) { // Solo verifica si hay un método procesándose
            verificarElementosFaltantes(metodoActual);
        }
    } catch (RuntimeException e) {
        errores.add(new Errores(
                "ANALISIS FINAL",
                e.getMessage(),
                -1,
                -1
        ));
    }
}


    // Analiza una declaración principal
    private void parseStatement() {
    if (check(Tokens.PALABRA_CLAVE) && getCurrentToken().getValor().equals("HUNGARO")) {
        metodoActual = "HUNGARO";
        inicializarEstado(); // Restablece el estado de los elementos requeridos
        parseHungaro();
    } else if (check(Tokens.PALABRA_CLAVE) && getCurrentToken().getValor().equals("VOGEL")) {
        metodoActual = "VOGEL";
        inicializarEstado(); // Restablece el estado de los elementos requeridos
        parseVogel();
    } else if (check(Tokens.PALABRA_CLAVE) && getCurrentToken().getValor().equals("ESQNOROESTE")) {
        metodoActual = "ESQNOROESTE";
        inicializarEstado(); // Restablece el estado de los elementos requeridos
        parseEsqNoroeste();
    } else if (check(Tokens.PALABRA_CLAVE) && getCurrentToken().getValor().equals("CRUCEARROYO")) {
        metodoActual = "CRUCEARROYO";
        inicializarEstado(); // Restablece el estado de los elementos requeridos
        parseCruceArroyo();
    } else {
        throw new RuntimeException("Método no reconocido: " + getCurrentToken().getValor());
    }
}


    // Analiza una declaración de asignación
    private void parseHungaro() {
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'HUNGARO'");
    consume(Tokens.OPEN_BRACE, "Se esperaba '{' después de 'HUNGARO'");

    while (!check(Tokens.CLOSE_BRACE)) {
        if (check(Tokens.PALABRA_CLAVE)) {
            String value = getCurrentToken().getValor();
            switch (value) {
                case "RECURSOS" -> {
                    verificarOrden(1, "HUNGARO");
                    parseRecursos();
                    recursosPresentes = true;
                }
                case "TAREAS" -> {
                    if (!recursosPresentes) {
                        errores.add(new Errores(
                            "TAREAS",
                            "Se esperaba 'RECURSOS' antes de 'TAREAS'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                    }
                    verificarOrden(2, "HUNGARO");
                    parseTareas();
                    tareasPresentes = true;
                }
                case "COSTOS" -> {
                    if (!tareasPresentes) {
                        errores.add(new Errores(
                            "COSTOS",
                            "Se esperaba 'TAREAS' antes de 'COSTOS'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                    }
                    verificarOrden(3, "HUNGARO");
                    parseCostos("HUNGARO");
                    costosPresentes = true;
                }
                case "MINIMIZAR", "MAXIMIZAR" -> {
                    if (!costosPresentes) {
                        errores.add(new Errores(
                            "OBJETIVO",
                            "Se esperaba 'COSTOS' antes del objetivo (MINIMIZAR o MAXIMIZAR)",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                    }
                    verificarOrden(4, "HUNGARO");
                    parseObjetivo();
                    objetivoPresente = true;
                }
                default -> throw new RuntimeException("Palabra clave no reconocida dentro de 'HUNGARO': " + value);
            }
        } else {
            throw new RuntimeException("Se esperaba una palabra clave dentro de 'HUNGARO'");
        }
    }

    consume(Tokens.CLOSE_BRACE, "Se esperaba '}' para cerrar 'HUNGARO'");
}
    
     private void parseVogel() {
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'VOGEL'");
    consume(Tokens.OPEN_BRACE, "Se esperaba '{' después de 'VOGEL'");

    while (!check(Tokens.CLOSE_BRACE)) {
        if (check(Tokens.PALABRA_CLAVE)) {
            String value = getCurrentToken().getValor();
            switch (value) {
                case "RECURSOS" -> {
                    verificarOrden(1, "VOGEL");
                    parseRecursos();
                    recursosPresentes = true;
                }
                case "TAREAS" -> {
                    if (!recursosPresentes) {
                        errores.add(new Errores(
                            "TAREAS",
                            "Se esperaba 'RECURSOS' antes de 'TAREAS'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                    }
                    verificarOrden(2, "VOGEL");
                    parseTareas();
                    tareasPresentes = true;
                }
                case "COSTOS" -> {
                    if (!tareasPresentes) {
                        errores.add(new Errores(
                            "COSTOS",
                            "Se esperaba 'TAREAS' antes de 'COSTOS'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                    }
                    verificarOrden(3, "VOGEL");
                    parseCostos("VOGEL");
                    costosPresentes = true;
                }
                case "MINIMIZAR", "MAXIMIZAR" -> {
                    if (!costosPresentes) {
                        errores.add(new Errores(
                            "OBJETIVO",
                            "Se esperaba 'COSTOS' antes del objetivo (MINIMIZAR o MAXIMIZAR)",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                    }
                    verificarOrden(4, "VOGEL");
                    parseObjetivo();
                    objetivoPresente = true;
                }
                default -> throw new RuntimeException("Palabra clave no reconocida dentro de 'VOGEL': " + value);
            }
        } else {
            throw new RuntimeException("Se esperaba una palabra clave dentro de 'VOGEL'");
        }
    }

    consume(Tokens.CLOSE_BRACE, "Se esperaba '}' para cerrar 'VOGEL'");
}

    private void parseEsqNoroeste() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'ESQNOROESTE'");
        consume(Tokens.OPEN_BRACE, "Se esperaba '{' después de 'ESQNOROESTE'");

        while (!check(Tokens.CLOSE_BRACE)) {
            if (check(Tokens.PALABRA_CLAVE)) {
                String value = getCurrentToken().getValor();
                switch (value) {
                    case "FUENTES" -> {
                        verificarOrden(1, "ESQNOROESTE");
                        parseFuentes();
                        fuentesPresentes = true;
                    }
                    case "DESTINOS" -> {
                        if (!fuentesPresentes) {
                        errores.add(new Errores(
                            "DESTINOS",
                            "Se esperaba 'FUENTES' antes de 'DESTINOS'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                        }
                        verificarOrden(2, "ESQNOROESTE");
                        parseDestinos();
                        destinosPresentes = true;
                    }
                    case "OFERTA" -> {
                        if (!destinosPresentes) {
                        errores.add(new Errores(
                            "OFERTA",
                            "Se esperaba 'DESTINOS' antes de 'OFERTA'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                        }
                        verificarOrden(3, "ESQNOROESTE");
                        parseOferta();
                        ofertaPresente = true;
                    }
                    case "DEMANDA" -> {
                        if (!ofertaPresente) {
                        errores.add(new Errores(
                            "DEMANDA",
                            "Se esperaba 'OFERTA' antes de 'DEMANDA'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                        }
                        verificarOrden(4, "ESQNOROESTE");
                        parseDemanda();
                        demandaPresente = true;
                    }
                    case "COSTOS" -> {
                        if (!demandaPresente) {
                        errores.add(new Errores(
                            "COSTOS",
                            "Se esperaba 'DEMANDA' antes de 'COSTOS'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                        }
                        verificarOrden(5, "ESQNOROESTE");
                        parseCostos("ESQNOROESTE");
                        costosPresentes = true;
                    }
                    default -> throw new RuntimeException("Palabra clave no reconocida dentro de 'ESQNOROESTE': " + value);
                }
            } else {
                throw new RuntimeException("Se esperaba una palabra clave dentro de 'ESQNOROESTE'");
            }
        }

        consume(Tokens.CLOSE_BRACE, "Se esperaba '}' para cerrar 'ESQNOROESTE'");
    }
    
        private void parseCruceArroyo() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'CRUCEARROYO'");
        consume(Tokens.OPEN_BRACE, "Se esperaba '{' después de 'CRUCEARROYO'");

        while (!check(Tokens.CLOSE_BRACE)) {
            if (check(Tokens.PALABRA_CLAVE)) {
                String value = getCurrentToken().getValor();
                switch (value) {
                    case "FUENTES" -> {
                        verificarOrden(1, "CRUCEARROYO");
                        parseFuentes();
                        fuentesPresentes = true;
                    }
                    case "DESTINOS" -> {
                        if (!fuentesPresentes) {
                        errores.add(new Errores(
                            "DESTINOS",
                            "Se esperaba 'FUENTES' antes de 'DESTINOS'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                        }
                        verificarOrden(2, "CRUCEARROYO");
                        parseDestinos();
                        destinosPresentes = true;
                    }
                    case "OFERTA" -> {
                        if (!destinosPresentes) {
                        errores.add(new Errores(
                            "OFERTA",
                            "Se esperaba 'DESTINOS' antes de 'OFERTA'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                        }
                        verificarOrden(3, "CRUCEARROYO");
                        parseOferta();
                        ofertaPresente = true;
                    }
                    case "DEMANDA" -> {
                        if (!ofertaPresente) {
                        errores.add(new Errores(
                            "DEMANDA",
                            "Se esperaba 'OFERTA' antes de 'DEMANDA'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                        }
                        verificarOrden(4, "CRUCEARROYO");
                        parseDemanda();
                        demandaPresente = true;
                    }
                    case "COSTOS" -> {
                        if (!demandaPresente) {
                        errores.add(new Errores(
                            "COSTOS",
                            "Se esperaba 'DEMANDA' antes de 'COSTOS'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                        }
                        verificarOrden(5, "CRUCEARROYO");
                        parseCostos("CRUCEARROYO");
                        costosPresentes = true;
                    }
                    default -> throw new RuntimeException("Palabra clave no reconocida dentro de 'CRUCEARROYO': " + value);
                }
            } else {
                throw new RuntimeException("Se esperaba una palabra clave dentro de 'CRUCEARROYO'");
            }
        }

        consume(Tokens.CLOSE_BRACE, "Se esperaba '}' para cerrar 'CRUCEARROYO'");
    }
    
    // Verifica si algún elemento obligatorio falta
    private void verificarElementosFaltantes(String metodo) {
    StringBuilder faltantes = new StringBuilder();

    switch (metodo) {
        case "HUNGARO", "VOGEL" -> {
            if (!recursosPresentes) faltantes.append("RECURSOS, ");
            if (!tareasPresentes) faltantes.append("TAREAS, ");
            if (!costosPresentes) faltantes.append("COSTOS, ");
            if (!objetivoPresente) faltantes.append("OBJETIVO (MINIMIZAR o MAXIMIZAR), ");
        }
        case "ESQNOROESTE", "CRUCEARROYO" -> {
            if (!recursosPresentes) faltantes.append("FUENTES, ");
            if (!tareasPresentes) faltantes.append("DESTINOS, ");
            if (!ofertaPresente) faltantes.append("OFERTA, ");
            if (!demandaPresente) faltantes.append("DEMANDA, ");
            if (!costosPresentes) faltantes.append("COSTOS, ");
        }
        default -> throw new RuntimeException("Método desconocido al verificar elementos faltantes: " + metodo);
    }

    if (faltantes.length() > 0) {
        // Eliminar la última coma y espacio
        faltantes.setLength(faltantes.length() - 2);
        throw new RuntimeException("Faltan los siguientes elementos obligatorios en '" + metodo + "': " + faltantes);
    }
}


    // Verifica si los elementos están en el orden correcto
    private void verificarOrden(int elementoActual, String metodo) {
    if (elementoActual < ultimoElementoProcesado) {
        throw new RuntimeException(
            "El elemento actual está fuera de orden. Se esperaba un elemento después de: " 
            + getNombreElemento(ultimoElementoProcesado, metodo)
        );
    }
    ultimoElementoProcesado = elementoActual;
}

    private String getNombreElemento(int elemento, String metodo) {
    return switch (metodo) {
        case "HUNGARO", "VOGEL" -> switch (elemento) {
            case 1 -> "RECURSOS";
            case 2 -> "TAREAS";
            case 3 -> "COSTOS";
            case 4 -> "OBJETIVO";
            default -> "DESCONOCIDO";
        };
        case "ESQNOROESTE", "CRUCEARROYO" -> switch (elemento) {
            case 1 -> "FUENTES";
            case 2 -> "DESTINOS";
            case 3 -> "COSTOS";
            case 4 -> "OFERTA y DEMANDA";
            default -> "DESCONOCIDO";
        };
        default -> "DESCONOCIDO";
    };
}

    // Analiza la lista de recursos
    private void parseRecursos() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'RECURSOS'");
        consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'RECURSOS'");
        numRecursos = parseListaIdentificadores();
        consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'RECURSOS'");
    }

    // Analiza la lista de tareas
    private void parseTareas() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'TAREAS'");
        consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'TAREAS'");
        numTareas = parseListaIdentificadores();
        consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'TAREAS'");
    }

    // Analiza las matrices de costos
    private void parseCostos(String metodo) {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'COSTOS'");
        consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'COSTOS'");
        int numMatrices = parseListaMatrices(metodo);
        consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'COSTOS'");

        // Verificar que la matriz de costos sea cuadrada y consistente con recursos y tareas
        if (numMatrices != numRecursos) {
            throw new RuntimeException("El número de filas en la matriz de costos debe ser igual al número de recursos");
        }
    }

    // Analiza la lista de identificadores y devuelve el conteo
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

    // Analiza una lista de matrices y devuelve el conteo de filas
    private int parseListaMatrices(String metodo) {
        consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar las matrices");
        int count = 0;
        do {
            parseMatriz();
            count++;
        } while (match(Tokens.COMMA));
        consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar las matrices");
        return count;
    }
    
    // Analiza una matriz
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

    // Analiza el objetivo MINIMIZAR o MAXIMIZAR
    private void parseObjetivo() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'MINIMIZAR' o 'MAXIMIZAR'");
        consume(Tokens.SEMICOLON, "Se esperaba ';' después del objetivo");
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
    
private void parseFuentes() {
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'FUENTES'");
    consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'FUENTES'");
    numFuentes = parseListaIdentificadores();
    consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'FUENTES'");
}

private void parseDestinos() {
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'DESTINOS'");
    consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'DESTINOS'");
    numDestinos = parseListaIdentificadores();
    consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'DESTINOS'");
}

private void parseOferta() {
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'OFERTA'");
    consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'OFERTA'");
    parseListaNumerica();
    consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'OFERTA'");
}

private void parseDemanda() {
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'DEMANDA'");
    consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'DEMANDA'");
    parseListaNumerica();
    consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'DEMANDA'");
}

// Analiza una lista de valores numéricos y devuelve el conteo
private int parseListaNumerica() {
    consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar la lista de valores numéricos");
    int count = 0;
    do {
        if (check(Tokens.NUMERO) || check(Tokens.DECIMAL)) {
            advance(); // Avanza si el token es un número válido (entero o decimal)
            count++;
        } else {
            throw new RuntimeException("Se esperaba un valor numérico (entero o decimal)");
        }
    } while (match(Tokens.COMMA)); // Permite múltiples valores separados por comas
    consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar la lista de valores numéricos");
    return count;
}

    // Métodos de ayuda
    private void inicializarEstado() {
        recursosPresentes = false;
        tareasPresentes = false;
        costosPresentes = false;
        objetivoPresente = false;
        fuentesPresentes = false;
        destinosPresentes = false;
        ofertaPresente = false;
        demandaPresente = false;
        ultimoElementoProcesado = 0; // Restablece el control del orden
    }

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
