/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorSintactico;
import AnalizadorLexico.Errores;
import AnalizadorLexico.Lexer;
import AnalizadorLexico.Token;
import AnalizadorSintactico.Parser;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author hp
 */
public class PuebaSintaxis {
 


    public static void main(String[] args) {
        // Entrada de prueba
        String entrada = """
        HUNGARO { 
            RECURSOS = [Trabajador1, Trabajador2, Trabajador3];
            TAREAS = [TareaA, TareaB, TareaC];
            COSTOS = [
                [4, 8,9],
                [5, 12, 7],
                [9, 3, 10.24]
            ];
            MINIMIZAR;
        }
        """;
        
        String entrada2 = """
        VOGEL { 
            RECURSOS = [Trabajador1, Trabajador2, Trabajador3];
            TAREAS = [TareaA, TareaB, TareaC];
            COSTOS = [
                [4, 8,9],
                [5, 12, 7],
                [9, 3, 10.24]
            ];
            MINIMIZAR;
        }
        """;
        
        String entrada3 = """
        ESQNOROESTE { 
            FUENTES = [Fuente1, Fuente2, Fuente3];
            DESTINOS = [Destino1, Destino2, Destino3];
            OFERTA = [10,15,20]; 
            DEMANDA = [30,10,10.5];
            COSTOS = [
                [4, 8,9],
                [5, 12, 7],
                [9, 3, 10.24]
            ];
            RESOLVER;
        }
        """;
        
        String entrada4 = """
        CRUCEARROYO { 
            FUENTES = [Fuente1, Fuente2, Fuente3];
            DESTINOS = [Destino1, Destino2, Destino3];
            OFERTA = [10,15,20];   
            DEMANDA = [30,10,10.5];
            COSTOS = [
                [4, 8,9],
                [5, 12, 7],
                [9, 3, 10.24]
            ];
            RESOLVER;
        }
        """;

        // Paso 1: Análisis léxico
        Lexer lexer = new Lexer();
        List<Errores> erroresLexicos = new ArrayList<>();
        List<Token> tokens = lexer.lex(entrada);

        // Mostrar tokens generados por el analizador léxico
        System.out.println("Tokens generados por el analizador léxico:");
        for (Token token : tokens) {
            System.out.println(token);
        }

        // Mostrar errores léxicos
        if (!erroresLexicos.isEmpty()) {
            System.out.println("\nErrores lexicos encontrados:");
            for (Errores error : erroresLexicos) {
                System.out.println(error);
            }
            return; // Termina si hay errores léxicos
        }

        // Paso 2: Análisis sintáctico
        List<Errores> erroresSintacticos = new ArrayList<>();
        Parser parser = new Parser(tokens, erroresSintacticos);
        parser.parse();

        // Mostrar errores sintácticos
        if (!erroresSintacticos.isEmpty()) {
            System.out.println("\nErrores sintacticos encontrados:");
            for (Errores error : erroresSintacticos) {
                System.out.println(error);
            }
        } else {
            System.out.println("\nAnalisis sintactico completado sin errores.");
        }
    }
    
}
   

