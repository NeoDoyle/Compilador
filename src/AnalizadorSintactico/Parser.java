package AnalizadorSintactico;

import AnalizadorLexico.Token;
import AnalizadorLexico.Tokens;
import AnalizadorLexico.Errores;
import compilador.TablaSimbolosFrame;
import compilador.ParseTreeFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.swing.tree.DefaultMutableTreeNode;
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
    private ParseTreeFrame parseTreeRoot;
    
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
    private int contadorCiclos = 0;

    private int ultimoElementoProcesado = 0; // 1: Recursos, 2: Tareas, 3: Costos, 4: Objetivo

    // Constructor
    public Parser(List<Token> tokens, List<Errores> errores, TablaSimbolosFrame tablaSimbolos) {
        this.tokens = tokens;
        this.errores = errores;
        this.currentTokenIndex = 0;
        this.tablaSimbolos = tablaSimbolos;
        this.parseTreeRoot = new ParseTreeFrame("ROOT"); // Nodo raiz del árbol
    }

    // Metodo principal para iniciar el analisis
    public void parse() {
    DefaultMutableTreeNode parentNode = parseTreeRoot.getRootNode();
    while (!isAtEnd()) {
        try {
            if (primerCiclo == true) {
                parseMetodo(parentNode); // Analiza la primera declaracion principal 
            } else {
                System.out.println("Entrando al bloque después de error");
                printDebugInfo();
                if(!isAtEnd()){
                    switch(metodoActual){
                        case "HUNGARO" -> {
                            System.out.println("Volviendo al método" + metodoActual + "después de error");
                            parseHungaro(parentNode);
                        }
                        case "VOGEL" -> {
                            System.out.println("Volviendo al método" + metodoActual + "después de error");
                            parseVogel(parentNode);
                        }
                        case "ESQNOROESTE" -> {
                            System.out.println("Volviendo al método" + metodoActual + "después de error");
                            parseEsqNoroeste(parentNode);
                        }
                        case "CRUCEARROYO" -> {
                            System.out.println("Volviendo al método" + metodoActual + "después de error");
                            parseCruceArroyo(parentNode);
                        }
                        default -> {
                            throw new RuntimeException("Método no reconocido");                           
                        }
                        
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
                DefaultMutableTreeNode errorNode = new DefaultMutableTreeNode("Error: " + e.getMessage());
                parentNode.add(errorNode);
              if(metodoActual.equals("")){
                  break;
              }else{
                  sincronizar(); // Intenta continuar despues del error
              }
              
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
    //verificar que todos los elementos esten presentes al final del analisis
    
    
    
}

    private void analisisFinal(){
        try {
            if (metodoActual != null) { // Solo verifica si hay un metodo procesandose
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
    //analiza una declaración principal
    private void parseMetodo(DefaultMutableTreeNode parentNode) {
    if (check(Tokens.PALABRA_CLAVE) && getCurrentToken().getValor().equals("HUNGARO")) {
        metodoActual = "HUNGARO";
        parseHungaro(parentNode);
        analisisFinal();
        inicializarEstado();
    } else if (check(Tokens.PALABRA_CLAVE) && getCurrentToken().getValor().equals("VOGEL")) {
        metodoActual = "VOGEL";
        parseVogel(parentNode);
        analisisFinal();
        inicializarEstado();
    } else if (check(Tokens.PALABRA_CLAVE) && getCurrentToken().getValor().equals("ESQNOROESTE")) {
        metodoActual = "ESQNOROESTE";
        parseEsqNoroeste(parentNode);
        analisisFinal();
        inicializarEstado();
    } else if (check(Tokens.PALABRA_CLAVE) && getCurrentToken().getValor().equals("CRUCEARROYO")) {
        metodoActual = "CRUCEARROYO";
        parseCruceArroyo(parentNode);
        analisisFinal();
        inicializarEstado();
    } else {
        throw new RuntimeException("Método no reconocido: " + getCurrentToken().getValor());
    }
}

    // alaisisis una declaración de asignacion
    private void parseHungaro(DefaultMutableTreeNode parentNode) {
    DefaultMutableTreeNode hungaroNode = new DefaultMutableTreeNode("Método: HUNGARO");    
    if(primerCiclo == true){
       System.out.println("Estamos en el primer ciclo");
       printDebugInfo();
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'HUNGARO'",parentNode);
        parentNode.add(hungaroNode);
        printDebugInfo();
        consume(Tokens.OPEN_LLAVE, "Se esperaba '{' después de 'HUNGARO'",hungaroNode);
        printDebugInfo(); 
    }else{
        System.out.println("No estamos en el primer ciclo");
    }
    while (!check(Tokens.CLOSE_LLAVE)) {
        if (check(Tokens.PALABRA_CLAVE)) {
            String value = getCurrentToken().getValor();
            switch (value) {
                case "RECURSOS" -> {
                    verificarOrden(1, "HUNGARO");
                    parseRecursos(hungaroNode);
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
                    parseTareas(hungaroNode);
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
                    parseCostos("HUNGARO", hungaroNode);
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
                    parseObjetivo(hungaroNode);
                    objetivoPresente = true;
                }
                default -> throw new RuntimeException("Palabra clave no reconocida dentro de 'HUNGARO': " + value);
            }
        } else {
            throw new RuntimeException("Se esperaba una palabra clave dentro de 'HUNGARO'");
        }
    }

    consume(Tokens.CLOSE_LLAVE, "Se esperaba '}' para cerrar 'HUNGARO'",hungaroNode);
    metodoActual="";
}
    
     private void parseVogel(DefaultMutableTreeNode parentNode) {
    DefaultMutableTreeNode vogelNode = new DefaultMutableTreeNode("Método: VOGEL");     
    if(primerCiclo == true){
       System.out.println("Estamos en el primer ciclo");
       printDebugInfo();
       
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'VOGEL'",parentNode);
        parentNode.add(vogelNode);
        printDebugInfo();
        consume(Tokens.OPEN_LLAVE, "Se esperaba '{' después de 'VOGEL'",parentNode);
        printDebugInfo(); 
    }else{
        System.out.println("No estamos en el primer ciclo");
        System.out.println("MetodoActual: "+metodoActual);
    }
    
    while (!check(Tokens.CLOSE_LLAVE)) {
        if (check(Tokens.PALABRA_CLAVE)) {
            String value = getCurrentToken().getValor();
            switch (value) {
                case "RECURSOS" -> {              
                    verificarOrden(1, "VOGEL");
                    parseRecursos(vogelNode);
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
                    parseTareas(vogelNode);
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
                    parseCostos("VOGEL",vogelNode);
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
                    parseObjetivo(vogelNode);
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
    consume(Tokens.CLOSE_LLAVE, "Se esperaba '}' para cerrar 'VOGEL'",vogelNode);
    metodoActual="";
    System.out.println("DESPUÉS DE CERRAR EL METODO");
}

    private void parseEsqNoroeste(DefaultMutableTreeNode parentNode) {
        DefaultMutableTreeNode esqNoroesteNode = new DefaultMutableTreeNode("Método: ESQNOROESTE");
        if(primerCiclo == true){
            System.out.println("Estamos en el primer ciclo");
            printDebugInfo();
            
             consume(Tokens.PALABRA_CLAVE, "Se esperaba 'ESQNOROESTE'",esqNoroesteNode);
             parentNode.add(esqNoroesteNode);
             printDebugInfo();
             consume(Tokens.OPEN_LLAVE, "Se esperaba '{' después de 'ESQNOROESTE'",esqNoroesteNode);
             printDebugInfo(); 
         }else{
             System.out.println("No estamos en el primer ciclo");
         }

        while (!check(Tokens.CLOSE_LLAVE)) {
            if (check(Tokens.PALABRA_CLAVE)) {
                String value = getCurrentToken().getValor();
                switch (value) {
                    case "FUENTES" -> {
                        verificarOrden(1, "ESQNOROESTE");
                        parseFuentes(esqNoroesteNode);
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
                        parseDestinos(esqNoroesteNode);
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
                        parseOferta(esqNoroesteNode);
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
                        parseDemanda(esqNoroesteNode);
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
                        parseCostos("ESQNOROESTE",esqNoroesteNode);
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
                        parseResolver(esqNoroesteNode);
                        resolverPresente = true;
                    }
                    default -> throw new RuntimeException("Palabra clave no reconocida dentro de 'ESQNOROESTE': " + value);
                }
            } else {
                throw new RuntimeException("Se esperaba una palabra clave dentro de 'ESQNOROESTE'");
            }
        }

        consume(Tokens.CLOSE_LLAVE, "Se esperaba '}' para cerrar 'ESQNOROESTE'",esqNoroesteNode);
        metodoActual="";
    }
    
        private void parseCruceArroyo(DefaultMutableTreeNode parentNode) {
        DefaultMutableTreeNode cruceArroyoNode = new DefaultMutableTreeNode("Método: CRUCEARROYO");    
        if(primerCiclo == true){
            System.out.println("Estamos en el primer ciclo");
            printDebugInfo();
            
             consume(Tokens.PALABRA_CLAVE, "Se esperaba 'CRUCEARROYO'",parentNode);
             parentNode.add(cruceArroyoNode);
             printDebugInfo();
             consume(Tokens.OPEN_LLAVE, "Se esperaba '{' después de 'CRUCEARROYO'",parentNode);
             printDebugInfo(); 
         }else{
             System.out.println("No estamos en el primer ciclo");
         }

        while (!check(Tokens.CLOSE_LLAVE)) {
            if (check(Tokens.PALABRA_CLAVE)) {
                String value = getCurrentToken().getValor();
                switch (value) {
                    case "FUENTES" -> {
                        verificarOrden(1, "CRUCEARROYO");
                        parseFuentes(cruceArroyoNode);
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
                        parseDestinos(cruceArroyoNode);
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
                        parseOferta(cruceArroyoNode);
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
                        parseDemanda(cruceArroyoNode);
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
                        parseCostos("CRUCEARROYO", cruceArroyoNode);
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
                        parseResolver(cruceArroyoNode);
                        resolverPresente = true;
                    }
                    default -> throw new RuntimeException("Palabra clave no reconocida dentro de 'CRUCEARROYO': " + value);
                }
            } else {
                throw new RuntimeException("Se esperaba una palabra clave dentro de 'CRUCEARROYO'");
            }
        }

        consume(Tokens.CLOSE_LLAVE, "Se esperaba '}' para cerrar 'CRUCEARROYO'",cruceArroyoNode);
        metodoActual="";
    }
    
    // Verifica si algun elemento obligatorio falta
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
    }

    if (faltantes.length() > 0) {
        // Eliminar la ultima coma y espacio
        faltantes.setLength(faltantes.length() - 2);
        throw new RuntimeException("Faltan los siguientes elementos obligatorios en '" + metodo + "': " + faltantes);
    }
}


    // Verifica si los elementos estan en el orden correcto
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
    private void parseRecursos(DefaultMutableTreeNode parentNode) {
        DefaultMutableTreeNode recursosNode = new DefaultMutableTreeNode("Propiedad: RECURSOS");
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'RECURSOS'",parentNode);
        parentNode.add(recursosNode);
        printDebugInfo();
        consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'RECURSOS'",parentNode);
        printDebugInfo();
        String listaIdentificadores = parseListaIdentificadores("RECURSOS", recursosNode);
        tablaSimbolos.registrarSimbolo("RECURSOS", "Lista de Identificadores", metodoActual, listaIdentificadores);
        consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'RECURSOS'",parentNode);
        printDebugInfo();
    }

    // Analiza la lista de tareas
    private void parseTareas(DefaultMutableTreeNode parentNode) {
        DefaultMutableTreeNode tareasNode = new DefaultMutableTreeNode("Propiedad: TAREAS");
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'TAREAS'",parentNode);
        parentNode.add(tareasNode);
        printDebugInfo();
        consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'TAREAS'",tareasNode);
        printDebugInfo();
        String listaIdentificadores = parseListaIdentificadores("TAREAS",tareasNode);
        tablaSimbolos.registrarSimbolo("TAREAS", "Lista de Identificadores", metodoActual, listaIdentificadores);
        System.out.println("A punto de evaluar el semicolon");
        printDebugInfo();
        consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'TAREAS'",tareasNode);
        printDebugInfo();
    }

    // Analiza las matrices de costos
    private void parseCostos(String metodo, DefaultMutableTreeNode parentNode) {
        DefaultMutableTreeNode costosNode = new DefaultMutableTreeNode("Propiedad: COSTOS");
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'COSTOS'",parentNode);
        parentNode.add(costosNode);
        consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'COSTOS'",costosNode);
        // Lista que contendra todas las matrices parseadas
        List<List<List<Double>>> matrices = new ArrayList<>();
        int numMatrices = parseListaMatrices(matrices, costosNode);
        StringBuilder listaMatrices = new StringBuilder("");
        for (int i = 0; i < matrices.size(); i++) {
            if (i > 0) listaMatrices.append(", ");
            listaMatrices.append(matrices.get(i)); // `toString` de la lista de listas
        }
    
        tablaSimbolos.registrarSimbolo("COSTOS", "Matriz de decimales", metodoActual, listaMatrices.toString());
        consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'COSTOS'",costosNode);
        System.out.println("Numero de matrices"+numMatrices);
        
    }

    // Analiza la lista de identificadores y devuelve el conteo
    private String parseListaIdentificadores(String variable, DefaultMutableTreeNode parentNode) {
        DefaultMutableTreeNode listaNode = new DefaultMutableTreeNode("Lista");
        consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar la lista de identificadores",parentNode);
        parentNode.add(listaNode);
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
            consume(Tokens.IDENTIFICADOR, "Se esperaba un identificador",listaNode);
            first = false;
            printDebugInfo();
            count++;
        } while (match(Tokens.COMA,parentNode));
        System.out.println("Antes de evaluar el close bracket de tareas");
        printDebugInfo();
        consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar la lista de identificadores",listaNode);
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
    private int parseListaMatrices(List<List<List<Double>>> matrices, DefaultMutableTreeNode parentNode) {
        printDebugInfo();
        DefaultMutableTreeNode matrizNode = new DefaultMutableTreeNode("Matriz");
        consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar las matrices",parentNode);
        parentNode.add(matrizNode);
        int count = 0;
        do {
            printDebugInfo();
            List<List<Double>> matriz = parseMatriz(matrizNode);
            matrices.add(matriz);
            //parseMatriz(metodoActual);
            count++;
        } while (match(Tokens.COMA,matrizNode));
        consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar las matrices",matrizNode);
        return count;
    }
    
    // Analiza una matriz
private List<Double> parseFilaMatriz(DefaultMutableTreeNode parentNode) {
    DefaultMutableTreeNode filaNode = new DefaultMutableTreeNode("Fila");
    System.out.println("Comenzando parseo de fila de matriz");
    printDebugInfo(); // Mostrar información del estado actual
    
    // Consumir el corchete de apertura de la fila
    if (!check(Tokens.OPEN_BRACKET)) {
        throw new RuntimeException("Se esperaba '[' para comenzar una fila de la matriz en la línea " +
            getCurrentToken().getLine() + " columna " + getCurrentToken().getColumn());
    }
    consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar una fila de la matriz",parentNode);
    parentNode.add(filaNode);
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
        
        // Verificar si el token actual es un numero valido
        if (check(Tokens.NUMERO) || check(Tokens.DECIMAL)) {
            // Parsear el número (entero o decimal)
            fila.add(Double.parseDouble(currentToken.getValor()));
            DefaultMutableTreeNode valorNode = new DefaultMutableTreeNode(getCurrentToken().getValor());
            filaNode.add(valorNode);
            System.out.println("Número parseado: " + fila.get(fila.size() - 1));
            advance(); // Avanzar al siguiente token
            columnCount++;
            printDebugInfo();
        } else {
            throw new RuntimeException("Se esperaba un número dentro de la matriz en la línea " + 
                currentToken.getLine() + " columna " + currentToken.getColumn());
        }
    } while (match(Tokens.COMA,filaNode)); // Continuar mientras haya comas entre números
    
    // Consumir el corchete de cierre de la fila
    if (!check(Tokens.CLOSE_BRACKET)) {
        throw new RuntimeException("Se esperaba ']' para cerrar la fila de la matriz en la línea " +
            getCurrentToken().getLine() + " columna " + getCurrentToken().getColumn());
    }
    consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar la fila de la matriz",parentNode);
    System.out.println("Token consumido: ']' (corchete de cierre)");
    printDebugInfo();
    
    
    System.out.println("Fila de matriz parseada: " + fila);
    return fila;
}

private List<List<Double>> parseMatriz(DefaultMutableTreeNode parentNode) {
    System.out.println("Comenzando parseo de matriz");
    printDebugInfo(); // Mostrar estado inicial
    
    List<List<Double>> matriz = new ArrayList<>();
    int rowCount = 0;
    
    do {
        System.out.println("Intentando parsear fila " + (rowCount + 1));
        printDebugInfo();
        
        // Parsear una fila
        List<Double> fila = parseFilaMatriz(parentNode);
        matriz.add(fila);
        rowCount++;
        
        System.out.println("Fila parseada correctamente: " + fila);
        printDebugInfo();
    } while (match(Tokens.COMA,parentNode));
    
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

    
    // Validar que todas las filas tengan el mismo numero de columnas
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

    // Analiza el objetivo MINIMIZAR o MAXIMIZAR
    private void parseObjetivo(DefaultMutableTreeNode parentNode) {
        printDebugInfo();
        DefaultMutableTreeNode objetivoNode = new DefaultMutableTreeNode("Funcion: " + getCurrentToken().getValor());
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'MINIMIZAR' o 'MAXIMIZAR'",objetivoNode);
        printDebugInfo();
        parentNode.add(objetivoNode);
        tablaSimbolos.registrarSimbolo(getPreviousToken().getValor(), "Función", metodoActual, "Ejecutar el programa");
        consume(Tokens.SEMICOLON, "Se esperaba ';' después del objetivo",objetivoNode);
    }
    
    // Analiza el objetivo MINIMIZAR o MAXIMIZAR
    private void parseResolver(DefaultMutableTreeNode parentNode) {
        DefaultMutableTreeNode resolverNode = new DefaultMutableTreeNode("Funcion: RESOLVER");
        consume(Tokens.PALABRA_CLAVE, "Se esperaba 'RESOLVER'",resolverNode);
        parentNode.add(resolverNode);
        tablaSimbolos.registrarSimbolo("RESOLVER", "Función", metodoActual, "Ejecutar el programa");
        consume(Tokens.SEMICOLON, "Se esperaba ';' después de RESOLVER",resolverNode);
    }


    

    
private void parseFuentes(DefaultMutableTreeNode parentNode) {
    DefaultMutableTreeNode fuentesNode = new DefaultMutableTreeNode("Propiedad: FUENTES");
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'FUENTES'",parentNode);
    parentNode.add(fuentesNode);
    consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'FUENTES'",fuentesNode);
    String listaIdentificadores = parseListaIdentificadores("FUENTES",fuentesNode);
    tablaSimbolos.registrarSimbolo("FUENTES", "Lista de Identificadores", metodoActual, listaIdentificadores);
    consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'FUENTES'",fuentesNode);
}

private void parseDestinos(DefaultMutableTreeNode parentNode) {
    DefaultMutableTreeNode destinosNode = new DefaultMutableTreeNode("Propiedad: DESTINOS");
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'DESTINOS'",parentNode);
    parentNode.add(destinosNode);
    consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'DESTINOS'",destinosNode);
    String listaIdentificadores = parseListaIdentificadores("DESTINOS",destinosNode);
    tablaSimbolos.registrarSimbolo("DESTINOS", "Lista de Identificadores", metodoActual, listaIdentificadores);
    consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'DESTINOS'",destinosNode);
}

private void parseOferta(DefaultMutableTreeNode parentNode) {
    DefaultMutableTreeNode ofertaNode = new DefaultMutableTreeNode("Propiedad: OFERTA");
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'OFERTA'",parentNode);
    parentNode.add(ofertaNode);
    consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'OFERTA'",ofertaNode);
    String listaNumerica = parseListaNumerica("OFERTA",ofertaNode);
    tablaSimbolos.registrarSimbolo("OFERTA", "Lista Numérica", metodoActual, listaNumerica);
    consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'OFERTA'",ofertaNode);
}

private void parseDemanda(DefaultMutableTreeNode parentNode) {
    DefaultMutableTreeNode demandaNode = new DefaultMutableTreeNode("Propiedad: DEMANDA");
    consume(Tokens.PALABRA_CLAVE, "Se esperaba 'DEMANDA'",parentNode);
    parentNode.add(demandaNode);
    consume(Tokens.ASIGNACION, "Se esperaba '=' después de 'DEMANDA'",demandaNode);
    String listaNumerica = parseListaNumerica("DEMANDA",demandaNode);
    tablaSimbolos.registrarSimbolo("DEMANDA", "Lista Numérica", metodoActual, listaNumerica);
    consume(Tokens.SEMICOLON, "Se esperaba ';' después de 'DEMANDA'",demandaNode);
}

// Analiza una lista de valores numericos y devuelve el conteo
private String parseListaNumerica(String variable, DefaultMutableTreeNode parentNode) {
    DefaultMutableTreeNode listaNode = new DefaultMutableTreeNode("Lista");
    consume(Tokens.OPEN_BRACKET, "Se esperaba '[' para comenzar la lista de valores numéricos",listaNode);
    parentNode.add(listaNode);
    int count = 0;
    StringBuilder lista = new StringBuilder("[");
    boolean first = true;
    do {
        if(!first){
                lista.append(",");
        }
        String caracter = getCurrentToken().getValor();
        lista.append(caracter);
        first = false;
        if (check(Tokens.NUMERO)){
            consume(Tokens.NUMERO, "Se esperaba un número (entero o decimal)", listaNode);
            count++;
        } else if (check(Tokens.DECIMAL)){
            consume(Tokens.DECIMAL, "Se esperaba un número (entero o decimal)", listaNode);
            count++;
        } else{
            throw new RuntimeException("Se esperaba un valor numérico (entero o decimal)");
        }
    } while (match(Tokens.COMA,listaNode)); // Permite múltiples valores separados por comas
    consume(Tokens.CLOSE_BRACKET, "Se esperaba ']' para cerrar la lista de valores numéricos",listaNode);
    lista.append("]");
    switch(variable){
            case "OFERTA" -> numOfertas = count;
            case "DEMANDA" -> numDemandas = count;
        }
        return lista.toString();
}

    // Metodos de ayuda
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

    private boolean match(Tokens expected, DefaultMutableTreeNode parentNode) {
        if (check(expected)) {
            consume(Tokens.COMA, "Se esperaba una coma", parentNode);
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

    private void consume(Tokens expected, String errorMessage, DefaultMutableTreeNode parentNode) {
        if (check(expected)) {
            if(Set.of(Tokens.IDENTIFICADOR, Tokens.NUMERO, Tokens.DECIMAL).contains(getCurrentToken().getTipo())){
                DefaultMutableTreeNode terminalNode = new DefaultMutableTreeNode(
                getCurrentToken().getTipo() + ":" + getCurrentToken().getValor());
                parentNode.add(terminalNode);
            }
            advance();
            
        } else {
            throw new RuntimeException(errorMessage);
        }
    }
    
    
    // manejar errores y sincronizacion
    private void sincronizar() {
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
                    }
                    case "VOGEL" -> {
                        metodoActual = "VOGEL";
                        advance();  
                    }
                    case "ESQNOROESTE" -> {
                        metodoActual = "ESQNOROESTE";
                        advance(); 
                    }
                    case "CRUCEARROYO" -> {
                        metodoActual = "CRUCEARROYO";
                        advance(); 
                    } default -> {
                        if(metodoActual.equals("")){
                            throw new RuntimeException("No se inicio el metodo correctamente.");
                        }
                        
                    }
                }
                return;
            }
            advance();
            
           
        }
    }

     /**
     * Metodo para mostrar el token actual y los tokens restantes para debugging.
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
        System.out.println("Metodo actual: " + metodoActual);
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

    public ParseTreeFrame getParseTreeFrame() {
    return this.parseTreeRoot; // Devuelve la instancia del arbol
}
    
    private static void salirConfirmacion() {
        int response = JOptionPane.showConfirmDialog(null, "¿Deseas salir del bucle?", "Confirmar salida",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }
}
