package AnalizadorSintactico;

import AnalizadorLexico.Token;
import AnalizadorLexico.Tokens;
import AnalizadorLexico.Errores;
import compilador.TablaSimbolosFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

public class Parser {

    private final List<Token> tokens;
    private final List<Errores> errores;
    private TablaSimbolosFrame tablaSimbolos;
    private int currentTokenIndex;

    private int numRecursos = 0;
    private int numTareas = 0;
    private int numFuentes = 0;
    private int numDestinos = 0;
    private int numOfertas = 0;
    private int numDemandas = 0;
    private int numMatrices = 0;
    
    private boolean recursosPresentes = false;
    private boolean tareasPresentes = false;
    private boolean costosPresentes = false;
    private boolean objetivoPresente = false;
    private boolean fuentesPresentes = false;
    private boolean destinosPresentes = false;
    private boolean ofertaPresente = false;
    private boolean demandaPresente = false;
    private boolean resolverPresente = false;
    private boolean primerCiclo = true;
    private String metodoActual = "";

    private int ultimoElementoProcesado = 0; // 1: Recursos, 2: Tareas, 3: Costos, 4: Objetivo

    // Constructor
    public Parser(List<Token> tokens, List<Errores> errores, TablaSimbolosFrame tablaSimbolos) {
        this.tokens = tokens;
        this.errores = errores;
        this.currentTokenIndex = 0;
        this.tablaSimbolos = tablaSimbolos;
    }

    // Metodo principal para iniciar el analisis
    public void parse() {
    while (!isAtEnd()) {
        try {
            if (primerCiclo == true) {
                parseMetodo(); // Analiza la primera declaración principal (HUNGARO, VOGEL, etc.)
            } else {
                System.out.println("Entrando al bloque después de error");
                printDebugInfo();
                if(!isAtEnd()){
                    switch(metodoActual){
                        case "HUNGARO" -> {
                            System.out.println("Volviendo al método" + metodoActual + "después de error");
                            parseHungaro();
                        }
                        case "VOGEL" -> {
                            System.out.println("Volviendo al método" + metodoActual + "después de error");
                            parseVogel();
                        }
                        case "ESQNOROESTE" -> {
                            System.out.println("Volviendo al método" + metodoActual + "después de error");
                            parseEsqNoroeste();
                        }
                        case "CRUCEARROYO" -> {
                            System.out.println("Volviendo al método" + metodoActual + "después de error");
                            parseCruceArroyo();
                        }
                        default -> throw new RuntimeException("Método no reconocido");
                    }
                }
                
            }
        } catch (RuntimeException e) {
            if(!isAtEnd()){
                errores.add(new Errores(
                    getCurrentToken().getValor(),
                    e.getMessage(),
                    getCurrentToken().getLine(),
                    getCurrentToken().getColumn()
                ));

              sincronizar(); // Intenta continuar después del error
            }else{
               errores.add(new Errores(
                    getPreviousToken().getValor(),
                    "Error en la última linea",
                    getPreviousToken().getLine(),
                    getPreviousToken().getColumn()
                )); 
            }
            
        }
    }
    if(isAtEnd()){
        System.out.println("Ya se acabaron los tokens.");
        System.out.println("Puntero final: " + currentTokenIndex);
    }
    // Verificar que todos los elementos estén presentes al final del análisis
    
}

    private void analisisFinal(){
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
    private void parseMetodo() {
    if (check(Tokens.PALABRA_CLAVE) && getCurrentToken().getValor().equals("HUNGARO")) {
        metodoActual = "HUNGARO";
        parseHungaro();
        analisisFinal();
        inicializarEstado();
    } else if (check(Tokens.PALABRA_CLAVE) && getCurrentToken().getValor().equals("VOGEL")) {
        metodoActual = "VOGEL";
        parseVogel();
        analisisFinal();
        inicializarEstado();
    } else if (check(Tokens.PALABRA_CLAVE) && getCurrentToken().getValor().equals("ESQNOROESTE")) {
        metodoActual = "ESQNOROESTE";
        parseEsqNoroeste();
        analisisFinal();
        inicializarEstado();
    } else if (check(Tokens.PALABRA_CLAVE) && getCurrentToken().getValor().equals("CRUCEARROYO")) {
        metodoActual = "CRUCEARROYO";
        parseCruceArroyo();
        analisisFinal();
        inicializarEstado();
    } else {
        throw new RuntimeException("Método no reconocido: " + getCurrentToken().getValor());
    }
}

    // Analiza una declaración de asignación
    private void parseHungaro() {
    if(primerCiclo == true){
       System.out.println("Estamos en el primer ciclo");
       printDebugInfo();
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'HUNGARO'");
        printDebugInfo();
        consume(Tokens.OPEN_BRACE, "Se esperaba '{' después de 'HUNGARO'");
        printDebugInfo(); 
    }else{
        System.out.println("No estamos en el primer ciclo");
    }
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
    if(primerCiclo == true){
       System.out.println("Estamos en el primer ciclo");
       printDebugInfo();
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'VOGEL'");
        printDebugInfo();
        consume(Tokens.OPEN_BRACE, "Se esperaba '{' después de 'VOGEL'");
        printDebugInfo(); 
    }else{
        System.out.println("No estamos en el primer ciclo");
        System.out.println("MetodoActual: "+metodoActual);
    }
    
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
                    printDebugInfo();
                    parseTareas();
                    printDebugInfo();
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
                    printDebugInfo();
                    parseCostos("VOGEL");
                    printDebugInfo();
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
                default -> {
                    throw new RuntimeException("Palabra clave no reconocida dentro de 'VOGEL': " + value);
                }
            }
        } else {
            throw new RuntimeException("Se esperaba una palabra clave dentro de 'VOGEL'");
        }
    }
    System.out.println("A PUNTO DE CERRAR EL METODO");
    printDebugInfo();
    consume(Tokens.CLOSE_BRACE, "Se esperaba '}' para cerrar 'VOGEL'");
    System.out.println("DESPUÉS DE CERRAR EL METODO");
}

    private void parseEsqNoroeste() {
        if(primerCiclo == true){
            System.out.println("Estamos en el primer ciclo");
            printDebugInfo();
             consume(Tokens.PALABRA_CLAVE, "Se esperaba 'ESQNOROESTE'");
             printDebugInfo();
             consume(Tokens.OPEN_BRACE, "Se esperaba '{' después de 'ESQNOROESTE'");
             printDebugInfo(); 
         }else{
             System.out.println("No estamos en el primer ciclo");
         }

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
                    case "RESOLVER" -> {
                        if (!costosPresentes) {
                        errores.add(new Errores(
                            "RESOLVER",
                            "Se esperaba 'COSTOS' antes de 'RESOLVER'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                        }
                        verificarOrden(6, "ESQNOROESTE");
                        parseResolver();
                        resolverPresente = true;
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
        if(primerCiclo == true){
            System.out.println("Estamos en el primer ciclo");
            printDebugInfo();
             consume(Tokens.PALABRA_CLAVE, "Se esperaba 'CRUCEARROYO'");
             printDebugInfo();
             consume(Tokens.OPEN_BRACE, "Se esperaba '{' después de 'CRUCEARROYO'");
             printDebugInfo(); 
         }else{
             System.out.println("No estamos en el primer ciclo");
         }

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
                    case "RESOLVER" -> {
                        if (!costosPresentes) {
                        errores.add(new Errores(
                            "RESOLVER",
                            "Se esperaba 'COSTOS' antes de 'RESOLVER'",
                            getCurrentToken().getLine(),
                            getCurrentToken().getColumn()
                        ));
                        }
                        verificarOrden(6, "ESQNOROESTE");
                        parseResolver();
                        resolverPresente = true;
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
            if (!fuentesPresentes) faltantes.append("FUENTES, ");
            if (!destinosPresentes) faltantes.append("DESTINOS, ");
            if (!ofertaPresente) faltantes.append("OFERTA, ");
            if (!demandaPresente) faltantes.append("DEMANDA, ");
            if (!costosPresentes) faltantes.append("COSTOS, ");
            if (!resolverPresente) faltantes.append("RESOLVER, ");
            
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
            case 3 -> "OFERTA";
            case 4 -> "DEMANDA";
            case 5 -> "COSTOS";
            case 6 -> "RESOLVER";
            default -> "DESCONOCIDO";
        };
        default -> "DESCONOCIDO";
    };
}

    // Analiza la lista de recursos
    private void parseRecursos() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'RECURSOS'");
        printDebugInfo();
        consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'RECURSOS'");
        printDebugInfo();
        String listaIdentificadores = parseListaIdentificadores("RECURSOS");
        tablaSimbolos.registrarSimbolo("RECURSOS", "Lista de Identificadores", metodoActual, listaIdentificadores);
        consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'RECURSOS'");
        printDebugInfo();
    }

    // Analiza la lista de tareas
    private void parseTareas() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'TAREAS'");
        printDebugInfo();
        consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'TAREAS'");
        printDebugInfo();
        String listaIdentificadores = parseListaIdentificadores("TAREAS");
        tablaSimbolos.registrarSimbolo("TAREAS", "Lista de Identificadores", metodoActual, listaIdentificadores);
        System.out.println("A punto de evaluar el semicolon");
        printDebugInfo();
        consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'TAREAS'");
        printDebugInfo();
    }

    // Analiza las matrices de costos
    private void parseCostos(String metodo) {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'COSTOS'");
        consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'COSTOS'");
        // Lista que contendrá todas las matrices parseadas
        List<List<List<Double>>> matrices = new ArrayList<>();
        int numMatrices = parseListaMatrices(matrices);
        StringBuilder listaMatrices = new StringBuilder("[");
        for (int i = 0; i < matrices.size(); i++) {
            if (i > 0) listaMatrices.append(", ");
            listaMatrices.append(matrices.get(i)); // `toString` de la lista de listas
        }
        listaMatrices.append("]");
    
        tablaSimbolos.registrarSimbolo("COSTOS", "Matriz de decimales", metodoActual, listaMatrices.toString());
        consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'COSTOS'");
        System.out.println("Numero de matrices"+numMatrices);
        
    }

    // Analiza la lista de identificadores y devuelve el conteo
    private String parseListaIdentificadores(String variable) {
        consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar la lista de identificadores");
        printDebugInfo();
        int count = 0;
        
        StringBuilder lista = new StringBuilder("[");
        boolean first = true;
        do {
            if(!first){
                lista.append(",");
            }
            printDebugInfo();
            String caracter = getCurrentToken().getValor();
            lista.append(caracter);
            consume(Tokens.IDENTIFICADOR, "Se esperaba un identificador");
            first = false;
            printDebugInfo();
            count++;
        } while (match(Tokens.COMA));
        System.out.println("Antes de evaluar el close bracket de tareas");
        printDebugInfo();
        consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar la lista de identificadores");
        lista.append("]");
        System.out.println("Despues de evaluar el close bracket de tareas");
        printDebugInfo();
        switch(variable){
            case "RECURSOS" -> numRecursos = count;
            case "TAREAS" -> numTareas = count;
            case "FUENTES" -> numFuentes = count;
            case "DESTINOS" -> numDestinos = count;
        }
        return lista.toString();
    }

    // Analiza una lista de matrices y devuelve el conteo de filas
    private int parseListaMatrices(List<List<List<Double>>> matrices) {
        printDebugInfo();
        consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar las matrices");
        int count = 0;
        do {
            printDebugInfo();
            List<List<Double>> matriz = parseMatriz();
            matrices.add(matriz);
            //parseMatriz(metodoActual);
            count++;
        } while (match(Tokens.COMA));
        consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar las matrices");
        return count;
    }
    
    // Analiza una matriz
private List<Double> parseFilaMatriz() {
    System.out.println("Comenzando parseo de fila de matriz");
    printDebugInfo(); // Mostrar información del estado actual
    
    // Consumir el corchete de apertura de la fila
    if (!check(Tokens.OPEN_BRACKET)) {
        throw new RuntimeException("Se esperaba '[' para comenzar una fila de la matriz en la línea " +
            getCurrentToken().getLine() + " columna " + getCurrentToken().getColumn());
    }
    consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar una fila de la matriz");
    System.out.println("Token consumido: '[' (corchete de apertura)"); 
    printDebugInfo();
    
    List<Double> fila = new ArrayList<>();
    int columnCount = 0;
    
    do {
        Token currentToken = getCurrentToken();
        System.out.println("Token actual: Tipo=" + currentToken.getTipo() + 
                           ", Lexema='" + currentToken.getValor() + 
                           "', Línea=" + currentToken.getLine() + 
                           ", Columna=" + currentToken.getColumn());
        
        // Verificar si el token actual es un número válido
        if (check(Tokens.NUMERO) || check(Tokens.DECIMAL)) {
            // Parsear el número (entero o decimal)
            fila.add(Double.parseDouble(currentToken.getValor()));
            System.out.println("Número parseado: " + fila.get(fila.size() - 1));
            advance(); // Avanzar al siguiente token
            columnCount++;
            printDebugInfo();
        } else {
            throw new RuntimeException("Se esperaba un número dentro de la matriz en la línea " + 
                currentToken.getLine() + " columna " + currentToken.getColumn());
        }
    } while (match(Tokens.COMA)); // Continuar mientras haya comas entre números
    
    // Consumir el corchete de cierre de la fila
    if (!check(Tokens.CLOSE_BRACKET)) {
        throw new RuntimeException("Se esperaba ']' para cerrar la fila de la matriz en la línea " +
            getCurrentToken().getLine() + " columna " + getCurrentToken().getColumn());
    }
    consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar la fila de la matriz");
    System.out.println("Token consumido: ']' (corchete de cierre)");
    printDebugInfo();
    
    
    System.out.println("Fila de matriz parseada: " + fila);
    return fila;
}

private List<List<Double>> parseMatriz() {
    System.out.println("Comenzando parseo de matriz");
    printDebugInfo(); // Mostrar estado inicial
    
    List<List<Double>> matriz = new ArrayList<>();
    int rowCount = 0;
    
    do {
        System.out.println("Intentando parsear fila " + (rowCount + 1));
        printDebugInfo();
        
        // Parsear una fila
        List<Double> fila = parseFilaMatriz();
        matriz.add(fila);
        rowCount++;
        
        System.out.println("Fila parseada correctamente: " + fila);
        printDebugInfo();
    } while (match(Tokens.COMA));
    
    // Validar dimensiones de la matriz
 
    if (metodoActual.equals("HUNGARO") || metodoActual.equals("VOGEL")) {
    if(numRecursos > 0){
        if (rowCount != numRecursos) {
            throw new RuntimeException("El número de filas en la matriz debe ser igual al número de recursos");
        }
    }
    
} else if (metodoActual.equals("CRUCEARROYO") || metodoActual.equals("ESQNOROESTE")) {
    if(numFuentes > 0){
        if (rowCount != numFuentes) {
                throw new RuntimeException("El número de filas en la matriz debe ser igual al número de fuentes");
            }
    } 
} else {
    throw new RuntimeException("Método no reconocido: " + metodoActual);
}

    
    // Validar que todas las filas tengan el mismo número de columnas
    int columnCount = matriz.get(0).size();
    for (List<Double> fila : matriz) {
        if (metodoActual.equals("HUNGARO") || metodoActual.equals("VOGEL")) {
            if (fila.size() != columnCount) {
                throw new RuntimeException("Todas las filas de la matriz deben tener el mismo número de columnas");
            }
        }else if (metodoActual.equals("CRUCEARROYO") || metodoActual.equals("ESQNOROESTE")) {
            if (fila.size() != columnCount) {
                throw new RuntimeException("Todas las filas de la matriz deben tener el mismo número de columnas");
            }
        }
    }

    System.out.println("Parseo de matriz completado exitosamente");
    return matriz;
}

/**
 * Método para mostrar el token actual y los tokens restantes para debugging.
 */
private void printDebugInfo() {
    System.out.println("Estado actual del parser:");
    if (tokens.isEmpty()) {
        System.out.println("No hay tokens disponibles.");
        return;
    }
    int currentIndex = currentTokenIndex;
    System.out.println("Puntero actual: " + currentIndex);
    System.out.println("Token actual: " + tokens.get(currentIndex));
    System.out.println("Tokens restantes: "+ (tokens.size() - currentIndex));
}





    // Analiza el objetivo MINIMIZAR o MAXIMIZAR
    private void parseObjetivo() {
        printDebugInfo();
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'MINIMIZAR' o 'MAXIMIZAR'");
        printDebugInfo();
        tablaSimbolos.registrarSimbolo(getPreviousToken().getValor(), "Función", metodoActual, "Ejecutar el programa");
        consume(Tokens.SEMICOLON, "Se esperaba ';' después del objetivo");
    }
    
    // Analiza el objetivo MINIMIZAR o MAXIMIZAR
    private void parseResolver() {
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'RESOLVER'");
        tablaSimbolos.registrarSimbolo("RESOLVER", "Función", metodoActual, "Ejecutar el programa");
        consume(Tokens.SEMICOLON, "Se esperaba ';' después de RESOLVER");
    }


    // Manejo de errores y sincronización
    private void sincronizar() {
        int contadorCiclos = 0;
        primerCiclo = false;
        System.out.println("Antes del advance");
        printDebugInfo();
        System.out.println("Despues del advance");
        printDebugInfo();
        while (!isAtEnd()) {
            if (getCurrentToken().getTipo() == Tokens.PALABRA_CLAVE) {
                switch(getCurrentToken().getValor()){
                    case "HUNGARO" -> {
                        metodoActual = "HUNGARO";
                        advance();
                        contadorCiclos++;
                        if(contadorCiclos>50){
                            salirConfirmacion();
                        }
                    }
                    case "VOGEL" -> {
                        metodoActual = "VOGEL";
                        advance();
                        contadorCiclos++;
                        if(contadorCiclos>50){
                            salirConfirmacion();
                        }
                    }
                    case "ESQNOROESTE" -> {
                        metodoActual = "ESQNOROESTE";
                        advance();
                        contadorCiclos++;
                        if(contadorCiclos>50){
                            salirConfirmacion();
                        }
                    }
                    case "CRUCEARROYO" -> {
                        metodoActual = "CRUCEARROYO";
                        advance();
                        contadorCiclos++;
                        if(contadorCiclos>50){
                            salirConfirmacion();
                        }
                    }
                }
                return;
            }
            advance();
            contadorCiclos++;
            if(contadorCiclos>50){
                            salirConfirmacion();
                        }
        }
    }
    

    
private void parseFuentes() {
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'FUENTES'");
    consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'FUENTES'");
    String listaIdentificadores = parseListaIdentificadores("FUENTES");
    tablaSimbolos.registrarSimbolo("FUENTES", "Lista de Identificadores", metodoActual, listaIdentificadores);
    consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'FUENTES'");
}

private void parseDestinos() {
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'DESTINOS'");
    consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'DESTINOS'");
    String listaIdentificadores = parseListaIdentificadores("DESTINOS");
    tablaSimbolos.registrarSimbolo("DESTINOS", "Lista de Identificadores", metodoActual, listaIdentificadores);
    consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'DESTINOS'");
}

private void parseOferta() {
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'OFERTA'");
    consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'OFERTA'");
    String listaNumerica = parseListaNumerica("OFERTA");
    tablaSimbolos.registrarSimbolo("OFERTA", "Lista Numérica", metodoActual, listaNumerica);
    consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'OFERTA'");
}

private void parseDemanda() {
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'DEMANDA'");
    consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'DEMANDA'");
    String listaNumerica = parseListaNumerica("DEMANDA");
    tablaSimbolos.registrarSimbolo("DEMANDA", "Lista Numérica", metodoActual, listaNumerica);
    consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'DEMANDA'");
}

// Analiza una lista de valores numéricos y devuelve el conteo
private String parseListaNumerica(String variable) {
    consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar la lista de valores numéricos");
    int count = 0;
    StringBuilder lista = new StringBuilder("[");
    lista.append("[");
    boolean first = true;
    do {
        if(!first){
                lista.append(",");
        }
        String caracter = getCurrentToken().getValor();
        lista.append(caracter);
        first = false;
        if (check(Tokens.NUMERO) || check(Tokens.DECIMAL)) {
            advance(); // Avanza si el token es un número válido (entero o decimal)
            count++;
        } else {
            throw new RuntimeException("Se esperaba un valor numérico (entero o decimal)");
        }
    } while (match(Tokens.COMA)); // Permite múltiples valores separados por comas
    consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar la lista de valores numéricos");
    lista.append("]");
    switch(variable){
            case "OFERTA" -> numOfertas = count;
            case "DEMANDA" -> numDemandas = count;
        }
        return lista.toString();
}

    // Métodos de ayuda
    private void inicializarEstado() {
        primerCiclo = true;
        numRecursos = 0;
        numTareas = 0;
        numFuentes = 0;
        numDestinos = 0;
        numOfertas = 0;
        numDemandas = 0;
    
        recursosPresentes = false;
        tareasPresentes = false;
        costosPresentes = false;
        objetivoPresente = false;
        fuentesPresentes = false;
        destinosPresentes = false;
        ofertaPresente = false;
        demandaPresente = false;
        resolverPresente = false;
        primerCiclo = true;
        metodoActual = "";
        ultimoElementoProcesado = 0;
    }

    private Token advance() {
        if (!isAtEnd()) {
            currentTokenIndex++;
        }
        return getPreviousToken();
    }

    private boolean match(Tokens expected) {
        if (check(expected)) {
            consume(Tokens.COMA, "Se esperaba una coma");
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
    
    private static void salirConfirmacion() {
        int response = JOptionPane.showConfirmDialog(null, "¿Deseas salir del bucle?", "Confirmar salida",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }
}
